package estimeet.meetup.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import static estimeet.meetup.model.database.SqliteContract.UserColumns;

/**
 * Created by AmyDuan on 27/01/16.
 */
public class SqliteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "meetup.db";
    private static final int DATABASE_VERSION = 100;

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String USER_TABLE = "CREATE TABLE " + SqliteContract.Tables.USERS + "("
            + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + UserColumns.ID + " INTEGER NOT NULL,"
            + UserColumns.FIRST_NAME + " TEXT NOT NULL,"
            + UserColumns.LAST_NAME + " TEXT NOT NULL,"
            + "UNIQUE (" + UserColumns.ID + "))";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(this.getClass().getSimpleName(), "Upgrading database from version " + oldVersion + " to "
        + newVersion);

        db.execSQL("DROP TABLE IF EXISTS " + SqliteContract.Tables.USERS);

        onCreate(db);
    }
}
