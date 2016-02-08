package estimeet.meetup.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsAuthConfig;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsOAuthSigning;
import com.digits.sdk.android.DigitsSession;
import com.twitter.sdk.android.core.AuthToken;
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
import estimeet.meetup.ui.presenter.MainPresenter;
import estimeet.meetup.util.DigitsButton;

/**
 * Created by AmyDuan on 6/02/16.
 */
@EFragment(R.layout.fragment_main)
public class MainFragment extends BaseFragment implements MainPresenter.MainView {

    @Inject MainPresenter presenter;

    @ViewById(R.id.digits_button)
    DigitsButton digitsButton;

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
        digitsButton.setupButton();
        digitsButton.setCallback(new AuthCallback() {
            @Override
            public void success(DigitsSession digitsSession, String s) {
                showShortToastMessage("Successful");

                TwitterAuthConfig config = TwitterCore.getInstance().getAuthConfig();
                TwitterAuthToken token = (TwitterAuthToken)digitsSession.getAuthToken();
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
