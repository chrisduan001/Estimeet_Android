package estimeet.meetup.ui.presenter;

import java.lang.ref.WeakReference;

import javax.inject.Inject;
import javax.inject.Named;

import estimeet.meetup.interactor.ManageFriendInteractor;
import estimeet.meetup.interactor.FriendsInteractor;
import estimeet.meetup.model.Friend;
import estimeet.meetup.model.User;
import estimeet.meetup.ui.BaseView;

/**
 * Created by AmyDuan on 15/03/16.
 */
public class ManageFriendPresenter extends BasePresenter implements FriendsInteractor.GetFreindsListener {

    private ManageFriendInteractor manageFriendInteractor;
    private FriendsInteractor friendsInteractor;

    private WeakReference<ManageFriendView> view;

    @Inject
    public ManageFriendPresenter(ManageFriendInteractor manageFriendInteractor,
                                 FriendsInteractor friendsInteractor) {
        this.manageFriendInteractor = manageFriendInteractor;
        this.friendsInteractor = friendsInteractor;
    }

    //region fragment call
    public void setView(ManageFriendView view) {
        this.view = new WeakReference<>(view);
    }

    public void requestFriendList() {
        friendsInteractor.call(this);
        friendsInteractor.getFriendsList();
    }

    public void onUpdateFriend(Friend friend) {
        manageFriendInteractor.updateFriendData(friend);
    }
    //endregion

    //region mainInteractor callback
    @Override
    public void onAuthFailed() {
        view.get().onAuthFailed();
    }

    @Override
    public void onFriendListCompleted() {
        view.get().onGetFriendsList();
    }

    @Override
    public void onError(String errorMessage) {
        view.get().onError(errorMessage);
    }
    //endregion

    public interface ManageFriendView extends BaseView {
        void onGetFriendsList();
    }
}
