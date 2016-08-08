package estimeet.meetup.ui.presenter;

import android.Manifest;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import estimeet.meetup.interactor.CancelSessionInteractor;
import estimeet.meetup.interactor.CreateSessionInteractor;
import estimeet.meetup.interactor.DeleteNotificationInteractor;
import estimeet.meetup.interactor.GetNotificationInteractor;
import estimeet.meetup.interactor.LocationDataInteractor;
import estimeet.meetup.interactor.MainInteractor;
import estimeet.meetup.interactor.PushInteractor;
import estimeet.meetup.interactor.SendGeoDataInteractor;
import estimeet.meetup.model.FriendSession;
import estimeet.meetup.ui.BaseView;
import estimeet.meetup.ui.fragment.BaseFragment;
import estimeet.meetup.util.MeetupLocationService;
import estimeet.meetup.factory.SessionCreationFactory;

/**
 * Created by AmyDuan on 6/02/16.
 */
public class MainPresenter extends BasePresenter implements GetNotificationInteractor.GetNotificationListener,
        MainInteractor.MainListener, LocationDataInteractor.LocationDataListener{

    @Inject MainInteractor mainInteractor;
    @Inject PushInteractor pushInteractor;
    @Inject GetNotificationInteractor notificationInteractor;
    @Inject DeleteNotificationInteractor deleteNotificationInteractor;
    @Inject CancelSessionInteractor cancelSessionInteractor;
    @Inject SendGeoDataInteractor geoInteractor;
    @Inject CreateSessionInteractor createSessionInteractor;
    @Inject LocationDataInteractor locationDataInteractor;

    private WeakReference<MainView> view;
    private boolean isGetNotificationInProcess;
    private boolean isRequestSession;
    private Context context;

    private FriendSession friendSession;

    @Inject
    public MainPresenter(Context context, MainInteractor mainInteractor, PushInteractor pushInteractor,
                         GetNotificationInteractor notificationInteractor,
                         DeleteNotificationInteractor deleteNotificationInteractor,
                         CancelSessionInteractor cancelSessionInteractor,
                         SendGeoDataInteractor geoInteractor,
                         CreateSessionInteractor createSessionInteractor,
                         LocationDataInteractor locationDataInteractor) {
        this.context = context;
        this.mainInteractor = mainInteractor;
        this.pushInteractor = pushInteractor;
        this.notificationInteractor = notificationInteractor;
        this.deleteNotificationInteractor = deleteNotificationInteractor;
        this.cancelSessionInteractor = cancelSessionInteractor;
        this.geoInteractor = geoInteractor;
        this.createSessionInteractor = createSessionInteractor;
        this.locationDataInteractor = locationDataInteractor;
    }

    @Override
    public void onResume() {
        checkSessionExpiration();
        locationDataInteractor.call(this);
        mainInteractor.call(this);
        notificationInteractor.call(this);
        notificationInteractor.getNotifications();
        isGetNotificationInProcess = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        checkSessionExpiration();
    }

    @Override
    public void onAuthFailed() {
        view.get().onAuthFailed();
    }

    @Override
    public void onPermissionResult(boolean isGranted) {
        if (isGranted) {
            //expires == 0: not continuous tracking. only need to get one off location
            if (isRequestSession) {
                mainInteractor.onSessionRequest(friendSession);
                //get travel mode to set the action bar
                mainInteractor.getTravelMode();
                onStartLocationService(0);
            } else {
                //accept session
                onPermissionGrantedForNewSession();
            }
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    view.get().onError(BaseFragment.ERROR_LOCATION_PERMISSION + "");
                }
            });

            /**
             Problem: when user denied the permission, the cell suppose to reset to the original state
             However, the cell was not reset. Not sure if this is a bug related to recyclerview/content provider
             Current solution is to create a session and then delete it after 100milliseconds
             delay is required here, otherwise it won't work
             */
            if (isRequestSession) {
                Looper.prepare();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mainInteractor.deleteSession(friendSession.getFriendId());
                    }
                }, 100);
                Looper.loop();
            }
        }
    }

    //region fragment call
    public void setView(MainView view) {
        this.view = new WeakReference<>(view);
    }

    public void registerPushChannel() {
        pushInteractor.registerPushChannel();
    }

    public void onSessionRequest(FriendSession friendSession) {
        mainInteractor.insertSession(friendSession);
        this.friendSession = friendSession;
        isRequestSession = true;
        view.get().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        view.get().checkGPSOn();

    }

    public void requestNotification() {
        if (!isGetNotificationInProcess) {
            notificationInteractor.getNotifications();
        }
    }

    public void createNewSession(FriendSession friendSession) {
        isRequestSession = false;
        this.friendSession = friendSession;
        view.get().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void onPermissionGrantedForNewSession() {
        int lengthInMins = SessionCreationFactory.getSessionLengthInMinutes(friendSession.getRequestedLength());
        long expireInMillis = TimeUnit.MINUTES.toMillis(lengthInMins);
        createSessionInteractor.call(this);
        createSessionInteractor.createSession(friendSession, expireInMillis);
        //get travel mode to set the action bar
        mainInteractor.getTravelMode();

        if (expireInMillis > 0) {
            onStartLocationService(expireInMillis);
        }
    }

    public void ignoreSession(FriendSession friendSession) {
        mainInteractor.onSessionIgnored(friendSession);
    }

    public void cancelSession(FriendSession friendSession) {
        cancelSessionInteractor.call(this);
        cancelSessionInteractor.cancelSession(friendSession);
        checkSessionExpiration();
    }

    public void requestLocationData(FriendSession friendSession) {
        locationDataInteractor.onRequestLocation(friendSession);
    }

    public void requestPendingLocationData() {
        locationDataInteractor.requestPendingLocationData();
    }

    public void setTravelMode(int mode) {
        mainInteractor.setTravelMode(mode);
    }
    //endregion

    //region interactor callback
    @Override
    public void getNotificationFinished() {
        isGetNotificationInProcess = false;
        deleteNotificationInteractor.call();
    }

    @Override
    public void onCreateNewSession(long expireTimeInMilli) {
        MeetupLocationService.getInstance(context).startLocationService(expireTimeInMilli);
    }

    @Override
    public void onError(String errorMessage) {
        view.get().onError(errorMessage);
        checkSessionExpiration();
    }

    @Override
    public void onCheckSessionExpiration(Boolean result) {
        //null == no session available, no == no active session, yes == active session
        if (result == null || !result) {
            view.get().onNoActiveSessions();
        }

        //session on pending or active session, this will show the toolbar
        if (result != null) {
            mainInteractor.getTravelMode();
        }
    }

    @Override
    public void onFailedToGetLocation() {
        view.get().onError(BaseFragment.ERROR_NULL_USER_SESSION_LOCATION + "");
    }

    @Override
    public void onGetTravelMode(int travelMode) {
        view.get().onTravelMode(travelMode);
    }
    //endregion

    //region location service
    private void onStartLocationService(long expires) {
        MeetupLocationService.getInstance(context).setGeoDataInteractor(geoInteractor);
        MeetupLocationService.getInstance(context).startLocationService(expires);
    }
    //endregion

    //region logic
    //if no active session available, the call back will stop tracking
    private void checkSessionExpiration() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mainInteractor.checkSessionExpiration();
            }
        });
    }
    //endregion

    public interface MainView extends BaseView {
        void onNoActiveSessions();

        void onTravelMode(int travelMode);

        void checkGPSOn();
    }
}
