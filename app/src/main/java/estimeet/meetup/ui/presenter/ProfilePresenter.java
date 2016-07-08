package estimeet.meetup.ui.presenter;

import android.Manifest;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import estimeet.meetup.interactor.FriendsInteractor;
import estimeet.meetup.interactor.ProfileInteractor;
import estimeet.meetup.ui.BaseView;

/**
 * Created by AmyDuan on 9/02/16.
 */
public class ProfilePresenter extends BasePresenter implements ProfileInteractor.ProfileListener {

    private WeakReference<ProfileView> view;
    private ProfileInteractor interactor;

    @Inject
    public ProfilePresenter(ProfileInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void onResume() {
        interactor.call(this);
    }

    @Override
    public void onPause() {
        interactor.unSubscribe();
    }

    //override from base presenter
    @Override
    public void onPermissionResult(boolean isGranted) {
        if (isGranted) {
            view.get().startCameraAction();
        }
    }

    //region fragment call
    public void setView(ProfileView view) {
        this.view = new WeakReference<>(view);
    }

    public void intentToStartCamera() {
        view.get().checkPermission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public void registerCallBack(CallbackManager callbackManager) {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                interactor.initFbRequest(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    public void onUpdateProfile(String name, Bitmap bitmap) {
        if (TextUtils.isEmpty(name)) {
            view.get().onInvalidName();
            return;
        }

        view.get().showProgressDialog();

        interactor.initUpdateProfile(name, bitmap, true);
    }
    //endregion

    //region mainInteractor callback
    @Override
    public void onError(String errorMessage) {
        dismissProgressDialog();
        view.get().onError(errorMessage);
    }

    @Override
    public void onAuthFailed() {
        view.get().onAuthFailed();
        dismissProgressDialog();
    }

    @Override
    public void onFacebookResponse(String name, String dpUri) {
        view.get().onReceivedFbData(name, dpUri);
    }

    @Override
    public void onUpdateProfileSuccessful() {
        dismissProgressDialog();
        view.get().onProfileCompleted();
    }

    //not implemented
    @Override
    public void onGetUserDp(Bitmap bitmap) {}
    //endregion

    //region logic
    private void dismissProgressDialog() {
        view.get().dismissProgressDialog();
    }

    //endregion
    public interface ProfileView extends BaseView {
        void startCameraAction();
        void onReceivedFbData(String name, String dpUri);
        void onProfileCompleted();
        void onInvalidName();
    }
}
