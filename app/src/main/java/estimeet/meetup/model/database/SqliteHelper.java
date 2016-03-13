package estimeet.meetup.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import static estimeet.meetup.model.database.SqliteContract.UserColumns;
import static estimeet.meetup.model.database.SqliteContract.FriendColumns;
import static estimeet.meetup.model.database.SqliteContract.DpImageColumns;

/**
 * Created by AmyDuan on 27/01/16.
 */
public class SqliteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "meetup.db";
    private static final int DATABASE_VERSION = 101;

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String USER_TABLE = "CREATE TABLE " + SqliteContract.Tables.USERS + "("
            + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + UserColumns.ID + " INTEGER NOT NULL,"
            + UserColumns.FIRST_NAME + " TEXT NOT NULL,"
            + UserColumns.LAST_NAME + " TEXT NOT NULL,"
            + "UNIQUE (" + UserColumns.ID + "))";

    private static final String FRIENDS_TABLE = "CREATE TABLE " + SqliteContract.Tables.FRIENDS + "("
            + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + FriendColumns.ID + " INTEGER NOT NULL,"
            + FriendColumns.USER_ID + " INTEGER NOT NULL,"
            + FriendColumns.USER_NAME + " TEXT NOT NULL,"
            + "UNIQUE (" + FriendColumns.ID + "))";

    private static final String IMAGE_TABLE = "CREATE TABLE " + SqliteContract.Tables.DP_IMAGE + "("
            + DpImageColumns.ID + " INTEGER NOT NULL,"
            + DpImageColumns.USER_IMAGE + " BLOB NOT NULL,"
            + "INIQUE (" + DpImageColumns.ID + "))";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_TABLE);
        db.execSQL(FRIENDS_TABLE);
        db.execSQL(IMAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(this.getClass().getSimpleName(), "Upgrading database from version " + oldVersion + " to "
        + newVersion);

        db.execSQL("DROP TABLE IF EXISTS " + SqliteContract.Tables.USERS);
        db.execSQL("DROP TABLE IF EXISTS " + SqliteContract.Tables.FRIENDS);
        db.execSQL("DROP TABLE IF EXISTS " + SqliteContract.Tables.DP_IMAGE);

        onCreate(db);
    }
}
