package estimeet.meetup.model.PostModel;

import android.support.annotation.Keep;

import com.google.android.gms.common.annotation.KeepName;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AmyDuan on 4/04/16.
 */

public class NotificationModel {
    @SerializedName("senderId")
    private int senderId;
    @SerializedName("receiverId")
    private int receiverId;
    @SerializedName("receiverUId")
    private long receiverUId;

    public NotificationModel(int senderId, int receiverId, long receiverUId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.receiverUId = receiverUId;
    }
}
