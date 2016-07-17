package estimeet.meetup.network;

import estimeet.meetup.model.BaseModel;
import estimeet.meetup.model.Friend;
import estimeet.meetup.model.ListItem;
import estimeet.meetup.model.LocationModel;
import estimeet.meetup.model.NotificationEntity;
import estimeet.meetup.model.PostModel.AddFriendModel;
import estimeet.meetup.model.PostModel.AuthUser;
import estimeet.meetup.model.PostModel.RequestLocationModel;
import estimeet.meetup.model.PostModel.SendContact;
import estimeet.meetup.model.PostModel.NotificationModel;
import estimeet.meetup.model.PostModel.UpdateModel;
import estimeet.meetup.model.Session;
import estimeet.meetup.model.TokenResponse;
import estimeet.meetup.model.User;
import estimeet.meetup.model.UserFromSearch;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by AmyDuan on 6/02/16.
 */
public interface EstimeetApi {
    @GET("/useraction/getuserdetail")
    Observable<User> getUser(
            @Query("userid") int userId);

    @GET("/signin/manualsignin")
    Observable<User> manualSignin(
            @Query("userid") int id,
            @Query("useruid") long useruid
    );

    @GET("/pauserequest")
    Observable<String> pauseRequest();

    @POST("/SignIn/SignInUser")
    Observable<User> signInUser(
            @Body AuthUser body
    );

    @POST("/Profile/buildFriendsFromContacts")
    Observable<Void> sendContacts(
        @Header("Authorization") String token,
        @Body SendContact contactModel
    );

    @FormUrlEncoded
    @POST("/estimeetauth/token")
    Observable<TokenResponse> renewToken(
            @Field("grant_type") String type,
            @Field("username") int id,
            @Field("password") String deviceId
    );

    @POST("/profile/updateuserprofile")
    Observable<User> updateProfile(
            @Header("Authorization") String token,
            @Query("isRegister") boolean register,
            @Body UpdateModel userModel
    );

    @GET("/User/getFriendsList")
    Observable<ListItem<Friend>> getFriendsList(
            @Header("Authorization") String token,
            @Query("id") int id,
            @Query("userId") long userId
    );

    @GET("/user/registerchannel")
    Observable<BaseModel> registerChannel(
            @Header("Authorization") String token,
            @Query("channeltype") String type,
            @Query("id") int id,
            @Query("userid") long userId
    );

    @GET("/user/getallnotifications")
    Observable<ListItem<NotificationEntity>> getAllNotifications(
            @Header("Authorization") String token,
            @Query("id") int id,
            @Query("userid") long userId
    );

    @POST("/user/sendRequestSessionNotification")
    Observable<Boolean> sendRequestSession(
            @Header("Authorization") String token,
            @Query("length") int length,
            @Body NotificationModel notificationModel
    );

    @GET("/user/deleteNotifications")
    Observable<Boolean> deleteNotifications(
            @Header("Authorization") String token,
            @Query("id") int id,
            @Query("userId") long userId,
            @Query("notificationId") long notificationId
    );

    @GET("/user/findfriendbyphonenumber")
    Observable<ListItem<UserFromSearch>> searchFriendByPhone(
        @Header("Authorization") String token,
        @Query("phoneNumber") String phoneNumber
    );

    @POST("/user/requestaddfriend")
    Observable<Boolean> requestAddFriend (
            @Header("Authorization") String token,
            @Body AddFriendModel addFriendModel
    );

    @POST("/session/cancelsession")
    Observable<Boolean> cancelSession(
        @Header("Authorization") String token,
        @Body NotificationModel notificationModel
    );

    @POST("/user/sendgeodata")
    Observable<Boolean> sendGeoData(
            @Header("Authorization") String token,
            @Query("data") String data,
            @Query("userUId") long userUid,
            @Query("travelMode") int travelMode,
            @Query("needNotify") boolean needNotify,
            @Body NotificationModel notificationModel
    );

    @POST("/session/createSession")
    Observable<Session> createSession(
            @Header("Authorization") String token,
            @Query("expireTimeInMinutes") int minutes,
            @Query("length") int length,
            @Body NotificationModel notificationModel
    );

    @POST("/Session/getSessionData")
    Observable<ListItem<LocationModel>> getTravelInfo(
            @Header("Authorization") String token,
            @Body RequestLocationModel requestLocationModel
    );
}
