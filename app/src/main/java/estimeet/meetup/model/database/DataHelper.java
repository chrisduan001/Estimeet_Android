package estimeet.meetup.model.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import estimeet.meetup.model.Friend;
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

    public void deleteUserData(int userId) {
        contentResolver.delete(SqliteContract.Users.CONTENT_URI, null, null);
    }

    /**
     * single user
     * @param user
     */
    public void insertUserData(User user) {
        contentResolver.insert(SqliteContract.Users.CONTENT_URI, user.toContentValues());
    }
    /**
     * multiple users
     */

    public void insertUsersData(List<User> users) {
        ContentValues[] userValues = new ContentValues[users.size()];
        for (int i=0; i < users.size(); i++) {
            userValues[i] = users.get(i).toContentValues();
        }

        contentResolver.bulkInsert(SqliteContract.Users.CONTENT_URI, userValues);
    }

    public void insertFriendsData(List<Friend> friends) {
        ContentValues[] userValues = new ContentValues[friends.size()];
        for (int i=0; i < friends.size(); i++) {
            userValues[i] = friends.get(i).toContentValues();
        }

        contentResolver.bulkInsert(SqliteContract.Friends.CONTENT_URI, userValues);
    }

    public void insertFriend(Friend friend) {
        if (getFriend(friend.id) == null) {
            contentResolver.insert(SqliteContract.Friends.CONTENT_URI, friend.toContentValues());
        }
    }

    public void updateFriendData(Friend friend) {
        ContentValues value = friend.toContentValues();
        contentResolver.update(SqliteContract.Friends.buildFriendUri(friend.id), value, null, null);
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Cursor cursor = contentResolver.query(SqliteContract.Users.CONTENT_URI, UserQuery.PROJECTION,
                null, null, null);

        if (cursor == null) throw new RuntimeException("Cursor can't be null");
        while (cursor.moveToNext()) {
            User user = User.fromCursor(cursor);
            users.add(user);
        }

        cursor.close();

        return users;
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

    public Friend getFriend(int id) {
        Cursor cursor = contentResolver.query(SqliteContract.Friends.CONTENT_URI, FriendQuery.PROJECTION,
                SqliteContract.FriendColumns.ID + " = " + id, null, null);
        if (cursor == null) throw new RuntimeException("Cursor can't be null");
        return cursor.moveToFirst() ? Friend.fromCursor(cursor) : null;
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
}
