package estimeet.meetup.di.components;

import dagger.Component;
import estimeet.meetup.di.Modules.MainModule;
import estimeet.meetup.di.PerActivity;
import estimeet.meetup.ui.fragment.MainFragment;

/**
 * Created by AmyDuan on 6/02/16.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = MainModule.class)
public interface MainComponent {
    void inject(MainFragment fragment);
}
