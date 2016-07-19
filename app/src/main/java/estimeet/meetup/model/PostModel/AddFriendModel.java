package estimeet.meetup.model.PostModel;

import android.support.annotation.Keep;

import com.google.android.gms.common.annotation.KeepName;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AmyDuan on 16/07/16.
 */
public class AddFriendModel {
    @SerializedName("senderId")
    private int senderId;
    @SerializedName("senderUid")
    private long senderUid;
    @SerializedName("friendId")
    private int friendId;
    @SerializedName("friendUid")
    private long friendUid;

    public AddFriendModel(int senderId, long senderUid, int friendId, long friendUid) {
        this.senderId = senderId;
        this.senderUid = senderUid;
        this.friendId = friendId;
        this.friendUid = friendUid;
    }
}
