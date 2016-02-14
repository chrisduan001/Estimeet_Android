package estimeet.meetup.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.inject.Inject;
import estimeet.meetup.R;
import estimeet.meetup.di.components.MainComponent;
import estimeet.meetup.di.components.SignInComponent;
import estimeet.meetup.ui.presenter.BasePresenter;
import estimeet.meetup.ui.presenter.ProfilePresenter;
import estimeet.meetup.util.CircleTransform;

/**
 * Created by AmyDuan on 9/02/16.
 */
@EFragment(R.layout.fragment_profile)
public class ProfileFragment extends BaseFragment implements ProfilePresenter.ProfileView {

    @Inject ProfilePresenter presenter;
    @Inject Picasso picasso;
    @Inject CircleTransform circleTransform;

    @ViewById(R.id.profile_image)       ImageView profileImage;
    @ViewById(R.id.et_user_name)        EditText userNameEt;

    private static final int CAPTURE_IMAGE_CODE = 100;
    private static final int CROP_IMAGE_CODE = 101;
    private static final int FACEBOOK_LOGIN_CODE = 200;

    CallbackManager callbackManager;

    //region lifecycle

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getContext(), FACEBOOK_LOGIN_CODE);
        return null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();

        presenter.setView(this);

        picasso.load(R.drawable.download).resize(300, 300).centerCrop()
                .transform(circleTransform).into(profileImage);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CAPTURE_IMAGE_CODE: {
                    try {
                        Crop.of(data.getData(), createTempFileUri()).asSquare().start(getContext(),
                                this, CROP_IMAGE_CODE);
                    } catch (IOException e) {
                        showShortToastMessage(getString(R.string.error_create_temp_file));
                    }
                    break;
                }

                case CROP_IMAGE_CODE: {
                    Bundle bundle = data.getExtras();
                    try {
                        //noinspection ConstantConditions
                        picasso.load(Uri.parse(bundle.get("output").toString())).resize(300,300)
                                .centerCrop().transform(circleTransform).into(profileImage);
                    } catch (Exception e) {
                        showShortToastMessage(getString(R.string.error_unable_to_crop_image));
                    }
                    break;
                }

                case FACEBOOK_LOGIN_CODE: {
                    callbackManager.onActivityResult(requestCode, resultCode, data);
                }

            }
        }
    }

    private void initialize() {
        getComponent(SignInComponent.class).inject(this);
    }
    //endregion

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }

    //region button click
    @Click(R.id.profile_image)
    protected void profileImageClicked() {
        presenter.intentToStartCamera();
    }

    @Click(R.id.fb_login_display_button)
    protected void fbButtonClicked() {
        setupFacebookAction();
        presenter.registerCallBack(callbackManager);
    }
    //endregion

    //region presenter callback
    @Override
    public void startCameraAction() {
        Crop.pickImage(getContext(), this, CAPTURE_IMAGE_CODE);
    }

    @Override
    public void onReceivedFbData(String name, String dpUri) {
        userNameEt.setText(name);
        picasso.load(dpUri).resize(300, 300).centerCrop().transform(circleTransform).into(profileImage);
    }

    //endregion

    //region facebook action
    private void setupFacebookAction() {
        //noinspection ArraysAsListWithZeroOrOneArgument
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        callbackManager = CallbackManager.Factory.create();
    }

    //endregion

    private Uri createTempFileUri() throws IOException {
        return Uri.fromFile(File.createTempFile("IMG_" + System.currentTimeMillis(), ".jpg"));
    }
}
