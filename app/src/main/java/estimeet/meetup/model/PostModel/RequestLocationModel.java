package estimeet.meetup.model.PostModel;

import android.support.annotation.Keep;

import com.google.android.gms.common.annotation.KeepName;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AmyDuan on 11/04/16.
 */
public class RequestLocationModel {
    @SerializedName("userId")
    private int userId;
    @SerializedName("userUid")
    private long userUid;
    @SerializedName("friendId")
    private int friendId;
    @SerializedName("friendUid")
    private long friendUid;
    @SerializedName("sessionId")
    private int sessionId;
    @SerializedName("sessionLid")
    private long sessionLid;
    @SerializedName("travelMode")
    private int travelMode;
    @SerializedName("userGeo")
    private String userGeo;

    public RequestLocationModel(int userId, long userUid, int friendId, long friendUid, int sessionId, long sessionLid, int travelMode, String userGeo) {
        this.userId = userId;
        this.userUid = userUid;
        this.friendId = friendId;
        this.friendUid = friendUid;
        this.sessionId = sessionId;
        this.sessionLid = sessionLid;
        this.travelMode = travelMode;
        this.userGeo = userGeo;
    }
}
