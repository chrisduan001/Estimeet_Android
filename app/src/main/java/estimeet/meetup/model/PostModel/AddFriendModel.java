package estimeet.meetup.model.PostModel;

/**
 * Created by AmyDuan on 16/07/16.
 */
public class AddFriendModel {

    private int senderId;
    private long senderUid;
    private int friendId;
    private long friendUid;

    public AddFriendModel(int senderId, long senderUid, int friendId, long friendUid) {
        this.senderId = senderId;
        this.senderUid = senderUid;
        this.friendId = friendId;
        this.friendUid = friendUid;
    }
}
