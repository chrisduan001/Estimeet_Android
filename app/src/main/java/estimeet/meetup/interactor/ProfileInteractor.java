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
import rx.Observable;

/**
 * Created by AmyDuan on 19/02/16.
 */
public class ProfileInteractor extends BaseInteractor<User> {

    private ProfileListener listener;
    private ProfileUpdateSubscriber subscriber;

    private String imageString;

    @Inject
    public ProfileInteractor(ServiceHelper service, DataHelper data, MeetUpSharedPreference sp) {
        super(service, data, sp);
    }

    //region present call
    public void call(final ProfileListener listener) {
        this.listener = listener;
    }

    public void initUpdateProfile(String name, String imageString) {
        this.imageString = imageString;
        subscriber = new ProfileUpdateSubscriber();
        baseUser = sharedPreference.getUserFromSp();
        baseUser.userName = name;
        makeRequest(subscriber, true);
    }

    public void unSubscribe() {
        if (subscriber != null && !subscriber.isUnsubscribed()) {
            subscriber.unsubscribe();
        }
    }
    //endregion

    @Override
    protected Observable<User> getObservable() {
        UpdateModel updateModel = new UpdateModel(baseUser.id, baseUser.userId, baseUser.userName, imageString);
        return serviceHelper.updateProfile(baseUser.token, updateModel);
    }

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
            super.onError(e);
        }

        @Override
        protected void onAuthError() {
            sharedPreference.removeSharedPreference();
            listener.onAuthFailed();
        }

        @Override
        protected void onError(String err) {
            listener.onError(err);
        }
    }

    public interface ProfileListener extends BaseListener {
        void onFacebookResponse(String name, String dpUri);
        void onUpdateProfileSuccessful();
    }
}
