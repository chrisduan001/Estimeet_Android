package estimeet.meetup.di.Modules;

import android.content.ContentResolver;
import android.content.Context;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import estimeet.meetup.model.database.DataHelper;

/**
 * Created by AmyDuan on 28/01/16.
 */
@Module
public class DatabaseModule {

    @Provides @Singleton
    public ContentResolver provideContentResolver(Context applicationContext) {
        return applicationContext.getContentResolver();
    }

    @Provides @Singleton
    public DataHelper provideDataHelper(ContentResolver resolver) {
        return new DataHelper(resolver);
    }
}
