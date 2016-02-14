package estimeet.meetup.di.components;

import dagger.Component;
import estimeet.meetup.di.PerActivity;
import estimeet.meetup.ui.fragment.ProfileFragment;
import estimeet.meetup.ui.fragment.SignInFragment;

/**
 * Created by AmyDuan on 14/02/16.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class)
public interface SignInComponent {
    void inject(SignInFragment fragment);
    void inject(ProfileFragment fragment);
}
