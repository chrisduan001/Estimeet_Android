package estimeet.meetup.di.components;

import android.app.Activity;

import dagger.Component;
import estimeet.meetup.di.Modules.ActivityModule;
import estimeet.meetup.di.PerActivity;

/**
 * Created by AmyDuan on 11/02/16.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    Activity activity();
}
