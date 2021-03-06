package estimeet.meetup.model;

import android.content.ContentValues;
import android.database.Cursor;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.model.database.SqliteContract;
import estimeet.meetup.ui.adapter.FriendListAdapter;

/**
 * Created by AmyDuan on 2/04/16.
 */
public class FriendSession {
    private int sessionId;
    private long sessionLId;
    private int sessionFriendId;
    private long dateCreated;
    private long dateUpdated;
    private long timeToExpireInMilli;
    private int distance;
    private int eta;
    private String geoCoordinate;
    private String location;
    private int type;
    private String friendName;
    private byte[] friendDp;
    private int friendId;
    private int requestedLength;
    private int travelMode;
    private long waitingTime;

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

    public int getSessionFriendId() {
        return sessionFriendId;
    }

    public void setSessionFriendId(int sessionFriendId) {
        this.sessionFriendId = sessionFriendId;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getTimeToExpireInMilli() {
        return timeToExpireInMilli;
    }

    public void setTimeToExpireInMilli(long timeToExpireInMilli) {
        this.timeToExpireInMilli = timeToExpireInMilli;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getEta() {
        return eta;
    }

    public void setEta(int eta) {
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

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public byte[] getFriendDp() {
        return friendDp;
    }

    public void setFriendDp(byte[] friendDp) {
        this.friendDp = friendDp;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    public int getRequestedLength() {
        return requestedLength;
    }

    public void setRequestedLength(int requestedLength) {
        this.requestedLength = requestedLength;
    }

    public int getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(int travelMode) {
        this.travelMode = travelMode;
    }

    public long getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(long dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public long getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(long waitingTime) {
        this.waitingTime = waitingTime;
    }

    public String getGeoCoordinate() {
        return geoCoordinate;
    }

    public void setGeoCoordinate(String geoCoordinate) {
        this.geoCoordinate = geoCoordinate;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues(11);
        contentValues.put(SqliteContract.SessionColumns.SESSION_ID, getSessionId());
        contentValues.put(SqliteContract.SessionColumns.SESSION_LID, getSessionLId());
        contentValues.put(SqliteContract.SessionColumns.FRIEND_ID, getFriendId());
        contentValues.put(SqliteContract.SessionColumns.DATE_CREATED, getDateCreated());
        contentValues.put(SqliteContract.SessionColumns.EXPIRE_MINUTES, getTimeToExpireInMilli());
        contentValues.put(SqliteContract.SessionColumns.SESSION_DISTANCE, getDistance());
        contentValues.put(SqliteContract.SessionColumns.SESSION_ETA, getEta());
        contentValues.put(SqliteContract.SessionColumns.SESSION_LOCATION, getLocation());
        contentValues.put(SqliteContract.SessionColumns.GEO_COORDINATE, getGeoCoordinate());
        contentValues.put(SqliteContract.SessionColumns.SESSION_TYPE, getType());
        contentValues.put(SqliteContract.SessionColumns.SESSION_REQUESTED_TIME, getRequestedLength());
        contentValues.put(SqliteContract.SessionColumns.TRAVEL_MODE, getTravelMode());
        contentValues.put(SqliteContract.SessionColumns.DATE_UPDATED, getDateUpdated());
        contentValues.put(SqliteContract.SessionColumns.WAITING_TIME, getWaitingTime() <= 0 ? null : getWaitingTime());
        return contentValues;
    }

    public static FriendSession fromCursor(Cursor cursor) {
        FriendSession friendSession = new FriendSession();
        friendSession.setSessionId(cursor.getInt(DataHelper.FriendSessionQuery.SESSION_ID));
        friendSession.setSessionLId(cursor.getLong(DataHelper.FriendSessionQuery.SESSION_LID));
        //this value is 0 when user start a friend request (friend id in session table)
        friendSession.setSessionFriendId(cursor.getInt(DataHelper.FriendSessionQuery.SESSION_FRIEND_ID));
        friendSession.setDateCreated(cursor.getLong(DataHelper.FriendSessionQuery.DATE_CREATED));
        friendSession.setTimeToExpireInMilli(cursor.getLong(DataHelper.FriendSessionQuery.EXPIRE_MINUTES));
        friendSession.setDistance(cursor.getInt(DataHelper.FriendSessionQuery.SESSION_DISTANCE));
        friendSession.setEta(cursor.getInt(DataHelper.FriendSessionQuery.SESSION_ETA));
        friendSession.setGeoCoordinate(cursor.getString(DataHelper.FriendSessionQuery.GEO_COORDINATE));
        friendSession.setLocation(cursor.getString(DataHelper.FriendSessionQuery.SESSION_LOCATION));
        friendSession.setType(cursor.getInt(DataHelper.FriendSessionQuery.SESSION_TYPE));
        friendSession.setFriendName(cursor.getString(DataHelper.FriendSessionQuery.FRIEND_NAME));
        friendSession.setFriendDp(cursor.getBlob(DataHelper.FriendSessionQuery.FRIEND_IMAGE));
        friendSession.setFriendId(cursor.getInt(DataHelper.FriendSessionQuery.FRIEND_ID));
        friendSession.setRequestedLength(cursor.getInt(DataHelper.FriendSessionQuery.SESSION_REQUESTED_TIME));
        friendSession.setTravelMode(cursor.getInt(DataHelper.FriendSessionQuery.SESSION_TRAVEL_MODE));
        friendSession.setDateUpdated(cursor.getLong(DataHelper.FriendSessionQuery.DATE_UPDATED));
        friendSession.setWaitingTime(cursor.getLong(DataHelper.FriendSessionQuery.WAITING_TIME));
        return friendSession;
    }

    public static int getSection(Cursor cursor) {
        if (cursor.getInt(DataHelper.FriendSessionQuery.SESSION_TYPE) == 0) {
            return FriendListAdapter.FRIEND_SECTION;
        }

        return FriendListAdapter.SESSION_SECTION;
    }
}