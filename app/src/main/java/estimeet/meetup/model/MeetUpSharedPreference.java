package estimeet.meetup.model;

import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.Calendar;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by AmyDuan on 18/02/16.
 */
@Singleton
public class MeetUpSharedPreference {
    private static final String ID = "ID";
    private static final String USER_ID = "USER_ID";
    private static final String NAME = "USER_NAME";
    private static final String DP = "USER_DP";
    private static final String PHONE = "PHONE_NUMBER";
    private static final String PASSWORD = "PASSWORD";
    private static final String TOKEN = "AUTH_TOKEN";
    private static final String EXPIRES = "TOKEN_EXPIRE_TIME";

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

    public void removeSharedPreference() {
        sharedPreferences.edit().clear().apply();
    }
}
