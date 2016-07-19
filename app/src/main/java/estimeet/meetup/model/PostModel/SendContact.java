package estimeet.meetup.model.PostModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AmyDuan on 12/03/16.
 */
public class SendContact {
    @SerializedName("id")
    private int id;
    @SerializedName("userId")
    private long userId;
    @SerializedName("contacts")
    private String contacts;

    public SendContact(int id, long userId, String contacts) {
        this.id = id;
        this.userId = userId;
        this.contacts = contacts;
    }
}
