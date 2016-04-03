package estimeet.meetup.model.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by AmyDuan on 27/01/16.
 */
@SuppressWarnings("NullableProblems")
public class SqliteProvider extends ContentProvider {

    private static final UriMatcher URI_MATCHER = buildUriMatcher();

    private static final int USERS = 100;
    private static final int USER_ID = 101;

    private static final int FRIENDS = 200;
    private static final int FRIENDS_ID = 201;

    private static final int SESSIONS = 300;
    private static final int SESSIONS_FRIEND_ID = 301;

    private SqliteHelper sqliteHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = SqliteContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, "Users", USERS);
        matcher.addURI(authority, "Users/*", USER_ID);

        matcher.addURI(authority, "Friends", FRIENDS);
        matcher.addURI(authority, "Friends/*", FRIENDS_ID);

        matcher.addURI(authority, "Sessions", SESSIONS);
        matcher.addURI(authority, "Sessions/*", SESSIONS_FRIEND_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        sqliteHelper = new SqliteHelper(getContext());
        return true;
    }

    private SelectionBuilder buildSelection(Uri uri) {
        final SelectionBuilder builder = new SelectionBuilder();
        final int match = URI_MATCHER.match(uri);
        switch (match) {
            case USERS: {
                return builder.table(SqliteContract.Tables.USERS);
            }
            case USER_ID: {
                final String userId = SqliteContract.Users.getUserId(uri);
                return builder.table(SqliteContract.Tables.USERS)
                        .where(SqliteContract.UserColumns.ID + "=?", userId);
            }

            case FRIENDS: {
                return builder.table(SqliteContract.Tables.FRIENDS);
            }
            case FRIENDS_ID: {
                final String id = SqliteContract.Friends.getFriendId(uri);
                return builder.table(SqliteContract.Tables.FRIENDS)
                        .where(SqliteContract.UserColumns.ID + "=?", id);
            }

            case SESSIONS: {
                return builder.table(SqliteContract.Tables.FRIENDS + " F LEFT JOIN "
                + SqliteContract.Tables.SESSIONS + " S ON F." + SqliteContract.FriendColumns.ID
                + " = S." + SqliteContract.SessionColumns.FRIEND_ID);
            }
            case SESSIONS_FRIEND_ID: {
                final String id = SqliteContract.Sessions.getFriendId(uri);
                return builder.table(SqliteContract.Tables.SESSIONS)
                        .where(SqliteContract.SessionColumns.FRIEND_ID + "=?", id);
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = sqliteHelper.getReadableDatabase();

        Context context = getContext();

        final SelectionBuilder builder = buildSelection(uri);
        Log.d("Sql command", "query: " + builder.toString());
        Cursor cursor = builder.where(selection, selectionArgs)
                .query(db, false, projection, sortOrder, null);

        if (context != null) {
            cursor.setNotificationUri(context.getContentResolver(), uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @SuppressWarnings("ConstantConditions")
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);

        switch (match) {
            case USERS: {
                db.replaceOrThrow(SqliteContract.Tables.USERS, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return SqliteContract.Users.buildUserUri(values.getAsInteger(SqliteContract.UserColumns.ID));
            }

            case FRIENDS: {
                db.replaceOrThrow(SqliteContract.Tables.FRIENDS, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return SqliteContract.Friends.buildFriendUri(values.getAsInteger(SqliteContract.FriendColumns.ID));
            }

            case SESSIONS: {
                db.replaceOrThrow(SqliteContract.Tables.SESSIONS, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return SqliteContract.Sessions.buildSessionUri(values.getAsInteger(SqliteContract.SessionColumns.FRIEND_ID));
            }

            default: {
                throw new UnsupportedOperationException("Unknown insert uri: " + uri);
            }
        }
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);

        String table;

        switch (match) {
            case USERS: {
                table = SqliteContract.Tables.USERS;
                break;
            }

            case FRIENDS: {
                table = SqliteContract.Tables.FRIENDS;
                break;
            }

            case SESSIONS: {
                table = SqliteContract.Tables.SESSIONS;
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown bulkInsert uri: " + uri);
        }

        try {
            db.beginTransaction();
            for (ContentValues contentValues: values) {
                db.replaceOrThrow(table, null, contentValues);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return values.length;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        final SelectionBuilder builder = buildSelection(uri);

        int retVal = builder.where(selection, selectionArgs).delete(db);
        getContext().getContentResolver().notifyChange(uri, null);
        return retVal;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        final SelectionBuilder builder = buildSelection(uri);

        int retVal = builder.where(selection, selectionArgs).update(db, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return retVal;
    }
}
