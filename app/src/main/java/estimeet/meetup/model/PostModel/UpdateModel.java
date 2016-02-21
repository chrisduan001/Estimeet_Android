package estimeet.meetup.model.PostModel;
/**
 * Created by AmyDuan on 18/02/16.
 */
public class UpdateModel {

    private long userId;
    private String password;
    public String name;
    private byte[] image;

    public UpdateModel(){}

    public UpdateModel(long userId, String password, String name, byte[] image) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.image = image;
    }
}
