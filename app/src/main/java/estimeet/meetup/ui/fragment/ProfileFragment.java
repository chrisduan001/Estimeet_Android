package estimeet.meetup.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;
import com.soundcloud.android.crop.Crop;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import java.io.File;
import java.io.IOException;
import javax.inject.Inject;
import estimeet.meetup.R;
import estimeet.meetup.di.components.MainComponent;
import estimeet.meetup.ui.presenter.BasePresenter;
import estimeet.meetup.ui.presenter.ProfilePresenter;

/**
 * Created by AmyDuan on 9/02/16.
 */
@EFragment(R.layout.fragment_profile)
public class ProfileFragment extends BaseFragment implements ProfilePresenter.ProfileView {

    @Inject ProfilePresenter presenter;
    @ViewById(R.id.profile_image) ImageView profileImage;

    private static final int CAPTURE_IMAGE_CODE = 100;
    private static final int CROP_IMAGE_CODE = 101;

    //region lifecycle
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();

        presenter.setView(this);

        setRoundImage(BitmapFactory.decodeResource(getResources(), R.drawable.download));
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
                        @SuppressWarnings("ConstantConditions")
                        Bitmap dd = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),
                                Uri.parse(bundle.get("output").toString()));
                        setRoundImage(dd);
                    } catch (Exception e) {
                        showShortToastMessage(getString(R.string.error_unable_to_crop_image));
                    }
                    break;
                }
            }
        }
    }

    private void initialize() {
        getComponent(MainComponent.class).inject(this);
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
    //endregion

    //region presenter callback
    @Override
    public void setUserPhoto(Bitmap bitmap) {
        setRoundImage(bitmap);
    }

    @Override
    public void startCameraAction() {
        Crop.pickImage(getContext(), this, CAPTURE_IMAGE_CODE);
    }
    //endregion

    private void setRoundImage(Bitmap bitmap) {
        RoundedBitmapDrawable image = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        image.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight())/2);
        profileImage.setImageDrawable(image);
    }

    private Uri createTempFileUri() throws IOException {
        return Uri.fromFile(File.createTempFile("IMG_" + System.currentTimeMillis(), ".jpg"));
    }
}
