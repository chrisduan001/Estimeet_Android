package estimeet.meetup.interactor;

import java.util.List;

import javax.inject.Inject;

import estimeet.meetup.DefaultSubscriber;
import estimeet.meetup.model.FriendSession;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.PostModel.NotificationModel;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import estimeet.meetup.factory.SessionFactory;
import rx.Observable;
/**
 * Created by AmyDuan on 6/02/16.
 */

public class MainInteractor extends BaseInteractor<Boolean> {

    private MainListener listener;
    private SendSessionRequestSubscriber subscriber;

    private FriendSession session;

    @Inject
    public MainInteractor(ServiceHelper serviceHelper, DataHelper dataHelper,
                          MeetUpSharedPreference sp) {
        super(serviceHelper, dataHelper, sp);
    }

    //region fragment call
    public void call(MainListener listener) {
        this.listener = listener;
    }

    public void onSessionRequest(FriendSession session) {
        this.session = session;
        //create session
        //session will be deleted if request failed
        SessionFactory.createRequestedSession(session);
        dataHelper.insertSession(session);

        subscriber = new SendSessionRequestSubscriber();
        makeRequest(subscriber, true);
    }

    public void onSessionIgnored(FriendSession friendSession) {
        dataHelper.deleteSession(friendSession.getFriendId());
    }

    public void checkSessionExpiration() {
        List<FriendSession> sessions = dataHelper.getAllActiveSession();

        for (FriendSession session: sessions) {
            if (System.currentTimeMillis() > session.getDateCreated() + session.getTimeToExpireInMilli()) {
                onSessionFinished(session.getFriendId());
            }
        }

        if (sessions.size() <= 0) {
            listener.onNoActiveSessions();
        }
    }

    public void onSessionFinished(int friendId) {
        dataHelper.deleteSession(friendId);
    }
    //endregion

    @Override
    protected Observable<Boolean> getObservable() {
        return serviceHelper.sendSessionRequest(baseUser.token, session.getRequestedLength(),
                new NotificationModel(baseUser.id, session.getFriendId(),
                        dataHelper.getFriend(session.getFriendId()).userId));
    }

    private class SendSessionRequestSubscriber extends DefaultSubscriber<Boolean> {

        @Override
        public void onNext(Boolean aBoolean) {
            super.onNext(aBoolean);

            if (!aBoolean) {
                dataHelper.deleteSession(session.getFriendId());
                throw new RuntimeException("1000");
            }
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
            dataHelper.deleteSession(session.getFriendId());
            listener.onError(err);
        }
    }

    public interface MainListener extends BaseListener {
        void onNoActiveSessions();
    }
}
