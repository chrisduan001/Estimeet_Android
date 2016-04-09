package estimeet.meetup.ui.presenter;

import android.Manifest;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import estimeet.meetup.interactor.CancelSessionInteractor;
import estimeet.meetup.interactor.DeleteNotificationInteractor;
import estimeet.meetup.interactor.GetNotificationInteractor;
import estimeet.meetup.interactor.MainInteractor;
import estimeet.meetup.interactor.PushInteractor;
import estimeet.meetup.interactor.SendGeoDataInteractor;
import estimeet.meetup.model.FriendSession;
import estimeet.meetup.ui.BaseView;
import estimeet.meetup.ui.fragment.BaseFragment;

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

    private WeakReference<MainView> view;
    private boolean isGetNotificationInProcess;
    private boolean isRequestSession;

    private FriendSession friendSession;

    @Inject
    public MainPresenter(MainInteractor mainInteractor, PushInteractor pushInteractor,
                         GetNotificationInteractor notificationInteractor,
                         DeleteNotificationInteractor deleteNotificationInteractor,
                         CancelSessionInteractor cancelSessionInteractor,
                         SendGeoDataInteractor geoInteractor) {
        this.mainInteractor = mainInteractor;
        this.pushInteractor = pushInteractor;
        this.notificationInteractor = notificationInteractor;
        this.deleteNotificationInteractor = deleteNotificationInteractor;
        this.cancelSessionInteractor = cancelSessionInteractor;
        this.geoInteractor = geoInteractor;
    }

    @Override
    public void onResume() {
        mainInteractor.call(this);
        notificationInteractor.call(this);
        notificationInteractor.getNotifications();
        isGetNotificationInProcess = true;
    }

    @Override
    public void onAuthFailed() {
        view.get().onAuthFailed();
    }

    @Override
    public void onPermissionResult(boolean isGranted) {
        if (isGranted) {
            if (isRequestSession) {
                mainInteractor.onSessionRequest(friendSession);
            } else {
                //accept session
            }

            view.get().onLocationPermissionGranted();
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

    public void cancelSession(FriendSession friendSession) {
        cancelSessionInteractor.call(this);
        cancelSessionInteractor.cancelSession(friendSession);
    }

    public void sendUserLocation(String geoData) {
        geoInteractor.sendGeoData(geoData);
    }
    //endregion

    //region mainInteractor callback
    @Override
    public void getNotificationFinished() {
        isGetNotificationInProcess = false;
        deleteNotificationInteractor.call();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mainInteractor.checkSessionExpiration();
            }
        });
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

    public interface MainView extends BaseView {
        void onLocationPermissionGranted();
        void onNoActiveSessions();
    }
}
