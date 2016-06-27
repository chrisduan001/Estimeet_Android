package estimeet.meetup.ui.presenter;

import android.Manifest;
import android.graphics.Bitmap;
import android.text.TextUtils;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import estimeet.meetup.interactor.ProfileInteractor;
import estimeet.meetup.ui.BaseView;

/**
 * Created by AmyDuan on 20/03/16.
 */
public class ManageProfilePresenter extends BasePresenter implements ProfileInteractor.ProfileListener {

    private WeakReference<ManageProfileView> view;

    private ProfileInteractor profileInteractor;

    //region lifecycle & view
    @Inject
    public ManageProfilePresenter(ProfileInteractor profileInteractor) {
        this.profileInteractor = profileInteractor;
        this.profileInteractor.call(this);
    }

    @Override
    public void onPause() {
        profileInteractor.unSubscribe();
    }

    @Override
    public void onPermissionResult(boolean isGranted) {
        if (isGranted) {
            view.get().startCameraAction();
        }
    }
    //endregion

    //region fragment call
    public void setView(ManageProfileView view) {
        this.view = new WeakReference<>(view);
    }

    public void intentToStartCamera() {
        view.get().checkPermission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public void onUpdateProfile(String name, Bitmap bitmap) {
        if (!TextUtils.isEmpty(name) && bitmap != null) {
            view.get().showProgressDialog();
            profileInteractor.initUpdateProfile(name, bitmap, false);
        }
    }

    public void loadUserDp(String dpUri) {
        profileInteractor.getUserDpString(dpUri);
    }
    //endregion

    //region interactor callback
    @Override
    public void onError(String errorMessage) {
        dismissProgressDialog();
        view.get().onError(errorMessage);
    }

    @Override
    public void onUpdateProfileSuccessful() {
        dismissProgressDialog();
        view.get().onProfileUpdated();
    }

    @Override
    public void onGetUserDp(Bitmap bitmap) {
        view.get().setUserDp(bitmap);
    }
    //not implemented for this class
    @Override
    public void onAuthFailed() {}
    //not implemented for this class
    @Override
    public void onFacebookResponse(String name, String dpUri) {}
    //endregion

    private void dismissProgressDialog() {
        view.get().dismissProgressDialog();
    }

    public interface ManageProfileView extends BaseView {
        void startCameraAction();
        void onProfileUpdated();
        void setUserDp(Bitmap bitmap);
    }
}
