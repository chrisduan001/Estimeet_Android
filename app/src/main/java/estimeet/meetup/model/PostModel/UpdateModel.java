package estimeet.meetup.model.PostModel;
/**
 * Created by AmyDuan on 18/02/16.
 */
public class UpdateModel {

    public int id;
    public long userId;
    public String password;
    private String userName;
    private String imageString;

    public UpdateModel(){}

    public UpdateModel(int id, long userId, String password, String name, String imageString) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.userName = name;
        this.imageString = imageString;
    }
}
