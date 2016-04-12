package estimeet.meetup.model;

import android.content.SharedPreferences;
import android.text.TextUtils;

import java.security.PublicKey;
import java.util.Calendar;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by AmyDuan on 18/02/16.
 */
@Singleton
public class MeetUpSharedPreference {
    private static final String ID              = "ID";
    private static final String USER_ID         = "USER_ID";
    private static final String NAME            = "USER_NAME";
    private static final String DP              = "USER_DP";
    private static final String PHONE           = "PHONE_NUMBER";
    private static final String PASSWORD        = "PASSWORD";
    private static final String TOKEN           = "AUTH_TOKEN";
    private static final String EXPIRES         = "TOKEN_EXPIRE_TIME";
    private static final String TRAVEL_MODE     = "TRAVEL_MODE";
    private static final String USER_GEO        = "USER_GEO";

    private static final String VERSION_CODE    = "VERSION_CODE";
    private static final String GCM_REG_ID      = "GCM_REG_ID";
    private static final String NOTIFICATIONID  = "NOTIFICATION_ID";

    private final SharedPreferences sharedPreferences;

    @Inject
    public MeetUpSharedPreference(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public User getUserFromSp() {
        User user = new User();

        user.userId = sharedPreferences.getLong(USER_ID, 0);
        user.id = sharedPreferences.getInt(ID, 0);
        user.userName = sharedPreferences.getString(NAME, "");
        user.dpUri = sharedPreferences.getString(DP, "");
        user.phoneNumber = sharedPreferences.getString(PHONE, "");
        user.password = sharedPreferences.getString(PASSWORD, "");
        user.token = sharedPreferences.getString(TOKEN, "");
        user.expiresTime = sharedPreferences.getLong(EXPIRES, 0);
        user.travelMode = sharedPreferences.getInt(TRAVEL_MODE, 0);

        return user;
    }

    public void storeUserInfo(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(ID, user.id);
        editor.putLong(USER_ID, user.userId);
        editor.putString(PHONE, user.phoneNumber);
        editor.putString(PASSWORD, user.password);
        editor.apply();
    }

    public void updateUserProfile(String name, String imageUri) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NAME, name);
        editor.putString(DP, imageUri);

        editor.apply();
    }

    public void updateUserToken(String token, long expireInSeconds) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN, token);
        long time = Calendar.getInstance().getTimeInMillis()/1000 + expireInSeconds;
        //shoudl renew at least 10 min before expires
        editor.putLong(EXPIRES, time - 600);
        editor.apply();
    }

    //if the version code changes, need to re-register the push channel
    public int getVersionCode() {
        return sharedPreferences.getInt(VERSION_CODE, 0);
    }

    public void setVersionCode(int code) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(VERSION_CODE, code);

        editor.apply();
    }

    //push notification
    public String getGcmRegId() {
        return sharedPreferences.getString(GCM_REG_ID, "");
    }

    public void setGcmRegId(String regId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GCM_REG_ID, regId);

        editor.apply();
    }

    //for retrieve or delete notifications from server
    public long getNotificationid() {
        return sharedPreferences.getLong(NOTIFICATIONID, 0);
    }

    public void setNotificationid(long id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(NOTIFICATIONID, id);

        editor.apply();
    }

    public void saveTravelInfo(int travelMode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(TRAVEL_MODE, travelMode);
        editor.apply();
    }

    public void saveUserGeo(String userGeo) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_GEO, userGeo);
        editor.apply();
    }

    public int getTravelMode() {
        return sharedPreferences.getInt(TRAVEL_MODE, 0);
    }

    public String getUserGeoCoord() {
        return sharedPreferences.getString(USER_GEO, "");
    }

    public void removeSharedPreference() {
        sharedPreferences.edit().clear().apply();
    }
}
