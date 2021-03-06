package estimeet.meetup.factory;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import estimeet.meetup.R;
import estimeet.meetup.model.FriendSession;
import estimeet.meetup.model.LocationModel;
import estimeet.meetup.ui.adapter.FriendListAdapter;

/**
 * Created by AmyDuan on 3/04/16.
 */
public class SessionCreationFactory {

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
        session.setType(SessionActivityFactory.SENT_SESSION);
        session.setSessionFriendId(session.getFriendId());
        setupSessionTimeWithDefaultValue(session);

        return session;
    }

    public static FriendSession createActiveSession(int friendId, int sessionId, long sessionLId,
                                                    long expireTimeInMilli, int length) {
        FriendSession session = new FriendSession();
        session.setSessionId(sessionId);
        session.setSessionLId(sessionLId);
        session.setFriendId(friendId);
        session.setSessionFriendId(friendId);
        session.setType(SessionActivityFactory.ACTIVE_SESSION);
        session.setRequestedLength(length);
        setupSessionTime(expireTimeInMilli, session);

        return session;
    }

    public static FriendSession updateDistanceEta(LocationModel locationModel, FriendSession friendSession) {
        friendSession.setDistance(locationModel.distance);
        friendSession.setEta(locationModel.eta);
        friendSession.setTravelMode(locationModel.travelMode);
        friendSession.setLocation(locationModel.location);
        friendSession.setDateUpdated(System.currentTimeMillis());
        friendSession.setGeoCoordinate(locationModel.geoCoordinate);
        friendSession.setWaitingTime(0);

        return friendSession;
    }

    public static FriendSession updateWaitingForLocationUpdateTime(long waitingMillis, FriendSession friendSession) {
        friendSession.setWaitingTime(waitingMillis);

        return friendSession;
    }

    public static FriendSession createPendingSession(int friendId, int requestedTime) {
        FriendSession session = new FriendSession();
        session.setFriendId(friendId);
        session.setSessionFriendId(friendId);
        session.setRequestedLength(requestedTime);
        session.setType(SessionActivityFactory.RECEIVED_SESSION);
        setupSessionTimeWithDefaultValue(session);

        return session;
    }

    public static String getSessionLengthString(int length, Context context) {
        switch (length) {
            case 0:
                return context.getString(R.string.session_length_30);
            case 1:
                return "";
            case 2:
                return "";
        }

        return "";
    }

    public static int getSessionLengthInMinutes(int length) {
        switch (length) {
            case 0:
                return 30;
            case 1:
                return 45;
            case 2:
                return 60;
            default:
                return 30;
        }
    }
}
