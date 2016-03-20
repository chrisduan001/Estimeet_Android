package estimeet.meetup.di.Modules;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import estimeet.meetup.di.PerActivity;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.User;

/**
 * Created by AmyDuan on 20/03/16.
 */
@Module
public class ManageProfileModule {
    @Provides
    @PerActivity
    @Named("currentUser")
    public User provideUser(MeetUpSharedPreference sharedPreference) {
        return sharedPreference.getUserFromSp();
    }
}
