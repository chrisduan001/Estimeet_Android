package estimeet.meetup.model.PostModel;

/**
 * Created by AmyDuan on 15/02/16.
 */
public class AuthUser {
    private String authHeader;
    private String authUri;
    private String phoneNumber;
    private long userId;

    public AuthUser(){}

    public AuthUser(String authHeader, String authUri, String phoneNumber, long userId) {
        this.authHeader = authHeader;
        this.authUri = authUri;
        this.phoneNumber = phoneNumber;
        this.userId = userId;
    }
}
