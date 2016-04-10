package estimeet.meetup.di.components;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import estimeet.meetup.di.Modules.BackgroundServiceModule;
import estimeet.meetup.di.Modules.ServiceModule;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.network.ServiceHelper;

/**
 * Created by AmyDuan on 9/04/16.
 */
@Singleton
@Component(modules = {ServiceModule.class, BackgroundServiceModule.class})
public interface BackgroundServiceComponent {
    void inject(Context context);

    ServiceHelper serviceHelper();
    MeetUpSharedPreference meetUpSharedPreference();
}
