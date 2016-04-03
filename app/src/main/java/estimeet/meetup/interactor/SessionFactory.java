package estimeet.meetup.interactor;

import java.util.concurrent.TimeUnit;

import estimeet.meetup.model.FriendSession;
import estimeet.meetup.ui.adapter.FriendListAdapter;

/**
 * Created by AmyDuan on 3/04/16.
 */
public class SessionFactory {

    private static final int DEFAULT_EXPIRE_TIME = 5;

    private static void setupSessionTimeWithDefaultValue(FriendSession session) {
        session.setTimeToExpire(TimeUnit.MINUTES.toMillis(DEFAULT_EXPIRE_TIME));
        session.setDateCreated(System.currentTimeMillis());
    }

    public static void setupSessionTime(long expireTime, FriendSession session) {
        session.setDateCreated(System.currentTimeMillis());
        session.setTimeToExpire(expireTime);
    }

    public static FriendSession createRequestedSession(FriendSession session) {
        session.setType(FriendListAdapter.SENT_SESSION);
        session.setSessionFriendId(session.getFriendId());
        setupSessionTimeWithDefaultValue(session);

        return session;
    }

    public static FriendSession createActiveSession(int friendId, int sessionId, long sessionLId) {
        FriendSession session = new FriendSession();
        session.setSessionId(sessionId);
        session.setSessionLId(sessionLId);
        session.setFriendId(friendId);
        session.setType(FriendListAdapter.ACTIVE_SESSION);
        setupSessionTime(10000, session);

        return session;
    }

    public static FriendSession createPendingSession(int friendId) {
        FriendSession session = new FriendSession();
        session.setFriendId(friendId);
        session.setType(FriendListAdapter.RECEIVED_SESSION);
        setupSessionTimeWithDefaultValue(session);

        return session;
    }
}
