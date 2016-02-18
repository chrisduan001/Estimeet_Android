package estimeet.meetup.model.PostModel;

/**
 * Created by AmyDuan on 18/02/16.
 */
public class UserUpdate {

    private long userId;
    private String password;
    private String name;
    private byte[] image;

    public UserUpdate(){}

    public UserUpdate(long userId, String password, String name, byte[] image) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.image = image;
    }
}
