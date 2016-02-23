package estimeet.meetup.interactor;

import com.digits.sdk.android.DigitsOAuthSigning;
import com.digits.sdk.android.DigitsSession;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;

import java.util.Map;

import javax.inject.Inject;

import estimeet.meetup.DefaultSubscriber;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.PostModel.AuthUser;
import estimeet.meetup.model.TokenResponse;
import estimeet.meetup.model.User;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
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
public class SignInInteractor extends BaseInteractor<User> {

    private SignInListener listener;
    private TokenSubscriber tokenSubscriber;

    @Inject
    public SignInInteractor(ServiceHelper serviceHelper, DataHelper dataHelper, MeetUpSharedPreference sharedPreference) {
        super(serviceHelper, dataHelper, sharedPreference);
    }

    //region fragment call
    private void signInUser(AuthUser user, SignInListener listener) {
        this.listener = listener;
        tokenSubscriber = new TokenSubscriber();
        initSignIn(user);
    }

    public void unSubscribe() {
        if (tokenSubscriber != null && !tokenSubscriber.isUnsubscribed()) {
            tokenSubscriber.unsubscribe();
        }
    }

    public void onAuthSuccessful(final DigitsSession digitsSession, String s, final SignInListener listener) {
        TwitterAuthConfig config = TwitterCore.getInstance().getAuthConfig();
        TwitterAuthToken token = (TwitterAuthToken) digitsSession.getAuthToken();
        DigitsOAuthSigning oAuthSigning = new DigitsOAuthSigning(config, token);
        final Map<String, String> authHeaders = oAuthSigning.getOAuthEchoHeadersForVerifyCredentials();
        String authProvider = authHeaders.get("X-Auth-Service-Provider");
        String authToken = authHeaders.get("X-Verify-Credentials-Authorization");
        String phoneNumber = digitsSession.getPhoneNumber().replace("+", "");
        long id = digitsSession.getId();

        signInUser(new AuthUser(authToken, authProvider, phoneNumber, id), listener);
    }
    //endregion

    //region network
    private void initSignIn(AuthUser user) {
        serviceHelper.signInUser(user).flatMap(new Func1<User, Observable<TokenResponse>>() {
            @Override
            public Observable<TokenResponse> call(User user) {
                if (user.hasError()) {
                    throw new RuntimeException(user.errorCode + "");
                } else {
                    sharedPreference.storeUser(user);
                }
                return getTokenObservable(user);
            }
        }).subscribe(new TokenSubscriber());
    }

    private class TokenSubscriber extends DefaultSubscriber<TokenResponse> {
        @Override
        public void onNext(TokenResponse tokenResponse) {
            sharedPreference.updateUserToken(tokenResponse.access_token, tokenResponse.expires_in);
            listener.onSignInSuccessful(sharedPreference.getUserFromSp());
        }

        @Override
        public void onError(Throwable e) {
            listener.onError(Integer.parseInt(e.getLocalizedMessage()));
        }
    }

    //endregion

    public interface SignInListener extends BaseListener {
        void onSignInSuccessful(User user);
    }
}
