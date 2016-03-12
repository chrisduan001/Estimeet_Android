package estimeet.meetup.model.PostModel;

/**
 * Created by AmyDuan on 12/03/16.
 */
public class SendContact {
    private int id;
    private long userId;
    private String contacts;

    public SendContact(int id, long userId, String contacts) {
        this.id = id;
        this.userId = userId;
        this.contacts = contacts;
    }
}
