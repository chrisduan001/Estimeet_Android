package estimeet.meetup.interactor;

import javax.inject.Inject;

import estimeet.meetup.DefaultSubscriber;
import estimeet.meetup.model.FriendSession;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.PostModel.NotificationModel;
import estimeet.meetup.model.Session;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import estimeet.meetup.ui.fragment.BaseFragment;
import estimeet.meetup.factory.SessionFactory;
import rx.Observable;

/**
 * Created by AmyDuan on 10/04/16.
 */
public class CreateSessionInteractor extends BaseInteractor<Session> {
    private FriendSession friendSession;
    private FriendSession activeFriendSession;
    private BaseListener listener;

    @Inject
    public CreateSessionInteractor(ServiceHelper service, DataHelper data, MeetUpSharedPreference sp) {
        super(service, data, sp);
    }

    public void call(BaseListener listener) {
        this.listener = listener;
    }

    public void createSession(FriendSession friendSession, long expireInMillis) {
        this.friendSession = friendSession;

        //insert active session in db first(active friend session), if network fails revert the data back to original one (friendsession)
        activeFriendSession = SessionFactory.createActiveSession(friendSession.getFriendId(), 0, 0,
                expireInMillis, friendSession.getRequestedLength());
        dataHelper.updateSession(activeFriendSession);

        makeRequest(new SessionSubscriber(), true);
    }

    @Override
    protected Observable<Session> getObservable() {
        long friendUid = dataHelper.getFriend(friendSession.getFriendId()).userId;
        return serviceHelper.createSession(baseUser.token,
                SessionFactory.getSessionLengthInMinutes(friendSession.getRequestedLength()),
                friendSession.getRequestedLength(),
                new NotificationModel(baseUser.id, friendSession.getFriendId(), friendUid));
    }

    private class SessionSubscriber extends DefaultSubscriber<Session> {

        @Override
        public void onNext(Session session) {
            super.onNext(session);

            activeFriendSession.setSessionId(session.sessionId);
            activeFriendSession.setSessionLId(session.sessionLId);
            dataHelper.updateSession(activeFriendSession);
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        protected void onAuthError() {}

        @Override
        protected void onError(String err) {

            if (err.equals(BaseFragment.ERROR_SESSION_EXPIRED + "")) {
                dataHelper.deleteSession(friendSession.getFriendId());
            }
            dataHelper.updateSession(friendSession);
            listener.onError(err);
        }
    }
}
