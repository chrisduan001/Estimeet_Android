package estimeet.meetup.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import estimeet.meetup.R;
import estimeet.meetup.ui.presenter.BasePresenter;
import estimeet.meetup.util.CircleTransform;

/**
 * Created by AmyDuan on 26/06/16.
 */
public abstract class DpBaseFragment extends BaseFragment {

    protected static final int CAPTURE_IMAGE_CODE = 100;
    protected static final int CROP_IMAGE_CODE = 101;

    @ViewById(R.id.progress_bar)        ProgressBar progressBar;
    @ViewById(R.id.profile_image)       ImageView profileImage;

    @Inject Picasso picasso;
    @Inject CircleTransform circleTransform;

    //region lifecycle & view
    @Override
    protected BasePresenter getPresenter() {
        return getSubclassPresenter();
    }

    @Override
    protected ProgressBar getProgressBar() {
        return progressBar;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initInjector();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
        }
    }

    protected void startShowProgress() {
        showProgressDialog(getString(R.string.progress_loading));
    }

    protected void showPickImage() {
        Crop.pickImage(getContext(), this, CAPTURE_IMAGE_CODE);
    }

    protected abstract BasePresenter getSubclassPresenter();
    protected abstract void initInjector();
    //endregion

    //crop image action
    private Uri createTempFileUri() throws IOException {
        return Uri.fromFile(File.createTempFile("IMG_" + System.currentTimeMillis(), ".jpg"));
    }
}
