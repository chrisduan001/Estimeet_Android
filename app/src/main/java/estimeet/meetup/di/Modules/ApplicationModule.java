package estimeet.meetup.di.Modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import estimeet.meetup.MainApplication;

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
}
