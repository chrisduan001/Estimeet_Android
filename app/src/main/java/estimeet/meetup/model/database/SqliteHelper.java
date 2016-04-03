package estimeet.meetup.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import static estimeet.meetup.model.database.SqliteContract.UserColumns;
import static estimeet.meetup.model.database.SqliteContract.FriendColumns;
import static estimeet.meetup.model.database.SqliteContract.SessionColumns;

/**
 * Created by AmyDuan on 27/01/16.
 */
public class SqliteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "meetup.db";
    private static final int DATABASE_VERSION = 108;

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
            + FriendColumns.IMAGE_URI + " TEXT NOT NULL,"
            + FriendColumns.IMAGE + " BLOB,"
            + FriendColumns.FAVOURITE + " INTEGER NOT NULL,"
            + "UNIQUE (" + FriendColumns.ID + "))";

    private static final String SESSION_TABLE = "CREATE TABLE " + SqliteContract.Tables.SESSIONS + "("
            + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + SessionColumns.FRIEND_ID + " INTEGER NOT NULL,"
            + SessionColumns.SESSION_ID + " INTEGER,"
            + SessionColumns.SESSION_LID + " INTEGER,"
            + SessionColumns.DATE_CREATED + " INTEGER NOT NULL,"
            + SessionColumns.EXPIRE_MINUTES + " INTEGER NOT NULL,"
            + SessionColumns.SESSION_DISTANCE + " TEXT,"
            + SessionColumns.SESSION_ETA + " TEXT,"
            + SessionColumns.SESSION_LOCATION + " TEXT,"
            + SessionColumns.SESSION_TYPE + " INTEGER NOT NULL,"
            + "UNIQUE (" + SessionColumns.FRIEND_ID + "))";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_TABLE);
        db.execSQL(FRIENDS_TABLE);
        db.execSQL(SESSION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(this.getClass().getSimpleName(), "Upgrading database from version " + oldVersion + " to "
                + newVersion);

        db.execSQL("DROP TABLE IF EXISTS " + SqliteContract.Tables.USERS);
        db.execSQL("DROP TABLE IF EXISTS " + SqliteContract.Tables.FRIENDS);
        db.execSQL("DROP TABLE IF EXISTS " + SqliteContract.Tables.SESSIONS);
        onCreate(db);
    }
}
