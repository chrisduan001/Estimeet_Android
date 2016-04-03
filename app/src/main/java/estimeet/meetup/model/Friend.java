package estimeet.meetup.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.model.database.SqliteContract;

/**
 * Created by AmyDuan on 12/03/16.
 */
public class Friend extends BaseModel {
    @SerializedName("id")
    public int id;

    @SerializedName("userId")
    public long userId;

    @SerializedName("userName")
    public String userName;

    @SerializedName("dpUri")
    public String dpUri;

    public byte[] image;

    public boolean isFavourite;

    public static boolean isSession;

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues(4);
        contentValues.put(SqliteContract.FriendColumns.ID, id);
        contentValues.put(SqliteContract.FriendColumns.USER_ID, userId);
        contentValues.put(SqliteContract.FriendColumns.USER_NAME, userName);
        contentValues.put(SqliteContract.FriendColumns.IMAGE_URI, dpUri);
        contentValues.put(SqliteContract.FriendColumns.IMAGE, image);
        contentValues.put(SqliteContract.FriendColumns.FAVOURITE, isFavourite ? 1 : 0);
        return contentValues;
    }

    public static Friend fromCursor(Cursor cursor) {
        Friend friend = new Friend();
        friend.id = cursor.getInt(DataHelper.FriendQuery.ID);
        friend.userId = cursor.getLong(DataHelper.FriendQuery.USER_ID);
        friend.userName = cursor.getString(DataHelper.FriendQuery.USER_NAME);
        friend.dpUri = cursor.getString(DataHelper.FriendQuery.IMAGE_URI);
        friend.image = cursor.getBlob(DataHelper.FriendQuery.IMAGE);
        friend.isFavourite = cursor.getInt(DataHelper.FriendQuery.FAVOURITE) != 0;
        return friend;
    }
}
