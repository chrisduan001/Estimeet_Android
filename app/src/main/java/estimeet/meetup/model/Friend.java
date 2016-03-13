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
    private int id;

    @SerializedName("userId")
    private long userId;

    @SerializedName("userName")
    private String userName;

    @SerializedName("dpUri")
    private String dpUri;

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues(4);
        contentValues.put(SqliteContract.FriendColumns.ID, id);
        contentValues.put(SqliteContract.FriendColumns.USER_ID, userId);
        contentValues.put(SqliteContract.FriendColumns.USER_NAME, userName);
        contentValues.put(SqliteContract.FriendColumns.IMAGE_URI, dpUri);
        return contentValues;
    }

    public ContentValues toImageContentValue(byte[] imageByte) {
        ContentValues cv = new ContentValues(2);
        cv.put(SqliteContract.DpImageColumns.ID, id);
        cv.put(SqliteContract.DpImageColumns.USER_IMAGE, imageByte);
        return cv;
    }

    public static Friend fromCursor(Cursor cursor) {
        Friend friend = new Friend();
        friend.id = cursor.getInt(DataHelper.FriendQuery.ID);
        friend.userId = cursor.getLong(DataHelper.FriendQuery.USER_ID);
        friend.userName = cursor.getString(DataHelper.FriendQuery.USER_NAME);

        return friend;
    }
}
