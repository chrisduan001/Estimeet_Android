package estimeet.meetup.di.Modules;

import android.content.Context;
import android.content.SharedPreferences;

import com.squareup.picasso.Picasso;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import estimeet.meetup.MainApplication;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.User;

/**
 * Created by AmyDuan on 6/02/16.
 */
@Module
public class ApplicationModule {
    private final MainApplication application;

    public ApplicationModule(MainApplication application) {
        this.application = application;
    }

    @Provides @Singleton
    public Context provideApplicationContext() {
        return this.application;
    }

    @Provides @Singleton
    public Picasso providePicasso() {
        return Picasso.with(this.application);
    }

    @Provides @Singleton
    public SharedPreferences provideSharedPreferences() {
        return application.getSharedPreferences("com.estimeet.meetup_shared_preference", Context.MODE_PRIVATE);
    }

    @Provides @Singleton @Named("currentUser")
    User provideUser(MeetUpSharedPreference sharedPreference) {
        return sharedPreference.getUserFromSp();
    }
}
