package estimeet.meetup.di.components;

import android.app.Activity;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.microsoft.windowsazure.messaging.NotificationHub;

import javax.inject.Named;

import dagger.Component;
import estimeet.meetup.di.Modules.MainModule;
import estimeet.meetup.di.PerActivity;
import estimeet.meetup.model.PushModel;
import estimeet.meetup.model.User;
import estimeet.meetup.ui.activity.MainActivity;
import estimeet.meetup.ui.fragment.MainFragment;

/**
 * Created by AmyDuan on 6/02/16.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = MainModule.class)
public interface MainComponent {
    void inject(MainActivity activity);
    void inject(MainFragment fragment);

    Activity activity();
    PushModel pushModel();
    @Named("currentUser") User user();
}
