package estimeet.meetup.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import org.androidannotations.annotations.EActivity;
import estimeet.meetup.R;
import estimeet.meetup.di.HasComponent;
import estimeet.meetup.di.components.DaggerSignInComponent;
import estimeet.meetup.di.components.SignInComponent;
import estimeet.meetup.ui.fragment.ProfileFragment;
import estimeet.meetup.ui.fragment.ProfileFragment_;
import estimeet.meetup.ui.fragment.SignInFragment;
import estimeet.meetup.ui.fragment.SignInFragment_;

/**
 * Created by AmyDuan on 14/02/16.
 */
@EActivity(R.layout.activity_sign_in)
public class SignInActivity extends BaseActivity implements HasComponent<SignInComponent>,
        SignInFragment.SignInListener, ProfileFragment.SignInListener {
    private SignInComponent signInComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeInjector();

        replaceFragment(R.id.container, new SignInFragment_());
    }

    private void initializeInjector() {
        signInComponent = DaggerSignInComponent.builder()
                .applicationComponent(getApplicationComponent())
                .build();
    }

    @Override
    public SignInComponent getComponent() {
        return signInComponent;
    }

    //region fragment callback
    @Override
    public void onPhoneVerified() {
        replaceFragment(R.id.container, new ProfileFragment_());
    }

    @Override
    public void onGetStarted() {
        startNewActivity(new Intent(this, MainActivity_.class));
        this.finish();
    }

    //endregion
}
