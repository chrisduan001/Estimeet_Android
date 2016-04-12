package estimeet.meetup.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.model.database.SqliteContract;

/**
 * Created by AmyDuan on 25/01/16.
 */
public class User extends BaseModel {
    @SerializedName("id")
    public int id;

    @SerializedName("userId")
    public long userId;

    @SerializedName("userName")
    public String userName;

    @SerializedName("dpUri")
    public String dpUri;

    @SerializedName("phoneNumber")
    public String phoneNumber;

    @SerializedName("password")
    public String password;

    @SerializedName("token")
    public String token;

    @SerializedName("expires_in")
    public long expiresTime;

    public int travelMode;

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues(3);
        values.put(SqliteContract.UserColumns.ID, userId);
//        values.put(SqliteContract.UserColumns.FIRST_NAME, firstName);
//        values.put(SqliteContract.UserColumns.LAST_NAME, lastName);

        return values;
    }

    public boolean isProfileCompleted() {
        return !TextUtils.isEmpty(userName);
    }
}
