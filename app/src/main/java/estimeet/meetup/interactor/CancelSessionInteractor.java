package estimeet.meetup.interactor;

import javax.inject.Inject;

import estimeet.meetup.DefaultSubscriber;
import estimeet.meetup.model.FriendSession;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.PostModel.SessionRequest;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import rx.Observable;

/**
 * Created by AmyDuan on 7/04/16.
 */
public class CancelSessionInteractor extends BaseInteractor<Boolean> {

    private BaseListener listener;
    private FriendSession friendSession;
    private long friendUid;

    @Inject
    public CancelSessionInteractor(ServiceHelper service, DataHelper data, MeetUpSharedPreference sp) {
        super(service, data, sp);
    }

    public void call(BaseListener listener) {
        this.listener = listener;
    }

    public void cancelSession(FriendSession friendSession) {
        dataHelper.deleteSession(friendSession.getFriendId());
        friendUid = dataHelper.getFriend(friendSession.getFriendId()).userId;
        this.friendSession = friendSession;
        makeRequest(new CancelSessionSub(), true);
    }

    @Override
    protected Observable<Boolean> getObservable() {
        return serviceHelper.cancelSession(baseUser.token,
                new SessionRequest(baseUser.id, friendSession.getFriendId(), friendUid));
    }

    private class CancelSessionSub extends DefaultSubscriber<Boolean> {

        @Override
        public void onNext(Boolean aBoolean) {
            super.onNext(aBoolean);
            if (!aBoolean) {
                throw new RuntimeException("1000");
            }
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        protected void onAuthError() {}

        @Override
        protected void onError(String err) {
            listener.onError(err);
            dataHelper.insertSession(friendSession);
        }
    }
}
