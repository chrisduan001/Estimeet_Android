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
}
