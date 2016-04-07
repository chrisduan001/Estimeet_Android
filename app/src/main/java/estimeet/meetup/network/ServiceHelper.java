package estimeet.meetup.network;

import javax.inject.Inject;
import javax.inject.Singleton;

import estimeet.meetup.model.BaseModel;
import estimeet.meetup.model.Friend;
import estimeet.meetup.model.ListItem;
import estimeet.meetup.model.NotificationEntity;
import estimeet.meetup.model.PostModel.AuthUser;
import estimeet.meetup.model.PostModel.SendContact;
import estimeet.meetup.model.PostModel.SessionRequest;
import estimeet.meetup.model.PostModel.UpdateModel;
import estimeet.meetup.model.TokenResponse;
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

    public Observable<User> signInUser(AuthUser authUser) {
        return estimeetApi.signInUser(authUser);
    }

    public Observable<User> sendContacts(String token, SendContact contactModel) {
        return estimeetApi.sendContacts(buildToken(token), contactModel);
    }

    public Observable<TokenResponse> renewToken(int id, String deviceId) {
        return estimeetApi.renewToken("password", id, deviceId);
    }

    public Observable<User> updateProfile(String token, UpdateModel model) {
        return estimeetApi.updateProfile(buildToken(token), model);
    }

    public Observable<ListItem<Friend>> getFriendsList(String token, int id, long userId) {
        return estimeetApi.getFriendsList(buildToken(token), id, userId);
    }

    public Observable<BaseModel> registerChannel(String token, int id, long userId) {
        return estimeetApi.registerChannel(buildToken(token), "gcm", id, userId);
    }

    public Observable<ListItem<NotificationEntity>> getAllNotifications(String token, int id, long userId) {
        return estimeetApi.getAllNotifications(buildToken(token), id, userId);
    }

    public Observable<Boolean> sendSessionRequest(String token, int length, SessionRequest request) {
        return estimeetApi.sendRequestSession(buildToken(token), length, request);
    }

    public Observable<Boolean> deleteNotifications(String token, int id, long userId, long notificationId) {
        return estimeetApi.deleteNotifications(buildToken(token), id, userId, notificationId);
    }

    public Observable<Boolean> cancelSession(String token, SessionRequest request) {
        return estimeetApi.cancelSession(buildToken(token), request);
    }

    private String buildToken(String token) {
        return "Bearer " + token;
    }
}
