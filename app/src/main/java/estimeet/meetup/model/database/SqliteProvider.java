package estimeet.meetup.model.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by AmyDuan on 27/01/16.
 */
public class SqliteProvider extends ContentProvider {

    private static final UriMatcher URI_MATCHER = buildUriMatcher();

    private static final int USERS = 100;
    private static final int USER_ID = 101;

    private SqliteHelper sqliteHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = SqliteContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, "Users", USERS);
        matcher.addURI(authority, "Users/*", USER_ID);

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

        Cursor cursor = builder.where(selection, selectionArgs)
                .query(db, true, projection, sortOrder, null);

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
            default: {
                throw new UnsupportedOperationException("Unknown inser uri: " + uri);
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

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        final SelectionBuilder builder = buildSelection(uri);

        int retVal = builder.where(selection, selectionArgs).delete(db);
        getContext().getContentResolver().notifyChange(uri, null);
        return retVal;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        final SelectionBuilder builder = buildSelection(uri);

        int retVal = builder.where(selection, selectionArgs).update(db, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return retVal;
    }
}