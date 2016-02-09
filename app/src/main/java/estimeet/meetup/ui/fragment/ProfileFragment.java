package estimeet.meetup.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

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

    //region lifecycle

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();

        presenter.setView(this);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.download);
        RoundedBitmapDrawable image = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        image.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight())/2);
        profileImage.setImageDrawable(image);
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
        presenter.takePhoto();
    }
    //endregion
}
