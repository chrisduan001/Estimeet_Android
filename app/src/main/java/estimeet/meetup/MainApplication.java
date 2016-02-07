package estimeet.meetup;

import android.app.Application;

import estimeet.meetup.di.Modules.ApplicationModule;
import estimeet.meetup.di.components.ApplicationComponent;
import estimeet.meetup.di.components.DaggerApplicationComponent;

/**
 * Created by AmyDuan on 6/02/16.
 */
public class MainApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeInjector();
    }

    private void initializeInjector() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
