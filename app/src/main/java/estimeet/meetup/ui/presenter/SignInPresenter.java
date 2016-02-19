package estimeet.meetup.ui.presenter;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsOAuthSigning;
import com.digits.sdk.android.DigitsSession;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import estimeet.meetup.interactor.SignInInteractor;
import estimeet.meetup.model.PostModel.AuthUser;
import estimeet.meetup.model.User;
import estimeet.meetup.ui.BaseView;
import estimeet.meetup.ui.fragment.SignInFragment;

/**
 * Created by AmyDuan on 8/02/16.
 *
 *  * Sign-in process:
 * 1. user get verified by digits
 * 2. send digits auth (header and token) to server
 * 3. server verify
 *      3.1 if user registered before: get user password, dpuri and navigate to main activity
 *      3.2 first time user: get user password (will be used for update profile dp, name etc)
 */
public class SignInPresenter extends BasePresenter implements SignInInteractor.SignInListener {

    private SignInView view;

    @Inject SignInInteractor signInInteractor;

    //region lifecycle
    @Inject
    public SignInPresenter(SignInInteractor interactor) {
        this.signInInteractor = interactor;
        signInInteractor.call(this);
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
        super.onPause();
        signInInteractor.unSubscribe();
    }
    //endregion

    //region fragment call
    public void setView(SignInView view) {
        this.view = view;
    }

    public void createDigitsAuthCallback() {
        view.setAuthCallback(new AuthCallback() {
            @Override
            public void success(DigitsSession digitsSession, String s) {
                //todo..temp progress dialog, change to a better one later
                view.showProgressDialog("Loading");
                signInInteractor.onAuthSuccessful(digitsSession, s);
            }

            @Override
            public void failure(DigitsException e) {
                view.showShortToastMessage(e.getLocalizedMessage());
            }
        });
    }
    //endregion

    //region interactor callback
    @Override
    public void onSignInSuccessful(User user) {
        view.dismissProgressDialog();
        view.onSignInSuccessful(user.isProfileCompleted());
    }

    @Override
    public void onError(String message) {
        view.dismissProgressDialog();
        view.showShortToastMessage(message);
    }
    //endregion

    public interface SignInView extends BaseView {
        void setAuthCallback(AuthCallback callback);
        void onSignInSuccessful(boolean isProfileCompleted);
    }
}
