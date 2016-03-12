package estimeet.meetup.interactor;

import android.text.TextUtils;

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
import estimeet.meetup.model.PostModel.SendContact;
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
    private SigninSubscriber signinSubscriber;

    @Inject
    public SignInInteractor(ServiceHelper serviceHelper, DataHelper dataHelper, MeetUpSharedPreference sharedPreference) {
        super(serviceHelper, dataHelper, sharedPreference);
    }

    //region fragment call
    private void signInUser(AuthUser user, SignInListener listener) {
        this.listener = listener;
        signinSubscriber = new SigninSubscriber();
        initSignIn(user);
    }

    public void unSubscribe() {
        if (signinSubscriber != null && !signinSubscriber.isUnsubscribed()) {
            signinSubscriber.unsubscribe();
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

    public void sendContacts(String contacts) {
        User user = sharedPreference.getUserFromSp();
        SendContact contactModel = new SendContact(user.id, user.userId, contacts);
        makeRequest(user, serviceHelper.sendContacts(user.token, contactModel), new DefaultSubscriber<User>(), true);
    }
    //endregion

    //region network
    private void initSignIn(AuthUser user) {
        makeRequest(null, serviceHelper.signInUser(user), signinSubscriber, false);
    }

    private class SigninSubscriber extends DefaultSubscriber<User> {
        @Override
        public void onNext(User user) {
            super.onNext(user);

            sharedPreference.storeUserInfo(user);
            if (!TextUtils.isEmpty(user.userName) && !TextUtils.isEmpty(user.dpUri)) {
                sharedPreference.updateUserProfile(user.userName, user.dpUri);
            }
            listener.onSignInSuccessful(user);
        }

        @Override
        public void onError(Throwable e) {
            if (e.getLocalizedMessage().equals("404")) {
                listener.onAuthFailed();
                return;
            }
            listener.onError(e.getLocalizedMessage());
        }
    }

    //endregion

    public interface SignInListener extends BaseListener {
        void onSignInSuccessful(User user);
    }
}
