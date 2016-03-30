package estimeet.meetup.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AmyDuan on 30/03/16.
 */
public class NotificationEntity extends BaseModel {
    @SerializedName("notificationId")
    public int notificationId;

    @SerializedName("identifier")
    public int identifier;

    @SerializedName("appendix")
    public String appendix;

    @SerializedName("senderId")
    public int senderId;

    @SerializedName("receiverId")
    private int receiverId;

    @SerializedName("receiverUId")
    private int receiverUId;

}
