package estimeet.meetup.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

import javax.inject.Inject;
import javax.inject.Named;

import estimeet.meetup.R;
import estimeet.meetup.di.HasComponent;
import estimeet.meetup.di.Modules.MainModule;
import estimeet.meetup.di.components.DaggerMainComponent;
import estimeet.meetup.di.components.MainComponent;
import estimeet.meetup.model.User;
import estimeet.meetup.ui.fragment.MainFragment;
import estimeet.meetup.ui.fragment.MainFragment_;

/**
 * Created by AmyDuan on 6/02/16.
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements HasComponent<MainComponent>, MainFragment.MainCallback{

    private MainComponent mainComponent;

    @Inject @Named("currentUser") User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeInjector();

        if (user.userId == 0 || TextUtils.isEmpty(user.userName)) {
            //user not sigin or user haven't finished their profile yet
            startNewActivity(SignInActivity.getCallingIntent(this, user.userId));
            this.finish();
        } else {
            replaceFragment(R.id.container, new MainFragment_());
        }
    }

    private void initializeInjector() {
        mainComponent = DaggerMainComponent.builder()
                .applicationComponent(getApplicationComponent())
                .mainModule(new MainModule(this))
                .build();

        mainComponent.inject(this);
    }

    @Override
    public MainComponent getComponent() {
        return mainComponent;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() <= 0) {
            this.finish();
        }
    }
}
