package estimeet.meetup.di.components;

import javax.inject.Named;

import dagger.Component;
import estimeet.meetup.di.Modules.ManageFriendModule;
import estimeet.meetup.di.Modules.ManageProfileModule;
import estimeet.meetup.di.PerActivity;
import estimeet.meetup.model.User;
import estimeet.meetup.ui.fragment.ManageProfileFragment;

/**
 * Created by AmyDuan on 20/03/16.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ManageProfileModule.class)
public interface ManageProfileComponent {
    void inject(ManageProfileFragment fragment);

    @Named("currentUser") User user();
}
