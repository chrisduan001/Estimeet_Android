package estimeet.meetup.di.Modules;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by AmyDuan on 9/04/16.
 */
@Module
public class BackgroundServiceModule {

    private final Context context;

    public BackgroundServiceModule(Context context) {
        this.context = context;
    }

    @Provides @Singleton
    public Context provideContext() {
        return this.context;
    }

    @Provides @Singleton
    public SharedPreferences provideSharedPreferences() {
        return context.getSharedPreferences("com.estimeet.meetup_shared_preference", Context.MODE_PRIVATE);
    }
}
