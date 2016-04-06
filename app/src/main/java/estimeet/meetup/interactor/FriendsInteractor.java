package estimeet.meetup.interactor;

import java.util.List;
import javax.inject.Inject;
import estimeet.meetup.DefaultSubscriber;
import estimeet.meetup.model.Friend;
import estimeet.meetup.model.ListItem;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.User;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import rx.Observable;

/**
 * Created by AmyDuan on 12/03/16.
 */
public class FriendsInteractor extends BaseInteractor<ListItem<Friend>> {

    private GetFreindsListener listener;
    private FriendListSubscriber subscriber;

    @Inject
    public FriendsInteractor(ServiceHelper service, DataHelper data, MeetUpSharedPreference sp) {
        super(service, data, sp);
    }

    //region presenter call
    public void call(GetFreindsListener listener) {
        this.listener = listener;
    }

    public void getFriendsList() {
        subscriber = new FriendListSubscriber();
        makeRequest(subscriber, true);
    }

    public void unSubscribe() {
        if (subscriber != null && !subscriber.isUnsubscribed()) {
            subscriber.unsubscribe();
        }
    }
    //endregion

    @Override
    protected Observable<ListItem<Friend>> getObservable() {
        return serviceHelper.getFriendsList(baseUser.token, baseUser.id, baseUser.userId);
    }

    private class FriendListSubscriber extends DefaultSubscriber<ListItem<Friend>> {
        @Override
        public void onNext(ListItem<Friend> friendListItem) {
            super.onNext(friendListItem);

            List<Friend> friendList = friendListItem.items;
            dataHelper.insertFriendsData(friendList);

            listener.onFriendListCompleted(friendList != null && friendList.size() > 0);
        }

        @Override
        public void onError(Throwable e) {
            listener.onFriendListCompleted(false);
        }

        @Override
        protected void onError(String err) {}

        @Override
        protected void onAuthError() {}
    }

    public interface GetFreindsListener extends BaseListener {
        void onFriendListCompleted(boolean isAnyFriends);
    }
}
