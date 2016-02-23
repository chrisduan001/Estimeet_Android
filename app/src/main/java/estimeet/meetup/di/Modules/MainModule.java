package estimeet.meetup.di.Modules;

import android.app.Activity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import estimeet.meetup.di.PerActivity;
import estimeet.meetup.model.MeetUpSharedPreference;
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
}
