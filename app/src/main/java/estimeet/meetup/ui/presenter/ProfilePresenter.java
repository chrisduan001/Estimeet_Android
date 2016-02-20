package estimeet.meetup.ui.presenter;

import android.Manifest;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.io.ByteArrayOutputStream;

import javax.inject.Inject;
import javax.inject.Named;

import estimeet.meetup.interactor.ProfileInteractor;
import estimeet.meetup.model.PostModel.UpdateModel;
import estimeet.meetup.model.User;
import estimeet.meetup.ui.BaseView;

/**
 * Created by AmyDuan on 9/02/16.
 */
public class ProfilePresenter extends BasePresenter implements ProfileInteractor.ProfileListener {

    private ProfileView view;
    private ProfileInteractor interactor;

    @Inject @Named("currentUser") User user;

    @Inject
    public ProfilePresenter(ProfileInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {
        interactor.unSubscribe();
    }

    //override from base presenter
    @Override
    public void onPermissionResult(boolean isGranted) {
        if (isGranted) {
            view.startCameraAction();
        }
    }

    //region fragment call
    public void setView(ProfileView view) {
        this.view = view;
    }

    public void intentToStartCamera() {
        view.checkPermission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
            view.onInvalidName();
            return;
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        UpdateModel updateModel = new UpdateModel(user.userId, user.password, name, byteArray);

        interactor.initUpdateProfile(user.token, updateModel, this);
    }
    //endregion

    //region interactor callback
    @Override
    public void onError(int errorCode) {
        //todo..set error message based on errorcode
        view.showShortToastMessage("");
    }

    @Override
    public void onFacebookResponse(String name, String dpUri) {
        view.onReceivedFbData(name, dpUri);
    }

    @Override
    public void onUpdateProfileSuccessful() {
        view.onProfileCompleted();
    }
    //region

    public interface ProfileView extends BaseView {
        void startCameraAction();
        void onReceivedFbData(String name, String dpUri);
        void onProfileCompleted();
        void onInvalidName();
    }
}
