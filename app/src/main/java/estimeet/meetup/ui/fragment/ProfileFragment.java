package estimeet.meetup.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Callback;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import java.util.Arrays;
import javax.inject.Inject;
import estimeet.meetup.R;
import estimeet.meetup.di.components.SignInComponent;
import estimeet.meetup.ui.presenter.BasePresenter;
import estimeet.meetup.ui.presenter.ProfilePresenter;

/**
 * Created by AmyDuan on 9/02/16.
 */
@EFragment(R.layout.fragment_profile)
public class ProfileFragment extends DpBaseFragment implements ProfilePresenter.ProfileView {

    public interface SignInCallback {
        void onGetStarted();
        void onAuthFailed();
        void navToFriendList();
    }

    @Inject ProfilePresenter presenter;

    @ViewById(R.id.et_user_name)        EditText userNameEt;

    private static final int FACEBOOK_LOGIN_CODE = 200;

    private CallbackManager callbackManager;
    private SignInCallback signInCallback;

    //region lifecycle & view
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SignInCallback) {
            this.signInCallback = (SignInCallback) context;
        } else {
            throw new UnsupportedOperationException("Activity must implement " +
                    SignInCallback.class.getSimpleName());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getContext(), FACEBOOK_LOGIN_CODE);
        return null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        presenter.setView(this);

        picasso.load(R.drawable.download).resize(300, 300).centerCrop()
                .transform(circleTransform).into(profileImage);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == FACEBOOK_LOGIN_CODE) {
                callbackManager.onActivityResult(requestCode, resultCode, data);
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    protected void initInjector() {
        getComponent(SignInComponent.class).inject(this);
    }

    @Override
    protected BasePresenter getSubclassPresenter() {
        return presenter;
    }
    //endregion

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

    @Click(R.id.btn_get_started)
    protected void getStartButtonClicked() {
        presenter.onUpdateProfile(userNameEt.getText().toString(),
                ((BitmapDrawable) profileImage.getDrawable()).getBitmap());
    }

    @Click(R.id.et_user_name)
    protected void onUserNameSelected() {
        userNameEt.setError(null);
    }
    //endregion

    //region presenter callback
    @Override
    public void onAuthFailed() {
        super.onAuthFailed();
        signInCallback.onAuthFailed();
    }

    @Override
    public void showProgressDialog() {
        startShowProgress();
    }

    @Override
    public void startCameraAction() {
        showPickImage();
    }

    @Override
    public void onReceivedFbData(String name, String dpUri) {
        userNameEt.setText(name);
        userNameEt.setError(null);

        showProgressDialog(getString(R.string.progress_loading));
        picasso.load(dpUri).resize(300, 300).centerCrop().transform(circleTransform).into(profileImage, new Callback() {
            @Override
            public void onSuccess() {
                dismissProgressDialog();
            }

            @Override
            public void onError() {
                dismissProgressDialog();
            }
        });
    }

    @Override
    public void onProfileCompleted() {
        signInCallback.onGetStarted();
    }

    @Override
    public void onInvalidName() {
        userNameEt.setError(getString(R.string.error_invialid_name));
    }

    @Override
    public void onNonEmptyFriendList() {
        signInCallback.navToFriendList();
    }

    //endregion

    //region facebook action
    private void setupFacebookAction() {
        //noinspection ArraysAsListWithZeroOrOneArgument
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        callbackManager = CallbackManager.Factory.create();
    }
    //endregion
}
