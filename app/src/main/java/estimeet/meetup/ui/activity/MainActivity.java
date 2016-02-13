package estimeet.meetup.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import org.androidannotations.annotations.EActivity;
import estimeet.meetup.R;
import estimeet.meetup.di.HasComponent;
import estimeet.meetup.di.Modules.ActivityModule;
import estimeet.meetup.di.components.DaggerMainComponent;
import estimeet.meetup.di.components.MainComponent;
import estimeet.meetup.ui.fragment.MainFragment_;
import estimeet.meetup.ui.fragment.ProfileFragment_;
import estimeet.meetup.ui.fragment.SignInFragment;
import estimeet.meetup.ui.fragment.SignInFragment_;

/**
 * Created by AmyDuan on 6/02/16.
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements HasComponent<MainComponent>, SignInFragment.SignInListener {

    private MainComponent mainComponent;

    private boolean NEEDSIGNIN = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeInjector();

        if (NEEDSIGNIN) {
            replaceFragment(R.id.container, new SignInFragment_());
        } else {
            replaceFragment(R.id.container, new MainFragment_());
        }

    }

    private void initializeInjector() {
        mainComponent = DaggerMainComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
    }

    @Override
    public MainComponent getComponent() {
        return mainComponent;
    }

    //region fragment callback
    @Override
    public void onPhoneVerified() {
        replaceFragment(R.id.container, new ProfileFragment_());
    }

    //endregion


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() <= 0) {
            this.finish();
        }
    }
}
