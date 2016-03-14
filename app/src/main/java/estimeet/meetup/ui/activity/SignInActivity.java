package estimeet.meetup.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import org.androidannotations.annotations.EActivity;

import java.util.EventListener;

import javax.inject.Inject;
import javax.inject.Named;

import estimeet.meetup.R;
import estimeet.meetup.di.HasComponent;
import estimeet.meetup.di.components.DaggerSignInComponent;
import estimeet.meetup.di.components.SignInComponent;
import estimeet.meetup.model.User;
import estimeet.meetup.ui.fragment.ProfileFragment;
import estimeet.meetup.ui.fragment.ProfileFragment_;
import estimeet.meetup.ui.fragment.SignInFragment;
import estimeet.meetup.ui.fragment.SignInFragment_;

/**
 * Created by AmyDuan on 14/02/16.
 */
@EActivity(R.layout.activity_sign_in)
public class SignInActivity extends BaseActivity implements HasComponent<SignInComponent>,
        SignInFragment.SignInCallback, ProfileFragment.SignInCallback {

    private static final String USER_ID = "SIGNIN_ACTIVITY_USER_ID";

    public static Intent getCallingIntent(Context context, long userId) {
        Intent intent = new Intent(context, SignInActivity_.class);
        intent.putExtra(USER_ID, userId);
        return intent;
    }

    private SignInComponent signInComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeInjector();

        Intent intent = getIntent();

        if (intent != null) {
            if (intent.getLongExtra(USER_ID, 0) != 0) {
                showProfileFragment();
            } else {
                showSignInFragment();
            }
        } else {
            throw new RuntimeException("Missing intent parameter for SignInActivity");
        }
    }

    private void initializeInjector() {
        signInComponent = DaggerSignInComponent.builder()
                .applicationComponent(getApplicationComponent())
                .build();

        signInComponent.inject(this);
    }

    private void showProfileFragment() {
        replaceFragment(R.id.container, new ProfileFragment_());
    }

    private void showSignInFragment() {
        replaceFragment(R.id.container, new SignInFragment_());
    }

    @Override
    public SignInComponent getComponent() {
        return signInComponent;
    }

    //region fragment callback
    @Override
    public void onSigninSuccessful(boolean isProfileCompleted) {
        if (isProfileCompleted) {
            startMainActivity();
        } else {
            replaceFragment(R.id.container, new ProfileFragment_());
        }
    }

    @Override
    public void onGetStarted() {
        startMainActivity();
    }

    private void startMainActivity() {
        MainActivity_.intent(this).start();
        this.finish();
    }

    @Override
    public void onAuthFailed() {
        showSignInFragment();
    }

    @Override
    public void navToFriendList() {
        startMainActivity();
        ManageFriendActivity_.intent(this).start();
    }
    //endregion
}
