package estimeet.meetup.interactor;

import javax.inject.Inject;

import estimeet.meetup.DefaultSubscriber;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.PostModel.AddFriendModel;
import estimeet.meetup.model.UserFromSearch;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import estimeet.meetup.ui.fragment.BaseFragment;
import rx.Observable;

/**
 * Created by AmyDuan on 16/07/16.
 */
public class AddFriendInteractor extends BaseInteractor<Boolean> {

    private AddFriendSubscriber subscriber;
    private AddFriendModel addFriendModel;

    @Inject
    public AddFriendInteractor(ServiceHelper service, DataHelper data, MeetUpSharedPreference sp) {
        super(service, data, sp);
    }

    public void unSubscribe() {
        if (subscriber != null && !subscriber.isUnsubscribed()) {
            subscriber.unsubscribe();
        }
    }

    public void requestAddFriend(UserFromSearch user, AddFriendListener listener) {
        addFriendModel = new AddFriendModel(baseUser.id, baseUser.userId, user.id, user.userId);
        subscriber = new AddFriendSubscriber(user, dataHelper, listener);
        makeRequest(subscriber, true);
    }

    @Override
    protected Observable<Boolean> getObservable() {
        return serviceHelper.requestAddFriend(baseUser.token, addFriendModel);
    }

    private static class AddFriendSubscriber extends DefaultSubscriber<Boolean> {
        private UserFromSearch user;
        private DataHelper dataHelper;
        private AddFriendListener listener;

        public AddFriendSubscriber(UserFromSearch user, DataHelper dataHelper, AddFriendListener listener) {
            this.user = user;
            this.dataHelper = dataHelper;
            this.listener = listener;
        }

        @Override
        public void onNext(Boolean aBoolean) {
            if (aBoolean) {
                dataHelper.insertFriend(user);
                listener.onAddSuccessful();
            } else {
                onError("");
            }
        }

        @Override
        protected void onAuthError() {
            listener.onAddFriendFailed(user.id);
            //use the default error message
            listener.onError("");
        }

        @Override
        protected void onError(String err) {
            listener.onAddFriendFailed(user.id);
            listener.onError("");
        }
    }

    public interface AddFriendListener extends BaseListener {
        void onAddSuccessful();
        void onAddFriendFailed(int userId);
    }
}
