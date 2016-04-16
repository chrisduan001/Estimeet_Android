package estimeet.meetup.factory;

import java.util.List;

import estimeet.meetup.model.FriendSession;
import estimeet.meetup.model.database.DataHelper;

/**
 * Created by AmyDuan on 16/04/16.
 */
public class SessionActivityFactory {

    public static final int SENT_SESSION = 100;
    public static final int RECEIVED_SESSION = 101;
    public static final int ACTIVE_SESSION = 102;

    //null == no session available, no == no active session, yes == active session
    public static Boolean checkSession(DataHelper dataHelper) {
        List<FriendSession> sessions = dataHelper.getAllActiveSession();

        Boolean isAnyActiveSession = null;
        for (FriendSession session: sessions) {
            if (System.currentTimeMillis() > session.getDateCreated() + session.getTimeToExpireInMilli()) {
                dataHelper.deleteSession(session.getFriendId());
            } else {
                if (session.getType() == ACTIVE_SESSION) {
                    return true;
                } else {
                    isAnyActiveSession = false;
                }
            }
        }

        return isAnyActiveSession;
    }
}
