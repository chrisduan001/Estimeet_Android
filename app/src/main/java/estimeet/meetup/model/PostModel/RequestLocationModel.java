package estimeet.meetup.model.PostModel;

/**
 * Created by AmyDuan on 11/04/16.
 */
public class RequestLocationModel {
    private int userId;
    private long userUid;
    private int friendId;
    private long friendUid;
    private int sessionId;
    private long sessionLid;
    private int travelMode;
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
