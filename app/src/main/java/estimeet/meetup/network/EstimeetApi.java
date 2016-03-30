package estimeet.meetup.network;

import estimeet.meetup.model.BaseModel;
import estimeet.meetup.model.Friend;
import estimeet.meetup.model.ListItem;
import estimeet.meetup.model.NotificationEntity;
import estimeet.meetup.model.PostModel.AuthUser;
import estimeet.meetup.model.PostModel.SendContact;
import estimeet.meetup.model.PostModel.UpdateModel;
import estimeet.meetup.model.TokenResponse;
import estimeet.meetup.model.User;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by AmyDuan on 6/02/16.
 */
public interface EstimeetApi {
    @GET("/useraction/getuserdetail")
    Observable<User> getUser(
            @Query("userid") int userId);

    @GET("/pauserequest")
    Observable<String> pauseRequest();

    @POST("/SignIn/SignInUser")
    Observable<User> signInUser(
            @Body AuthUser body
    );

    @POST("/Profile/buildFriendsFromContacts")
    Observable<User> sendContacts(
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
}
