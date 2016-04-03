package estimeet.meetup.ui.presenter;

import javax.inject.Inject;

import estimeet.meetup.interactor.GetNotificationInteractor;
import estimeet.meetup.interactor.MainInteractor;
import estimeet.meetup.interactor.PushInteractor;
import estimeet.meetup.model.FriendSession;
import estimeet.meetup.ui.BaseView;

/**
 * Created by AmyDuan on 6/02/16.
 */
public class MainPresenter extends BasePresenter {

    private MainView view;

    @Inject MainInteractor interactor;
    @Inject PushInteractor pushInteractor;
    @Inject GetNotificationInteractor notificationInteractor;

    @Inject
    public MainPresenter(MainInteractor interactor, PushInteractor pushInteractor,
                         GetNotificationInteractor notificationInteractor) {
        this.interactor = interactor;
        this.pushInteractor = pushInteractor;
        this.notificationInteractor = notificationInteractor;
    }

    @Override
    public void onResume() {
        notificationInteractor.getNotifications();
    }

    @Override
    public void onAuthFailed() {

    }

    //region fragment call
    public void setView(MainView view) {
        this.view = view;
    }

    public void registerPushChannel() {
        pushInteractor.registerPushChannel();
    }

    public void onSessionRequest(FriendSession friendSession) {
        interactor.onSessionRequest(friendSession);
    }

    public void checkSessionExpiration() {
        interactor.checkSessionExpiration();
    }

    public void requestData() {
        view.showToastMessage("requested data");
    }
    //endregion
    public interface MainView extends BaseView {
        void showToastMessage(String message);
    }
}
