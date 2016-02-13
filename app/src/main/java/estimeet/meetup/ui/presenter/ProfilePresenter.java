package estimeet.meetup.ui.presenter;

import android.Manifest;
import android.graphics.Bitmap;

import javax.inject.Inject;

import estimeet.meetup.ui.BaseView;

/**
 * Created by AmyDuan on 9/02/16.
 */
public class ProfilePresenter extends BasePresenter {

    private ProfileView view;

    @Inject
    public ProfilePresenter() {
    }

    //region fragment callback
    @Override
    public void onResume() {

    }

    @Override
    public void onPermissionResult(boolean isGranted) {
        if (isGranted) {
            view.startCameraAction();
        }
    }

    //endregion

    //region fragment call
    public void setView(ProfileView view) {
        this.view = view;
    }

    public void intentToStartCamera() {
        view.checkPermission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
    //endregion

    public interface ProfileView extends BaseView {
        void setUserPhoto(Bitmap bitmap);
        void startCameraAction();
    }
}
