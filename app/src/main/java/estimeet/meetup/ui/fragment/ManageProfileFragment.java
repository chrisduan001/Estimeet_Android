package estimeet.meetup.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;
import javax.inject.Named;

import estimeet.meetup.R;
import estimeet.meetup.di.components.MainComponent;
import estimeet.meetup.di.components.ManageProfileComponent;
import estimeet.meetup.model.User;
import estimeet.meetup.ui.presenter.BasePresenter;
import estimeet.meetup.ui.presenter.ManageProfilePresenter;

/**
 * Created by AmyDuan on 20/03/16.
 */
@EFragment(R.layout.fragment_manage_profile)
public class ManageProfileFragment extends DpBaseFragment implements ManageProfilePresenter.ManageProfileView {

    @Inject ManageProfilePresenter presenter;
    @Inject @Named("currentUser") User user;

    @ViewById(R.id.profile_name) TextView profileName;
    @ViewById(R.id.profile_id) TextView profileId;
    @ViewById(R.id.profile_mobile_number) TextView profileMobile;

    private ManageProfileCallback manageProfileCallback;

    public interface ManageProfileCallback {
        void onUserDpChanged();
        void onUserDpSaved();
    }
    //region activity
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ManageProfileCallback) {
            manageProfileCallback = (ManageProfileCallback) context;
        } else {
            throw new UnsupportedOperationException("Activity must implement " +
                ManageProfileCallback.class.getSimpleName());
        }
    }

    public void onSaveClicked() {
        presenter.onUpdateProfile(user.userName,
                ((BitmapDrawable) profileImage.getDrawable()).getBitmap());
    }
    //endregion

    //region lifecycle
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        presenter.setView(this);

        initProfileData();

        loadUserDp();
    }

    @Override
    protected void initInjector() {
        getComponent(ManageProfileComponent.class).inject(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == CROP_IMAGE_CODE) {
                manageProfileCallback.onUserDpChanged();
            }
        }
    }

    private void initProfileData() {
        profileName.setText("Hi, "+ user.userName + "!");
        profileMobile.setText(user.phoneNumber);
    }

    @Background
    protected void loadUserDp() {
        presenter.loadUserDp(user.dpUri);
    }

    @Override
    protected BasePresenter getSubclassPresenter() {
        return presenter;
    }
    //endregion

    //region presenter call
    @Override
    public void showProgressDialog() {
        startShowProgress();
    }

    @Override
    public void startCameraAction() {
        showPickImage();
    }

    @Override
    public void onProfileUpdated() {
        manageProfileCallback.onUserDpSaved();
    }

    @UiThread
    @Override
    public void setUserDp(Bitmap bitmap) {
        if (bitmap != null) {
            profileImage.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onAuthFailed() {
        getActivity().finish();
    }

    //endregion

    //region button click event
    @Click(R.id.profile_image)
    protected void profileImageClicked() {
        presenter.intentToStartCamera();
    }
    //endregion
}
