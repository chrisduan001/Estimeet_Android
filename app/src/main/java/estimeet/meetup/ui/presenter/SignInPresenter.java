package estimeet.meetup.ui.presenter;

import android.Manifest;
import android.content.res.Resources;
import android.text.TextUtils;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;

import java.util.List;

import javax.inject.Inject;

import estimeet.meetup.MainApplication;
import estimeet.meetup.R;
import estimeet.meetup.interactor.FriendsInteractor;
import estimeet.meetup.interactor.SignInInteractor;
import estimeet.meetup.model.Friend;
import estimeet.meetup.model.User;
import estimeet.meetup.ui.BaseView;
import estimeet.meetup.util.ContactList;

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
public class SignInPresenter extends BasePresenter implements SignInInteractor.SignInListener,
        FriendsInteractor.GetFreindsListener{

    private SignInView view;
    private final SignInInteractor.SignInListener listener;
    //needs to set this field to class variable, otherwise the callback will be called
    //as the AuthCallback holds a weak reference and will be set to null when nav to digits auth page
    //authcallback will be cleaned manually at onDestory
    @SuppressWarnings("FieldCanBeLocal")
    private AuthCallback authCallback;

    private SignInInteractor signInInteractor;
    private FriendsInteractor friendsInteractor;

    //region lifecycle
    @Inject
    public SignInPresenter(SignInInteractor interactor, FriendsInteractor friendsInteractor) {
        this.signInInteractor = interactor;
        this.friendsInteractor = friendsInteractor;
        this.listener = this;
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
        signInInteractor.unSubscribe();
        friendsInteractor.unSubscribe();
    }

    @Override
    public void onDestory() {
        super.onDestory();
        authCallback = null;
    }

    @Override
    public void onAuthFailed() {
        view.dismissProgressDialog();
        view.onAuthFailed();
    }

    @Override
    public void onPermissionResult(boolean isGranted) {
        if (isGranted) {
            view.showProgressDialog();
            view.onReadContactPermissionGranted();
        }
        view.dismissProgressDialog();
        view.onSignInSuccessful(false);
    }

    //endregion

    //region fragment call
    public void setView(SignInView view) {
        this.view = view;
    }

    public void createDigitsAuthCallback() {
        view.setAuthCallback(getAuthCallback());
    }

    public void sendContactList(String contacts) {
        signInInteractor.sendContacts(contacts);
    }

    private AuthCallback getAuthCallback() {
        authCallback = new AuthCallback() {
            @Override
            public void success(DigitsSession digitsSession, String s) {
                //todo..temp progress dialog, change to a better one later
                view.showProgressDialog();
                signInInteractor.onAuthSuccessful(digitsSession, s, listener);
            }

            @Override
            public void failure(DigitsException e) {
                view.onDigitsError();
            }
        };

        return authCallback;
    }
    //endregion

    //region interactor callback
    @Override
    public void onSignInSuccessful(User user) {
        //request user friend list if they logged in for the first time
        if (!user.isProfileCompleted()) {
            view.dismissProgressDialog();
            view.checkPermission(Manifest.permission.READ_CONTACTS);
        } else {
            friendsInteractor.call(this);
            friendsInteractor.getFriendsList(user);
        }
    }

    @Override
    public void onError(String errorMessage) {
        view.dismissProgressDialog();
        view.onError(errorMessage);
    }

    @Override
    public void onFriendListCompleted(List<Friend> friends) {
        view.dismissProgressDialog();
        view.onSignInSuccessful(true);
    }
    //endregion

    public interface SignInView extends BaseView {
        void setAuthCallback(AuthCallback callback);
        void onSignInSuccessful(boolean isProfileCompleted);
        void onDigitsError();
        void onReadContactPermissionGranted();
    }
}
