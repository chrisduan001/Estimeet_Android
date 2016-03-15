package estimeet.meetup.di.components;

import dagger.Component;
import estimeet.meetup.di.PerActivity;
import estimeet.meetup.ui.activity.ManageFriendActivity;
import estimeet.meetup.ui.fragment.ManageFriendFragment;

/**
 * Created by AmyDuan on 15/03/16.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class)
public interface ManageFriendComponent {
    void inject(ManageFriendFragment fragment);
}
