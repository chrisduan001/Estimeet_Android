package estimeet.meetup.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AmyDuan on 12/03/16.
 */
public class Friends extends BaseModel {
    @SerializedName("id")
    private int id;

    @SerializedName("userId")
    private int userId;

    @SerializedName("userName")
    private int userName;

    @SerializedName("dpUri")
    private int dpUri;
}
