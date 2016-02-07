package estimeet.meetup.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.model.database.SqliteContract;

/**
 * Created by AmyDuan on 25/01/16.
 */
public class User {

    @SerializedName("FirstName")
    public String firstName;

    @SerializedName("LastName")
    public String lastName;

    @SerializedName("UserID")
    public int userID;

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues(3);
        values.put(SqliteContract.UserColumns.ID, userID);
        values.put(SqliteContract.UserColumns.FIRST_NAME, firstName);
        values.put(SqliteContract.UserColumns.LAST_NAME, lastName);

        return values;
    }

    public static User fromCursor(Cursor cursor) {
        User user = new User();
        user.userID = cursor.getInt(DataHelper.UserQuery.USER_ID);
        user.firstName = cursor.getString(DataHelper.UserQuery.FIRST_NAME);
        user.lastName = cursor.getString(DataHelper.UserQuery.LAST_NAME);

        return user;
    }
}
