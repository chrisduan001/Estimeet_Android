package estimeet.meetup.network;

import javax.inject.Inject;
import javax.inject.Singleton;

import estimeet.meetup.model.PostModel.AuthUser;
import estimeet.meetup.model.PostModel.UpdateModel;
import estimeet.meetup.model.TokenResponse;
import estimeet.meetup.model.User;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by AmyDuan on 25/01/16.
 */
@Singleton
public class ServiceHelper {

    private final EstimeetApi estimeetApi;

    @Inject
    public ServiceHelper(EstimeetApi restApi) {
        this.estimeetApi = restApi;
    }

    public Observable<User> getUser(int userId) {
        return estimeetApi.getUser(userId);
    }

    public Observable<String> pauseRequest() {
        return estimeetApi.pauseRequest();
    }

    public Observable<User> signInUser(AuthUser authUser) {
        return estimeetApi.signInUser(authUser);
    }

    public Observable<TokenResponse> renewToken(int id, String deviceId) {
        return estimeetApi.renewToken("password", id, deviceId);
    }

    public Observable<User> updateProfile(String token, UpdateModel model) {
        return estimeetApi.updateProfile(token, model);
    }
}
