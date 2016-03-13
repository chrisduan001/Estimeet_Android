package estimeet.meetup.interactor;

import com.squareup.picasso.Picasso;
import javax.inject.Inject;
import estimeet.meetup.DefaultSubscriber;
import estimeet.meetup.model.Friend;
import estimeet.meetup.model.ListItem;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.User;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;

/**
 * Created by AmyDuan on 12/03/16.
 */
public class FriendsInteractor extends BaseInteractor<ListItem<Friend>> {

    private GetFreindsListener listener;
    private FriendListSubscriber subscriber;
    private Picasso picasso;

    @Inject
    public FriendsInteractor(ServiceHelper service, DataHelper data, MeetUpSharedPreference sp, Picasso picasso) {
        super(service, data, sp);
        this.picasso = picasso;
    }

    //region presenter call
    public void call(GetFreindsListener listener) {
        this.listener = listener;
    }

    public void getFriendsList(User user) {
        subscriber = new FriendListSubscriber();
        makeRequest(user, serviceHelper.getFriendsList(user.token, user.id, user.userId), subscriber, true);
    }

    public void unSubscribe() {
        if (subscriber != null && !subscriber.isUnsubscribed()) {
            subscriber.unsubscribe();
        }
    }
    //endregion

    private class FriendListSubscriber extends DefaultSubscriber<ListItem<Friend>> {
        @Override
        public void onNext(ListItem<Friend> friendListItem) {
            super.onNext(friendListItem);

            listener.onFriendListCompleted();
        }

        @Override
        public void onError(Throwable e) {
            listener.onFriendListCompleted();
        }

        @Override
        protected void onError(String err) {}

        @Override
        protected void onAuthError() {}
    }

    public interface GetFreindsListener extends BaseListener {
        void onFriendListCompleted();
    }
}
