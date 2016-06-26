package estimeet.meetup.network;

import javax.inject.Inject;
import javax.inject.Singleton;

import estimeet.meetup.model.BaseModel;
import estimeet.meetup.model.Friend;
import estimeet.meetup.model.ListItem;
import estimeet.meetup.model.LocationModel;
import estimeet.meetup.model.NotificationEntity;
import estimeet.meetup.model.PostModel.AuthUser;
import estimeet.meetup.model.PostModel.RequestLocationModel;
import estimeet.meetup.model.PostModel.SendContact;
import estimeet.meetup.model.PostModel.NotificationModel;
import estimeet.meetup.model.PostModel.UpdateModel;
import estimeet.meetup.model.Session;
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

    public Observable<User> manualSignin(int id, long uid) {
        return estimeetApi.manualSignin(id, uid);
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

    public Observable<User> updateProfile(String token, boolean register, UpdateModel model) {
        return estimeetApi.updateProfile(buildToken(token), register, model);
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

    public Observable<Boolean> sendSessionRequest(String token, int length, NotificationModel request) {
        return estimeetApi.sendRequestSession(buildToken(token), length, request);
    }

    public Observable<Boolean> deleteNotifications(String token, int id, long userId, long notificationId) {
        return estimeetApi.deleteNotifications(buildToken(token), id, userId, notificationId);
    }

    public Observable<Boolean> cancelSession(String token, NotificationModel request) {
        return estimeetApi.cancelSession(buildToken(token), request);
    }

    //need notify part is not implemented yet
    public Observable<Boolean> sendGeodata(String token, String geoData, long userUid, int travelMode, NotificationModel model) {
        return estimeetApi.sendGeoData(buildToken(token), geoData, userUid, travelMode, false, model);
    }

    public Observable<Session> createSession(String token, int expireInMinutes, int length, NotificationModel model) {
        return estimeetApi.createSession(buildToken(token), expireInMinutes, length, model);
    }

    public Observable<ListItem<LocationModel>> getTravelInfo(String token, RequestLocationModel model) {
        return estimeetApi.getTravelInfo(buildToken(token), model);
    }

    private String buildToken(String token) {
        return "Bearer " + token;
    }
}
