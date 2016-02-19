package estimeet.meetup.network;

import estimeet.meetup.model.PostModel.AuthUser;
import estimeet.meetup.model.PostModel.UpdateModel;
import estimeet.meetup.model.User;
import retrofit.http.Body;
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

    @GET("/pauserequest")
    Observable<String> pauseRequest();

    @POST("/SignIn/SignInUser")
    Observable<User> signInUser(
            @Body AuthUser body
    );

    @POST("/SignIn/UpdateProfile")
    Observable<User> updateProfile(
            @Header("Authorization") String token,
            @Body UpdateModel userModel
    );
}
