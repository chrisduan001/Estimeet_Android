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
    private User user;

    private WeakReference<ManageFriendView> view;

    @Inject
    public ManageFriendPresenter(ManageFriendInteractor manageFriendInteractor,
                                 FriendsInteractor friendsInteractor, @Named("currentUser") User user) {
        this.manageFriendInteractor = manageFriendInteractor;
        this.friendsInteractor = friendsInteractor;
        this.user = user;
    }

    //region fragment call
    public void setView(ManageFriendView view) {
        this.view = new WeakReference<>(view);
    }

    public void requestFriendList() {
        friendsInteractor.call(this);
        friendsInteractor.getFriendsList(user);
    }

    public void onUpdateFriend(Friend friend) {
        manageFriendInteractor.updateFriendData(friend);
    }
    //endregion

    //region interactor callback
    @Override
    public void onAuthFailed() {
        view.get().onAuthFailed();
    }

    @Override
    public void onFriendListCompleted(boolean isAnyFriends) {
        if (isAnyFriends) {
            view.get().onGetFriendsList();
        }
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
