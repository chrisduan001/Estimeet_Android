package estimeet.meetup.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
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
import estimeet.meetup.di.components.MainComponent;
import estimeet.meetup.ui.presenter.BasePresenter;
import estimeet.meetup.ui.presenter.SignInPresenter;
import estimeet.meetup.util.Navigator;

/**
 * Created by AmyDuan on 8/02/16.
 */
@EFragment(R.layout.fragment_sign_in)
public class SignInFragment extends BaseFragment implements SignInPresenter.SignInView {

    //call back to activity listener
    public interface SignInListener {
        void onPhoneVerified();
    }

    @Inject SignInPresenter presenter;

    @ViewById(R.id.sign_in_button) Button btnSignIn;

    private SignInListener signInListener;

    //region lifecycle
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SignInListener) {
            this.signInListener = (SignInListener) context;
        } else {
            throw new UnsupportedOperationException("Activity must implement " +
                    SignInListener.class.getSimpleName());
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
        getComponent(MainComponent.class).inject(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }
    //endregion

    //region button click
    @Click(R.id.sign_in_button)
    protected void signInClicked() {
        //todo..hard coded country code, for 1st version only
        DigitsAuthConfig builder = new DigitsAuthConfig.Builder()
                .withAuthCallBack(callback())
                .withThemeResId(R.style.CustomDigitsTheme)
                .withPhoneNumber("+64").build();

        Digits.authenticate(builder);
    }
    //endregion

    //region call presenter
    private AuthCallback callback () {
        return new AuthCallback() {
            @Override
            public void success(DigitsSession digitsSession, String s) {
                showShortToastMessage("Successful");
                TwitterAuthConfig config = TwitterCore.getInstance().getAuthConfig();
                TwitterAuthToken token = (TwitterAuthToken) digitsSession.getAuthToken();
                DigitsOAuthSigning oAuthSigning = new DigitsOAuthSigning(config, token);
                final Map<String, String> authHeaders = oAuthSigning.getOAuthEchoHeadersForVerifyCredentials();

                signInListener.onPhoneVerified();
            }

            @Override
            public void failure(DigitsException e) {
                showShortToastMessage(e.getMessage());
            }
        };
    }

    private void setView() {
        presenter.setView(this);
    }
    //endregion
}
