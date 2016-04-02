package estimeet.meetup.model;

import android.content.ContentValues;
import android.database.Cursor;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.model.database.SqliteContract;

/**
 * Created by AmyDuan on 2/04/16.
 */
public class FriendSession {
    private int sessionId;
    private long sessionLId;
    private int friendId;
    private long dateCreated;
    private int timeToExpire;
    private String distance;
    private String eta;
    private String location;
    private int type;

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public long getSessionLId() {
        return sessionLId;
    }

    public void setSessionLId(long sessionLId) {
        this.sessionLId = sessionLId;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getTimeToExpire() {
        return timeToExpire;
    }

    public void setTimeToExpire(int timeToExpire) {
        this.timeToExpire = timeToExpire;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues(9);
        contentValues.put(SqliteContract.SessionColumns.SESSION_ID, getSessionId());
        contentValues.put(SqliteContract.SessionColumns.SESSION_LID, getSessionLId());
        contentValues.put(SqliteContract.SessionColumns.FRIEND_ID, getFriendId());
        contentValues.put(SqliteContract.SessionColumns.DATE_CREATED, getDateCreated());
        contentValues.put(SqliteContract.SessionColumns.EXPIRE_MINUTES, getTimeToExpire());
        contentValues.put(SqliteContract.SessionColumns.SESSION_DISTANCE, getDistance());
        contentValues.put(SqliteContract.SessionColumns.SESSION_ETA, getEta());
        contentValues.put(SqliteContract.SessionColumns.SESSION_LOCATION, getLocation());
        contentValues.put(SqliteContract.SessionColumns.SESSION_TYPE, getType());
        return contentValues;
    }

    public static FriendSession fromCursor(Cursor cursor) {
        FriendSession friendSession = new FriendSession();
        friendSession.setSessionId(cursor.getInt(DataHelper.SessionQuery.SESSION_ID));
        friendSession.setSessionLId(cursor.getLong(DataHelper.SessionQuery.SESSION_LID));
        friendSession.setFriendId(cursor.getInt(DataHelper.SessionQuery.FRIEND_ID));
        friendSession.setDateCreated(cursor.getLong(DataHelper.SessionQuery.DATE_CREATED));
        friendSession.setTimeToExpire(cursor.getInt(DataHelper.SessionQuery.EXPIRE_MINUTES));
        friendSession.setDistance(cursor.getString(DataHelper.SessionQuery.SESSION_DISTANCE));
        friendSession.setEta(cursor.getString(DataHelper.SessionQuery.SESSION_ETA));
        friendSession.setLocation(cursor.getString(DataHelper.SessionQuery.SESSION_LOCATION));
        friendSession.setType(cursor.getInt(DataHelper.SessionQuery.SESSION_TYPE));
        return friendSession;
    }
}