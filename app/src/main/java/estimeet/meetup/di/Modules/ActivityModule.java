package estimeet.meetup.di.Modules;

import android.app.Activity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import estimeet.meetup.di.PerActivity;
import estimeet.meetup.ui.activity.MainActivity;

/**
 * Created by AmyDuan on 11/02/16.
 */
@Module
public class ActivityModule {
    private final Activity activity;
    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides @PerActivity
    public Activity provideActivity() {
        return this.activity;
    }
}
