package estimeet.meetup.model.PostModel;
/**
 * Created by AmyDuan on 18/02/16.
 */
public class UpdateModel {

    public int id;
    public long userId;
    public String password;
    private String name;
    private byte[] image;

    public UpdateModel(){}

    public UpdateModel(int id, long userId, String password, String name, byte[] image) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.image = image;
    }
}
