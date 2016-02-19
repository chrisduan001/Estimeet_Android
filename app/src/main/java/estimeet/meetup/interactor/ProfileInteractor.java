package estimeet.meetup.interactor;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONObject;

import javax.inject.Inject;

import estimeet.meetup.DefaultSubscriber;
import estimeet.meetup.model.PostModel.UpdateModel;
import estimeet.meetup.model.User;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by AmyDuan on 19/02/16.
 */
public class ProfileInteractor extends BaseInteractor {

    private ProfileListener listener;
    private ProfileUpdateSubscriber profileSubscriber;

    @Inject
    public ProfileInteractor(ServiceHelper service, DataHelper data) {
        super(service, data);
    }

    //region present call
    public void call(ProfileListener listener) {
        this.listener = listener;
        profileSubscriber = new ProfileUpdateSubscriber();
    }

    public void initUpdateProfile(String token, UpdateModel user) {
        serviceHelper.updateProfile(token, user).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(profileSubscriber);
    }

    public void unSubscribe() {
        profileSubscriber.unsubscribe();
    }
    //endregion

    //region networkcall
    public void initFbRequest(AccessToken accessToken) {
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
                    listener.onFacebookResponse(name, dpUri);

                } catch (Exception e) {
                    throw new RuntimeException("Facebook parse error");
                }
            }
        }).executeAsync();
    }
    //endregion

    private static class ProfileUpdateSubscriber extends DefaultSubscriber<User> {
        @Override
        public void onNext(User user) {
            super.onNext(user);
        }

        @Override
        public void onError(Throwable e) {
        }
    }

    public interface ProfileListener extends BaseListener {
        void onFacebookResponse(String name, String dpUri);
        void onUpdateProfileSuccessful();
    }
}
