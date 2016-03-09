package estimeet.meetup.interactor;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONObject;

import javax.inject.Inject;

import estimeet.meetup.DefaultSubscriber;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.PostModel.UpdateModel;
import estimeet.meetup.model.User;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;

/**
 * Created by AmyDuan on 19/02/16.
 */
public class ProfileInteractor extends BaseInteractor<User> {

    private ProfileListener listener;
    private ProfileUpdateSubscriber subscriber;


    @Inject
    public ProfileInteractor(ServiceHelper service, DataHelper data, MeetUpSharedPreference sp) {
        super(service, data, sp);
    }

    //region present call
    public void call(final ProfileListener listener) {
        this.listener = listener;
    }

    public void initUpdateProfile(String name, String imageString) {
        subscriber = new ProfileUpdateSubscriber();

        User user = sharedPreference.getUserFromSp();

        UpdateModel updateModel = new UpdateModel(user.id, user.userId, name, imageString);
        makeRequest(user, serviceHelper.updateProfile(user.token, updateModel), subscriber);
    }

    public void unSubscribe() {
        if (subscriber != null && !subscriber.isUnsubscribed()) {
            subscriber.unsubscribe();
        }
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

    private class ProfileUpdateSubscriber extends DefaultSubscriber<User> {

        @Override
        public void onNext(User user) {
            //super.onnext checks if there is any error with model, if there is throw an error with error code
            super.onNext(user);

            sharedPreference.updateUserProfile(user.userName, user.dpUri);
            listener.onUpdateProfileSuccessful();
        }

        @Override
        public void onError(Throwable e) {
            if (e.getLocalizedMessage().equals("401")) {
                listener.onAuthFailed();
                sharedPreference.removeSharedPreference();
                return;
            }

            listener.onError(e.getLocalizedMessage());
        }
    }

    public interface ProfileListener extends BaseListener {
        void onFacebookResponse(String name, String dpUri);
        void onUpdateProfileSuccessful();
    }
}
