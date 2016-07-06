package estimeet.meetup.di.components;

import dagger.Component;
import estimeet.meetup.di.PerActivity;
import estimeet.meetup.ui.activity.SignInActivity;
import estimeet.meetup.ui.fragment.PermissionFragment;
import estimeet.meetup.ui.fragment.ProfileFragment;
import estimeet.meetup.ui.fragment.SignInFragment;

/**
 * Created by AmyDuan on 14/02/16.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class)
public interface SignInComponent {
    void inject(SignInActivity activity);
    void inject(SignInFragment fragment);
    void inject(ProfileFragment fragment);
    void inject(PermissionFragment fragment);
}
