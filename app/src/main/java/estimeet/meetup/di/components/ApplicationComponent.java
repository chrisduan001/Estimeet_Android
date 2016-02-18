package estimeet.meetup.di.components;

import com.squareup.picasso.Picasso;

import javax.inject.Named;
import javax.inject.Singleton;
import dagger.Component;
import estimeet.meetup.di.Modules.ApplicationModule;
import estimeet.meetup.di.Modules.DatabaseModule;
import estimeet.meetup.di.Modules.ServiceModule;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.User;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import estimeet.meetup.ui.activity.BaseActivity;

/**
 * Created by AmyDuan on 6/02/16.
 */
@Singleton
@Component(modules = {ApplicationModule.class, ServiceModule.class, DatabaseModule.class})
public interface ApplicationComponent {
    void inject(BaseActivity activity);

    DataHelper dataHelper();
    ServiceHelper serviceHelper();
    Picasso picasso();
    MeetUpSharedPreference sharedPreferences();

    @Named("currentUser") User user();
}
