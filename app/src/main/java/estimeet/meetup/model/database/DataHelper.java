package estimeet.meetup.model.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.support.v4.app.NavUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import estimeet.meetup.model.Friend;
import estimeet.meetup.model.FriendSession;
import estimeet.meetup.model.User;

/**
 * Created by AmyDuan on 28/01/16.
 */

@Singleton
public class DataHelper {
    private final ContentResolver contentResolver;

    public DataHelper(ContentResolver resolver) {
        this.contentResolver = resolver;
    }

    public int getUserId() {
        Cursor cursor = contentResolver.query(SqliteContract.Users.CONTENT_URI,
                new String[] {SqliteContract.UserColumns.ID}, null, null, SqliteContract.Users.DEFAULT_SORT);

        int userId = -1;
        while (cursor.moveToNext()) {
            userId = cursor.getInt(0);
        }

        cursor.close();

        return userId;
    }

    public void insertFriend(Friend friend) {
        if (getFriend(friend.id) == null) {
            contentResolver.insert(SqliteContract.Friends.CONTENT_URI, friend.toContentValues());
        }
    }

    public void insertFriendsData(List<Friend> friends) {
        ContentValues[] userValues = new ContentValues[friends.size()];
        for (int i=0; i < friends.size(); i++) {
            userValues[i] = friends.get(i).toContentValues();
        }

        contentResolver.bulkInsert(SqliteContract.Friends.CONTENT_URI, userValues);
    }

    public void updateFriendData(Friend friend) {
        ContentValues value = friend.toContentValues();
        contentResolver.update(SqliteContract.Friends.buildFriendUri(friend.id), value, null, null);
    }

    public Friend getFriend(int id) {
        Cursor cursor = contentResolver.query(SqliteContract.Friends.buildFriendUri(id), FriendQuery.PROJECTION,
                null, null, null);
        if (cursor == null) throw new RuntimeException("Cursor can't be null");

        Friend friend = cursor.moveToFirst() ? Friend.fromCursor(cursor) : null;

        cursor.close();
        return friend;
    }

    public List<Friend> getAllFriends() {
        List<Friend> friends = new ArrayList<>();
        Cursor cursor = contentResolver.query(SqliteContract.Friends.CONTENT_URI, FriendQuery.PROJECTION,
                null, null, null);
        if (cursor == null) throw new RuntimeException("Cursor can't be null");
        while (cursor.moveToNext()) {
            Friend friend = Friend.fromCursor(cursor);
            friends.add(friend);
        }

        cursor.close();
        return friends;
    }

    public void insertSession(FriendSession session) {
        if (getSession(session.getSessionFriendId()) != null) {
            updateSession(session);
            return;
        }
        contentResolver.insert(SqliteContract.Sessions.CONTENT_URI, session.toContentValues());
    }

    public void updateSession(FriendSession friendSession) {
        ContentValues contentValues = friendSession.toContentValues();
        contentResolver.update(SqliteContract.Sessions.buildSessionUri(friendSession.getSessionFriendId()), contentValues, null, null);
    }

    public FriendSession getSession(int id) {
        Cursor cursor = contentResolver.query(SqliteContract.Sessions.CONTENT_URI,
                FriendSessionQuery.PROJECTION, SqliteContract.Sessions.FRIEND_ID + "=?",
                new String[]{id + ""}, null);
        if (cursor == null) throw new RuntimeException("Cursor can't be null");

        FriendSession friendSession = cursor.moveToFirst() ? FriendSession.fromCursor(cursor) : null;
        cursor.close();

        return friendSession;
    }

    public List<FriendSession> getAllActiveSession() {
        List<FriendSession> sessions = new ArrayList<>();
        Cursor cursor = contentResolver.query(SqliteContract.Sessions.CONTENT_URI,
                FriendSessionQuery.PROJECTION, SqliteContract.Sessions.FRIEND_ID + "!=?",
                new String[]{"0"}, null);

        if (cursor == null) throw new RuntimeException("Cursor can't be null");
        while (cursor.moveToNext()) {
            sessions.add(FriendSession.fromCursor(cursor));
        }

        cursor.close();
        return sessions;
    }

    public void deleteSession(int friendId) {
        contentResolver.delete(SqliteContract.Sessions.buildSessionUri(friendId), null, null);
    }

    public void updateUser(User user, int userId) {
        ContentValues values = user.toContentValues();
        contentResolver.update(SqliteContract.Users.buildUserUri(userId), values, null, null);

    }

    public void deleteUser(int userId) {
        contentResolver.delete(SqliteContract.Users.CONTENT_URI, SqliteContract.UserColumns.ID + " = " + userId, null);
    }

    public void deleteAllUser() {
        contentResolver.delete(SqliteContract.Users.CONTENT_URI, null, null);
        contentResolver.notifyChange(SqliteContract.Users.CONTENT_URI, null);
    }

    public interface UserQuery {
        String[] PROJECTION = {
                BaseColumns._ID,
                SqliteContract.UserColumns.ID,
                SqliteContract.UserColumns.FIRST_NAME,
                SqliteContract.UserColumns.LAST_NAME
        };

        int ID = 0;
        int USER_ID = 1;
        int FIRST_NAME = 2;
        int LAST_NAME = 3;
    }

    public interface FriendQuery {
        String[] PROJECTION = {
                BaseColumns._ID,
                SqliteContract.FriendColumns.ID,
                SqliteContract.FriendColumns.USER_ID,
                SqliteContract.FriendColumns.USER_NAME,
                SqliteContract.FriendColumns.IMAGE_URI,
                SqliteContract.FriendColumns.IMAGE,
                SqliteContract.FriendColumns.FAVOURITE
        };

        int B_ID = 0;
        int ID = 1;
        int USER_ID = 2;
        int USER_NAME = 3;
        int IMAGE_URI = 4;
        int IMAGE = 5;
        int FAVOURITE = 6;
    }

    public interface FriendSessionQuery {
        String[] PROJECTION = {
                "S." + BaseColumns._ID,
                SqliteContract.SessionColumns.SESSION_ID,
                SqliteContract.SessionColumns.SESSION_LID,
                SqliteContract.SessionColumns.FRIEND_ID,
                SqliteContract.SessionColumns.DATE_CREATED,
                SqliteContract.SessionColumns.EXPIRE_MINUTES,
                SqliteContract.SessionColumns.SESSION_DISTANCE,
                SqliteContract.SessionColumns.SESSION_ETA,
                SqliteContract.SessionColumns.SESSION_LOCATION,
                SqliteContract.SessionColumns.SESSION_TYPE,
                SqliteContract.SessionColumns.SESSION_REQUESTED_TIME,
                SqliteContract.FriendColumns.IMAGE,
                SqliteContract.FriendColumns.USER_NAME,
                SqliteContract.FriendColumns.ID,
        };

        int B_ID = 0;
        int SESSION_ID = 1;
        int SESSION_LID = 2;
        int SESSION_FRIEND_ID = 3;
        int DATE_CREATED = 4;
        int EXPIRE_MINUTES = 5;
        int SESSION_DISTANCE = 6;
        int SESSION_ETA = 7;
        int SESSION_LOCATION = 8;
        int SESSION_TYPE = 9;
        int SESSION_REQUESTED_TIME = 10;
        int FRIEND_IMAGE = 11;
        int FRIEND_NAME = 12;
        int FRIEND_ID = 13;
    }
}
