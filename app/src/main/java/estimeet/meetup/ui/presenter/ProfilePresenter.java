package estimeet.meetup.ui.presenter;

import android.Manifest;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONArray;
import org.json.JSONObject;

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

    public void registerCallBack(CallbackManager callbackManager) {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                initFbRequest(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }
    //endregion

    //region facebook
    private void initFbRequest(AccessToken accessToken) {
        Bundle bundle = new Bundle();
        bundle.putString("fields", "id,name,picture");

        new GraphRequest(accessToken, "me", bundle, HttpMethod.GET, new GraphRequest.Callback() {

            @Override
            public void onCompleted(GraphResponse response) {
                try {
                    JSONObject mainObj = response.getJSONObject();
                    String id = response.getJSONObject().getString("id");
                    String dpUri = "https://graph.facebook.com/" + id + "/picture?type=large";
                    String name = mainObj.getString("name");
                    view.onReceivedFbData(name, dpUri);

                } catch (Exception e) {
                    throw new RuntimeException("Facebook parse error");
                }
            }
        }).executeAsync();
    }
    //endregion

    public interface ProfileView extends BaseView {
        void startCameraAction();
        void onReceivedFbData(String name, String dpUri);
    }
}
