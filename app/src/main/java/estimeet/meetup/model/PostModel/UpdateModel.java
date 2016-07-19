package estimeet.meetup.model.PostModel;


import com.google.android.gms.common.annotation.KeepName;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AmyDuan on 18/02/16.
 */
public class UpdateModel {
    @SerializedName("id")
    public int id;
    @SerializedName("userId")
    public long userId;
    @SerializedName("userName")
    private String userName;
    @SerializedName("imageString")
    private String imageString;

    public UpdateModel(){}

    public UpdateModel(int id, long userId, String name, String imageString) {
        this.id = id;
        this.userId = userId;
        this.userName = name;
        this.imageString = imageString;
    }
}
