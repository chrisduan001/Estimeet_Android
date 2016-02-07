package estimeet.meetup.network;

import estimeet.meetup.model.User;
import retrofit.http.GET;
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
}
