package estimeet.meetup.model;

import android.content.SharedPreferences;
import android.text.TextUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by AmyDuan on 18/02/16.
 */
@Singleton
public class MeetUpSharedPreference {
    private static final String ID = "USER_ID";
    private static final String NAME = "USER_NAME";
    private static final String DP = "USER_DP";
    private static final String PHONE = "PHONE_NUMBER";
    private static final String PASSWORD = "PASSWORD";
    private static final String TOKEN = "AUTH_TOKEN";

    private final SharedPreferences sharedPreferences;

    @Inject
    public MeetUpSharedPreference(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public User getUserFromSp() {
        User user = new User();
        user.userId = sharedPreferences.getLong(ID, 0);
        if (user.userId == 0) {
            return null;
        }
        user.userName = sharedPreferences.getString(NAME, "");
        user.dpUri = sharedPreferences.getString(DP, "");
        user.phoneNumber = sharedPreferences.getString(PHONE, "");
        user.password = sharedPreferences.getString(PASSWORD, "");
        user.token = sharedPreferences.getString(TOKEN, "");

        return user;
    }

    public void storeUser(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(ID, user.userId);
        editor.putString(NAME, TextUtils.isEmpty(NAME) ? "" : user.userName);
        editor.putString(DP, TextUtils.isEmpty(DP) ? "" : user.dpUri);
        editor.putString(PHONE, user.phoneNumber);
        editor.putString(PASSWORD, user.password);
        editor.putString(TOKEN, user.token);
        editor.apply();
    }

    public void removeSharedPreference() {
        sharedPreferences.edit().clear().apply();
    }
}
