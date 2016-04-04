package estimeet.meetup.interactor;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import estimeet.meetup.DefaultSubscriber;
import estimeet.meetup.model.FriendSession;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.PostModel.SessionRequest;
import estimeet.meetup.model.User;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import estimeet.meetup.ui.adapter.FriendListAdapter;
import rx.Observable;
/**
 * Created by AmyDuan on 6/02/16.
 */

public class MainInteractor extends BaseInteractor<Boolean> {

    private MainListener listener;
    private SendSessionRequestSubscriber subscriber;

    private FriendSession session;
    private User user;

    @Inject
    public MainInteractor(ServiceHelper serviceHelper, DataHelper dataHelper,
                          MeetUpSharedPreference sp, @Named("currentUser") User user) {
        super(serviceHelper, dataHelper, sp);
        this.user = user;
    }

    //region fragment call
    public void call(MainListener listener) {
        this.listener = listener;
    }

    public void onSessionRequest(FriendSession session) {
        this.session = session;
        subscriber = new SendSessionRequestSubscriber();
        makeRequest(user, subscriber, true);
    }

    public void checkSessionExpiration() {
        List<FriendSession> sessions = dataHelper.getAllActiveSession();

        for (FriendSession session: sessions) {
            if (System.currentTimeMillis() > session.getDateCreated() + session.getTimeToExpire()) {
                onSessionFinished(session.getFriendId());
            }
        }
    }

    public void onSessionFinished(int friendId) {
        dataHelper.deleteSession(friendId);
    }
    //endregion

    @Override
    protected Observable<Boolean> getObservable(User user) {
        return serviceHelper.sendSessionRequest(user.token, 0,
                new SessionRequest(user.id, session.getFriendId(),
                        dataHelper.getFriend(session.getFriendId()).userId));
    }

    private class SendSessionRequestSubscriber extends DefaultSubscriber<Boolean> {

        @Override
        public void onNext(Boolean aBoolean) {
            super.onNext(aBoolean);

            if (aBoolean) {
                SessionFactory.createRequestedSession(session);
                dataHelper.insertSession(session);
            } else throw new RuntimeException("1000");
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        protected void onAuthError() {
            sharedPreference.removeSharedPreference();
            listener.onAuthFailed();
        }

        @Override
        protected void onError(String err) {
            listener.onError(err);
        }
    }

    public interface MainListener extends BaseListener {

    }
}
