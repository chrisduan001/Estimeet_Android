package estimeet.meetup.util;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import estimeet.meetup.R;
import estimeet.meetup.model.FriendSession;
import estimeet.meetup.ui.adapter.FriendListAdapter;

/**
 * Created by AmyDuan on 3/04/16.
 */
public class SessionFactory {

    private static final int DEFAULT_EXPIRE_TIME = 5;

    private static void setupSessionTimeWithDefaultValue(FriendSession session) {
        session.setTimeToExpireInMilli(TimeUnit.MINUTES.toMillis(DEFAULT_EXPIRE_TIME));
        session.setDateCreated(System.currentTimeMillis());
    }

    public static void setupSessionTime(long expireTime, FriendSession session) {
        session.setDateCreated(System.currentTimeMillis());
        session.setTimeToExpireInMilli(expireTime);
    }

    public static FriendSession createRequestedSession(FriendSession session) {
        session.setType(FriendListAdapter.SENT_SESSION);
        session.setSessionFriendId(session.getFriendId());
        setupSessionTimeWithDefaultValue(session);

        return session;
    }

    public static FriendSession createActiveSession(int friendId, int sessionId, long sessionLId,
                                                    long expireTimeInMilli) {
        FriendSession session = new FriendSession();
        session.setSessionId(sessionId);
        session.setSessionLId(sessionLId);
        session.setFriendId(friendId);
        session.setType(FriendListAdapter.ACTIVE_SESSION);
        setupSessionTime(expireTimeInMilli, session);

        return session;
    }

    public static FriendSession createPendingSession(int friendId, int requestedTime) {
        FriendSession session = new FriendSession();
        session.setFriendId(friendId);
        session.setRequestedLength(requestedTime);
        session.setType(FriendListAdapter.RECEIVED_SESSION);
        setupSessionTimeWithDefaultValue(session);

        return session;
    }

    public static String getSessionLengthString(int length, Context context) {
        switch (length) {
            case 0:
                return context.getString(R.string.session_length_15);
            case 1:
                return "";
            case 2:
                return "";
        }

        return "";
    }
}
