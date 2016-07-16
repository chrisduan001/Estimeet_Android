package estimeet.meetup.interactor;

import java.util.List;

import javax.inject.Inject;

import estimeet.meetup.DefaultSubscriber;
import estimeet.meetup.model.ListItem;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.UserFromSearch;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import rx.Observable;

/**
 * Created by AmyDuan on 16/07/16.
 */
public class SearchFriendInteractor extends BaseInteractor<ListItem<UserFromSearch>> {

    private SearchFriendSubscriber subscriber;
    private String phone;

    @Inject
    public SearchFriendInteractor(ServiceHelper service, DataHelper data, MeetUpSharedPreference sp) {
        super(service, data, sp);
    }

    public void unSubscribe() {
        if (subscriber != null && !subscriber.isUnsubscribed()) {
            subscriber.unsubscribe();
        }
    }

    public void searchFriendByPhone(String phone, SearchFriendListener listener) {
        subscriber = new SearchFriendSubscriber(listener, dataHelper, baseUser.id);
        this.phone = phone;
        makeRequest(subscriber, true);
    }

    @Override
    protected Observable<ListItem<UserFromSearch>> getObservable() {
        return serviceHelper.searchFriendByPhone(baseUser.token, phone);
    }

    private static class SearchFriendSubscriber extends DefaultSubscriber<ListItem<UserFromSearch>> {
        private SearchFriendListener listener;
        private DataHelper dataHelper;
        private int baseUserId;

        public SearchFriendSubscriber(SearchFriendListener listener, DataHelper dataHelper, int baseUserId) {
            this.listener = listener;
            this.dataHelper = dataHelper;
            this.baseUserId = baseUserId;
        }

        @Override
        public void onNext(ListItem<UserFromSearch> userFromSearchListItem) {
            if (userFromSearchListItem != null && userFromSearchListItem.items != null) {
                for (UserFromSearch user: userFromSearchListItem.items) {
                    user.isFriend = dataHelper.getFriend(user.id) != null || user.id == baseUserId;
                }

                onComplete(userFromSearchListItem.items);
            } else {
                onComplete(null);
            }
        }

        @Override
        protected void onAuthError() {
            onComplete(null);
        }

        @Override
        protected void onError(String err) {
            onComplete(null);
        }

        private void onComplete(List<UserFromSearch> users) {
            listener.onSearchCompleted(users);
        }
    }

    public interface SearchFriendListener {
        void onSearchCompleted(List<UserFromSearch> users);
    }
}
