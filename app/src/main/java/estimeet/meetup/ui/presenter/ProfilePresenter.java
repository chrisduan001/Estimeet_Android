package estimeet.meetup.ui.presenter;

import javax.inject.Inject;

import estimeet.meetup.ui.BaseView;
import estimeet.meetup.ui.PermissionType;

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
    public void onRequestPermissionCallback(boolean isGranted) {
        if (isGranted) {
            view.showShortToastMessage("Camera opening");
        } else {
            view.showShortToastMessage("Permission denied");
        }
    }

    //endregion

    //region fragment call
    public void setView(ProfileView view) {
        this.view = view;
    }

    public void takePhoto() {
        view.checkPermission(PermissionType.CAMERA);
    }
    //endregion

    public interface ProfileView extends BaseView {

    }
}
