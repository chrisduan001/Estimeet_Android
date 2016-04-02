package estimeet.meetup.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AmyDuan on 2/04/16.
 */
public class Session extends BaseModel {
    @SerializedName("SessionId")
    public int sessionId;

    @SerializedName("SessionLId")
    public long sessionLId;
}
