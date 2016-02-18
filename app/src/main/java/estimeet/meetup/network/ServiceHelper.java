package estimeet.meetup.network;

import javax.inject.Inject;
import javax.inject.Singleton;

import estimeet.meetup.model.PostModel.AuthUser;
import estimeet.meetup.model.User;
import rx.Observable;

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
}
