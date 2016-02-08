package estimeet.meetup.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsOAuthSigning;
import com.digits.sdk.android.DigitsSession;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.Map;

import javax.inject.Inject;

import estimeet.meetup.R;
import estimeet.meetup.di.components.MainComponent;
import estimeet.meetup.ui.presenter.BasePresenter;
import estimeet.meetup.ui.presenter.SignInPresenter;

/**
 * Created by AmyDuan on 8/02/16.
 */
@EFragment(R.layout.fragment_sign_in)
public class SignInFragment extends BaseFragment implements SignInPresenter.SignInView {

    @Inject SignInPresenter presenter;

    @ViewById(R.id.digits_button) DigitsAuthButton digitsButton;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();

        presenter.setView(this);
    }

    @Override
    public void onResume() {
        super.onResume();


        digitsButton.setAuthTheme(R.style.CustomDigitsTheme);
        digitsButton.setBackgroundResource(R.drawable.digits_button);
        digitsButton.setCallback(new AuthCallback() {
            @Override
            public void success(DigitsSession digitsSession, String s) {
                showShortToastMessage("Successful");
                TwitterAuthConfig config = TwitterCore.getInstance().getAuthConfig();
                TwitterAuthToken token = (TwitterAuthToken) digitsSession.getAuthToken();
                DigitsOAuthSigning oAuthSigning = new DigitsOAuthSigning(config, token);
                Map<String, String> authHeaders = oAuthSigning.getOAuthEchoHeadersForVerifyCredentials();
            }

            @Override
            public void failure(DigitsException e) {
                showShortToastMessage(e.getMessage());
            }
        });
    }

    private void initialize() {
        getComponent(MainComponent.class).inject(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }
}
