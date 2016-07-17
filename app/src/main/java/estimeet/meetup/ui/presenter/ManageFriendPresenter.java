package estimeet.meetup.ui.presenter;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import estimeet.meetup.interactor.AddFriendInteractor;
import estimeet.meetup.interactor.ManageFriendInteractor;
import estimeet.meetup.interactor.FriendsInteractor;
import estimeet.meetup.interactor.SearchFriendInteractor;
import estimeet.meetup.model.Friend;
import estimeet.meetup.model.User;
import estimeet.meetup.model.UserFromSearch;
import estimeet.meetup.ui.BaseView;

/**
 * Created by AmyDuan on 15/03/16.
 */
public class ManageFriendPresenter extends BasePresenter implements FriendsInteractor.GetFreindsListener,
        AddFriendInteractor.AddFriendListener, SearchFriendInteractor.SearchFriendListener{

    private ManageFriendInteractor manageFriendInteractor;
    private FriendsInteractor friendsInteractor;
    private AddFriendInteractor addFriendInteractor;
    private SearchFriendInteractor searchFriendInteractor;

    private WeakReference<ManageFriendView> view;

    @Inject
    public ManageFriendPresenter(ManageFriendInteractor manageFriendInteractor,
                                 FriendsInteractor friendsInteractor, AddFriendInteractor addFriendInteractor,
                                 SearchFriendInteractor searchFriendInteractor) {
        this.manageFriendInteractor = manageFriendInteractor;
        this.friendsInteractor = friendsInteractor;
        this.addFriendInteractor = addFriendInteractor;
        this.searchFriendInteractor = searchFriendInteractor;
    }

    @Override
    public void onPause() {
        addFriendInteractor.unSubscribe();
        searchFriendInteractor.unSubscribe();
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

    public void searchFriendByPhone(String phoneNumber) {
        searchFriendInteractor.searchFriendByPhone(phoneNumber, this);
    }

    public void addFriend(UserFromSearch user) {
        addFriendInteractor.requestAddFriend(user, this);
    }
    //endregion

    //region interactor callback
    @Override
    public void onAuthFailed() {
        view.get().onAuthFailed();
    }

    @Override
    public void onFriendListCompleted() {
        view.get().onGetFriendsList();
    }

    @Override
    public void onSearchCompleted(List<UserFromSearch> users) {
        view.get().onSearchCompleted(users);
    }

    @Override
    public void onAddFriendFailed(int userId) {
        view.get().onAddFriendFailed(userId);
    }

    @Override
    public void onAddSuccessful() {
        view.get().onAddFriendSuccessful();
    }

    @Override
    public void onError(String errorMessage) {
        view.get().onError(errorMessage);
    }
    //endregion

    public interface ManageFriendView extends BaseView {
        void onGetFriendsList();
        void onSearchCompleted(List<UserFromSearch> users);
        void onAddFriendFailed(int userId);
        void onAddFriendSuccessful();
    }
}
