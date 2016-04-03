package estimeet.meetup.model.database;

import android.net.Uri;

/**
 * Created by AmyDuan on 27/01/16.
 */
public class SqliteContract {
    public static final String CONTENT_AUTHORITY = "com.estimeet.meetup";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public interface Tables {
        String USERS = "Users";
        String FRIENDS = "Friends";
        String SESSIONS = "Sessions";
    }

    public interface UserColumns {
        String ID = "ID";
        String FIRST_NAME = "FirstName";
        String LAST_NAME = "LastName";
    }

    public interface FriendColumns {
        String ID = "ID";
        String USER_ID = "UserID";
        String USER_NAME = "UserName";
        String IMAGE_URI = "ImageUri";
        String IMAGE = "Image";
        String FAVOURITE = "Favourite";
    }

    public interface SessionColumns {
        String SESSION_ID = "SessionID";
        String FRIEND_ID = "FriendId";
        String SESSION_LID = "SessionLId";
        String DATE_CREATED = "DateCreated";
        String EXPIRE_MINUTES = "TimeToExpire";
        String SESSION_REQUESTED_TIME = "RequestedTime";
        String SESSION_DISTANCE = "Distance";
        String SESSION_ETA = "ETA";
        String SESSION_LOCATION = "Location";
        String SESSION_TYPE = "Type";
    }

    public static class Users implements UserColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(Tables.USERS).build();

        public static final String DEFAULT_SORT = UserColumns.ID + " DESC LIMIT 1";

        public static Uri buildUserUri(int userId) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(userId)).build();
        }

        public static String getUserId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class Friends implements FriendColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(Tables.FRIENDS).build();

        public static final String DEFAULT_SORT = FriendColumns.USER_NAME;

        public static Uri buildFriendUri(int id) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
        }

        public static String getFriendId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class Sessions implements SessionColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(Tables.SESSIONS).build();

        public static final String DEFAULT_SORT = SessionColumns.DATE_CREATED + " DESC";

        public static Uri buildSessionUri(int id) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
        }

        public static String getFriendId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}
