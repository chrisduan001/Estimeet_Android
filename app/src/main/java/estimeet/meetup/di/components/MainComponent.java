package estimeet.meetup.di.components;

import dagger.Component;
import estimeet.meetup.di.PerActivity;
import estimeet.meetup.ui.fragment.MainFragment;
import estimeet.meetup.ui.fragment.ProfileFragment;
import estimeet.meetup.ui.fragment.SignInFragment;

/**
 * Created by AmyDuan on 6/02/16.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class)
public interface MainComponent {
    void inject(MainFragment fragment);
    void inject(SignInFragment fragment);
    void inject(ProfileFragment fragment);
}
