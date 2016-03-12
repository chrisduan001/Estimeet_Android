package estimeet.meetup.ui.presenter;

import android.Manifest;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.io.ByteArrayOutputStream;

import javax.inject.Inject;

import estimeet.meetup.MainApplication;
import estimeet.meetup.R;
import estimeet.meetup.interactor.ProfileInteractor;
import estimeet.meetup.ui.BaseView;

/**
 * Created by AmyDuan on 9/02/16.
 */
public class ProfilePresenter extends BasePresenter implements ProfileInteractor.ProfileListener {

    private ProfileView view;
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

        view.showProgressDialog();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        String imageString = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
        interactor.initUpdateProfile(name, imageString);
    }
    //endregion

    //region interactor callback
    @Override
    public void onError(String errorMessage) {
        dismissProgressDialog();
        //don't have to handle returned value from processerrorcode
        //only 2 error types could happen here
        processErrorCode(errorMessage, view);
    }

    @Override
    public void onAuthFailed() {
        view.onAuthFailed();
        dismissProgressDialog();
    }

    @Override
    public void onFacebookResponse(String name, String dpUri) {
        view.onReceivedFbData(name, dpUri);
    }

    @Override
    public void onUpdateProfileSuccessful() {
        view.onProfileCompleted();
        dismissProgressDialog();
    }
    //endregion

    //region logic
    private void dismissProgressDialog() {
        view.dismissProgressDialog();
    }

    //endregion
    public interface ProfileView extends BaseView {
        void startCameraAction();
        void onReceivedFbData(String name, String dpUri);
        void onProfileCompleted();
        void onInvalidName();
    }
}
