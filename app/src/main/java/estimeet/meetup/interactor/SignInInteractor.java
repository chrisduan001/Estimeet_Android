package estimeet.meetup.interactor;

import com.digits.sdk.android.DigitsOAuthSigning;
import com.digits.sdk.android.DigitsSession;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;

import java.util.Map;

import javax.inject.Inject;

import estimeet.meetup.DefaultSubscriber;
import estimeet.meetup.model.PostModel.AuthUser;
import estimeet.meetup.model.User;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by AmyDuan on 8/02/16.
 *
 * Sign-in process:
 * 1. user get verified by digits
 * 2. send digits auth (header and token) to server
 * 3. server verify
 *      3.1 if user registered before: get user password, dpuri and navigate to main activity
 *      3.2 first time user: get user password (will be used for update profile dp, name etc)
 */
public class SignInInteractor extends BaseInteractor {

    private SignInListener listener;
    private SignInSubscriber signInSubscriber;

    @Inject
    public SignInInteractor(ServiceHelper serviceHelper, DataHelper dataHelper) {
        super(serviceHelper, dataHelper);
    }

    //region fragment call
    public void call(SignInListener listener) {
        this.listener = listener;

        signInSubscriber = new SignInSubscriber(dataHelper, listener);
    }

    public void signInUser(AuthUser user) {
        serviceHelper.signInUser(user).subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(signInSubscriber);
    }

    public void unSubscribe() {
        if (!signInSubscriber.isUnsubscribed()) {
            signInSubscriber.unsubscribe();
        }
    }

    public void onAuthSuccessful(DigitsSession digitsSession, String s) {
        TwitterAuthConfig config = TwitterCore.getInstance().getAuthConfig();
        TwitterAuthToken token = (TwitterAuthToken) digitsSession.getAuthToken();
        DigitsOAuthSigning oAuthSigning = new DigitsOAuthSigning(config, token);
        final Map<String, String> authHeaders = oAuthSigning.getOAuthEchoHeadersForVerifyCredentials();
        String authProvider = authHeaders.get("X-Auth-Service-Provider");
        String authToken = authHeaders.get("X-Verify-Credentials-Authorization");
        String phoneNumber = digitsSession.getPhoneNumber().replace("+", "");
        long id = digitsSession.getId();

        signInUser(new AuthUser(authToken, authProvider, phoneNumber, id));
    }
    //endregion

    private static class SignInSubscriber extends DefaultSubscriber<User> {
        private final DataHelper dataHelper;
        private final SignInListener listener;

        public SignInSubscriber(DataHelper dataHelper, SignInListener listener) {
            this.dataHelper = dataHelper;
            this.listener = listener;
        }

        @Override
        public void onNext(User user) {
            if (user.hasError()) {
                throwError(user.errorMessage);
            } else {
                //when user.name == empty,
                //user will need to enter profile page to setup the name and dp
                if (user.isProfileCompleted()) {
                    //todo.. save to sharedpreference
                }

                listener.onSignInSuccessful(user);
            }
        }

        @Override
        public void onCompleted() {
            super.onCompleted();
        }

        @Override
        public void onError(Throwable e) {
            listener.onError(e.getLocalizedMessage());
        }
    }

    public interface SignInListener extends BaseListener {
        void onSignInSuccessful(User user);
    }
}
