package estimeet.meetup.interactor;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONObject;

import javax.inject.Inject;

import estimeet.meetup.DefaultSubscriber;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.PostModel.AuthUser;
import estimeet.meetup.model.PostModel.UpdateModel;
import estimeet.meetup.model.TokenResponse;
import estimeet.meetup.model.User;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

    public void initUpdateProfile(String token, UpdateModel user) {
        subscriber = new ProfileUpdateSubscriber(user);

        execute(serviceHelper.updateProfile(token, user), subscriber, true);
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

        private int id;
        private String password;

        public ProfileUpdateSubscriber(UpdateModel model) {
            id = model.id;
            password = model.password;
        }

        @Override
        public void onNext(User user) {
            super.onNext(user);

            sharedPreference.storeUser(user);
            listener.onUpdateProfileSuccessful();
            clearCache();
        }

        @Override
        public void onError(Throwable e) {
            if (Integer.parseInt(e.getLocalizedMessage()) == 401) {
                //auth token expired or haven't got auth token yet
                renewAuthToken(id, password, listener);

                return;
            }
            clearCache();
            listener.onError(Integer.parseInt(e.getLocalizedMessage()));
        }
    }

    public interface ProfileListener extends BaseListener {
        void onFacebookResponse(String name, String dpUri);
        void onUpdateProfileSuccessful();
    }
}
