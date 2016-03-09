package estimeet.meetup;

import android.app.Application;
import android.content.Context;

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
    private static final String TWITTER_KEY = "LBQcvuD4RwAY2Y5HaIqKR0b19";
    private static final String TWITTER_SECRET = "X3BOAxjLa5bGTOQGkWBMJNkvo9NZDIjoOymG8gkaN9oi0Q1hbl";

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
