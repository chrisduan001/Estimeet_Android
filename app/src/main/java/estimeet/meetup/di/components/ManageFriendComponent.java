package estimeet.meetup.di.components;

import javax.inject.Named;

import dagger.Component;
import estimeet.meetup.di.Modules.ManageFriendModule;
import estimeet.meetup.di.PerActivity;
import estimeet.meetup.model.User;
import estimeet.meetup.ui.activity.ManageFriendActivity;
import estimeet.meetup.ui.fragment.ManageFriendFragment;

/**
 * Created by AmyDuan on 15/03/16.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ManageFriendModule.class)
public interface ManageFriendComponent {
    void inject(ManageFriendFragment fragment);

    @Named("currentUser") User user();
}
