package estimeet.meetup.di.Modules;

import android.app.Activity;
import android.content.pm.PackageManager;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.microsoft.windowsazure.messaging.NotificationHub;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import estimeet.meetup.R;
import estimeet.meetup.di.PerActivity;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.PushModel;
import estimeet.meetup.model.User;
import estimeet.meetup.ui.activity.MainActivity;

/**
 * Created by AmyDuan on 23/02/16.
 */
@Module
public class MainModule {
    private final Activity activity;

    public MainModule(MainActivity activity) {
        this.activity = activity;
    }

    @Provides @PerActivity
    Activity activity() {
        return this.activity;
    }

    @Provides @PerActivity @Named("currentUser")
    public User provideUser(MeetUpSharedPreference sharedPreference) {
        return sharedPreference.getUserFromSp();
    }

    @Provides @PerActivity
    public PushModel providePushModel(GoogleCloudMessaging gcm, NotificationHub hub,
                                      @Named("versionCode") int code, @Named("currentUser") User user) {
        return new PushModel(gcm, hub, user.userId + "", activity.getString(R.string.push_SENDER_ID), code);
    }

    @Provides @PerActivity @Named("versionCode")
    int provideVersionCode() {
        int versionCode;
        try {
            versionCode = activity.getPackageManager()
                    .getPackageInfo(activity.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            versionCode = 0;
        }

        return versionCode;
    }

    @Provides @PerActivity
    GoogleCloudMessaging provideGcm() {
        return GoogleCloudMessaging.getInstance(activity);
    }

    @Provides @PerActivity
    NotificationHub provideHub() {
        return new NotificationHub(activity.getString(R.string.push_HUB_NAME),
                activity.getString(R.string.push_HUB_LISTENER_STRING), activity);
    }
}
