package estimeet.meetup.model.PostModel;

/**
 * Created by AmyDuan on 4/04/16.
 */
public class SessionRequest {
    private int senderId;
    private int receiverId;
    private long receiverUId;

    public SessionRequest(int senderId, int receiverId, long receiverUId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.receiverUId = receiverUId;
    }
}
