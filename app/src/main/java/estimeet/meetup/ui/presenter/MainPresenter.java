package estimeet.meetup.ui.presenter;

import android.Manifest;
import android.content.Context;
import android.os.AsyncTask;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import estimeet.meetup.interactor.CancelSessionInteractor;
import estimeet.meetup.interactor.CreateSessionInteractor;
import estimeet.meetup.interactor.DeleteNotificationInteractor;
import estimeet.meetup.interactor.GetNotificationInteractor;
import estimeet.meetup.interactor.MainInteractor;
import estimeet.meetup.interactor.PushInteractor;
import estimeet.meetup.interactor.SendGeoDataInteractor;
import estimeet.meetup.model.FriendSession;
import estimeet.meetup.ui.BaseView;
import estimeet.meetup.ui.fragment.BaseFragment;
import estimeet.meetup.util.MeetupLocationService;
import estimeet.meetup.util.SessionFactory;

/**
 * Created by AmyDuan on 6/02/16.
 */
public class MainPresenter extends BasePresenter implements GetNotificationInteractor.GetNotificationListener,
        MainInteractor.MainListener{

    @Inject MainInteractor mainInteractor;
    @Inject PushInteractor pushInteractor;
    @Inject GetNotificationInteractor notificationInteractor;
    @Inject DeleteNotificationInteractor deleteNotificationInteractor;
    @Inject CancelSessionInteractor cancelSessionInteractor;
    @Inject SendGeoDataInteractor geoInteractor;
    @Inject CreateSessionInteractor createSessionInteractor;

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
                         CreateSessionInteractor createSessionInteractor) {
        this.context = context;
        this.mainInteractor = mainInteractor;
        this.pushInteractor = pushInteractor;
        this.notificationInteractor = notificationInteractor;
        this.deleteNotificationInteractor = deleteNotificationInteractor;
        this.cancelSessionInteractor = cancelSessionInteractor;
        this.geoInteractor = geoInteractor;
        this.createSessionInteractor = createSessionInteractor;
    }

    @Override
    public void onResume() {
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
            long expires = 0;
            if (isRequestSession) {
                mainInteractor.onSessionRequest(friendSession);
            } else {
                //accept session
            }

            onStartLocationService(expires);
        } else {
            view.get().onError(BaseFragment.ERROR_LOCATION_PERMISSION + "");
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
        this.friendSession = friendSession;
        isRequestSession = true;
        view.get().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    public void requestNotification() {
        if (!isGetNotificationInProcess) {
            notificationInteractor.getNotifications();
        }
    }

    public void createNewSession(FriendSession friendSession) {
        int lengthInMins = SessionFactory.getSessionLengthInMinutes(friendSession.getRequestedLength());
        long expireInMillis = TimeUnit.MINUTES.toMinutes(lengthInMins);
        createSessionInteractor.call(this);
        createSessionInteractor.createSession(friendSession, expireInMillis);

        if (expireInMillis > 0) {
            MeetupLocationService.getInstance(context).startLocationService(expireInMillis);
        }
    }

    public void ignoreSession(FriendSession friendSession) {
        mainInteractor.onSessionIgnored(friendSession);
    }

    public void cancelSession(FriendSession friendSession) {
        cancelSessionInteractor.call(this);
        cancelSessionInteractor.cancelSession(friendSession);
    }
    //endregion

    //region mainInteractor callback
    @Override
    public void getNotificationFinished() {
        isGetNotificationInProcess = false;
        deleteNotificationInteractor.call();
        checkSessionExpiration();
    }

    @Override
    public void onError(String errorMessage) {
        view.get().onError(errorMessage);
    }

    @Override
    public void onNoActiveSessions() {
        view.get().onNoActiveSessions();
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
    }
}
