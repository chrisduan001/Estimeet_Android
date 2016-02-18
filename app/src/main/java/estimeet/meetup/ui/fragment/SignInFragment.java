package estimeet.meetup.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthConfig;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsOAuthSigning;
import com.digits.sdk.android.DigitsSession;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import java.util.Map;
import javax.inject.Inject;
import estimeet.meetup.R;
import estimeet.meetup.di.components.SignInComponent;
import estimeet.meetup.ui.presenter.BasePresenter;
import estimeet.meetup.ui.presenter.SignInPresenter;

/**
 * Created by AmyDuan on 8/02/16.
 *
 *   Sign-in process:
 * 1. user get verified by digits
 * 2. send digits auth (header and token) to server
 * 3. server verify
 *      3.1 if user registered before: get user password, dpuri and navigate to main activity
 *      3.2 first time user: get user password (will be used for update profile dp, name etc)
 */

@EFragment(R.layout.fragment_sign_in)
public class SignInFragment extends BaseFragment implements SignInPresenter.SignInView {

    //call back to activity listener
    public interface SignInCallback {
        void onSigninSuccessful(boolean isProfileCompleted);
    }

    @Inject SignInPresenter presenter;

    @ViewById(R.id.sign_in_button)  Button btnSignIn;
    @ViewById(R.id.progress_bar)    ProgressBar progressBar;

    private SignInCallback signInCallback;

    //region lifecycle
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SignInCallback) {
            this.signInCallback = (SignInCallback) context;
        } else {
            throw new UnsupportedOperationException("Activity must implement " +
                    SignInCallback.class.getSimpleName());
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();

        setView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initialize() {
        getComponent(SignInComponent.class).inject(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public ProgressBar getProgressBar() {
        return progressBar;
    }
    //endregion

    //region button click
    @Click(R.id.sign_in_button)
    protected void signInClicked() {
        presenter.createDigitsAuthCallback();
    }
    //endregion

    //region call presenter
    private void setView() {
        presenter.setView(this);
    }
    //endregion

    //region presenter callback
    @Override
    public void setAuthCallback(AuthCallback callback) {
        //todo..hard coded country code, for 1st version only
        DigitsAuthConfig builder = new DigitsAuthConfig.Builder()
                .withAuthCallBack(callback)
                .withThemeResId(R.style.CustomDigitsTheme)
                .withPhoneNumber("+64").build();

        Digits.authenticate(builder);
    }

    @Override
    public void onSignInSuccessful(boolean isProfileCompleted) {
        signInCallback.onSigninSuccessful(isProfileCompleted);
    }
    //endregion
}
