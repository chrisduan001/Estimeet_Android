package estimeet.meetup;

import android.app.Application;

import com.digits.sdk.android.Digits;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import estimeet.meetup.di.Modules.ApplicationModule;
import estimeet.meetup.di.components.ApplicationComponent;
import estimeet.meetup.di.components.DaggerApplicationComponent;
import io.fabric.sdk.android.Fabric;

/**
 * Created by AmyDuan on 6/02/16.
 */
public class MainApplication extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "1Rqz0dZC04oWjHvfMoaNmqYXh";
    private static final String TWITTER_SECRET = "riWKJ9Km2rIkPd14IWnr058Qxfjz1LNdeu4WLZDWV4oerovgTO";


    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits());
        initializeInjector();
    }

    private void initializeInjector() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
