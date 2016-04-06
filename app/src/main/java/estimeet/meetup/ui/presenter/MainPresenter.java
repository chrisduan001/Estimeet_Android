package estimeet.meetup.ui.presenter;

import android.os.AsyncTask;

import javax.inject.Inject;

import estimeet.meetup.interactor.DeleteNotificationInteractor;
import estimeet.meetup.interactor.GetNotificationInteractor;
import estimeet.meetup.interactor.MainInteractor;
import estimeet.meetup.interactor.PushInteractor;
import estimeet.meetup.model.FriendSession;
import estimeet.meetup.ui.BaseView;

/**
 * Created by AmyDuan on 6/02/16.
 */
public class MainPresenter extends BasePresenter implements
        GetNotificationInteractor.GetNotificationListener {

    private MainView view;
    private boolean isGetNotificationInProcess;

    @Inject MainInteractor mainInteractor;
    @Inject PushInteractor pushInteractor;
    @Inject GetNotificationInteractor notificationInteractor;
    @Inject DeleteNotificationInteractor deleteNotificationInteractor;

    @Inject
    public MainPresenter(MainInteractor mainInteractor, PushInteractor pushInteractor,
                         GetNotificationInteractor notificationInteractor,
                         DeleteNotificationInteractor deleteNotificationInteractor) {
        this.mainInteractor = mainInteractor;
        this.pushInteractor = pushInteractor;
        this.notificationInteractor = notificationInteractor;
        this.deleteNotificationInteractor = deleteNotificationInteractor;
    }

    @Override
    public void onResume() {
        notificationInteractor.call(this);
        notificationInteractor.getNotifications();
        isGetNotificationInProcess = true;
    }

    @Override
    public void onAuthFailed() {
        view.onAuthFailed();
    }

    //region fragment call
    public void setView(MainView view) {
        this.view = view;
    }

    public void registerPushChannel() {
        pushInteractor.registerPushChannel();
    }

    public void onSessionRequest(FriendSession friendSession) {
        mainInteractor.onSessionRequest(friendSession);
    }

    public void requestNotification() {
        if (!isGetNotificationInProcess) {
            notificationInteractor.getNotifications();
        }
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
        view.onError(errorMessage);
    }
    //endregion

    public interface MainView extends BaseView {
        void showToastMessage(String message);
    }
}
