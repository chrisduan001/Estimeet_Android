package estimeet.meetup.model;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.microsoft.windowsazure.messaging.NotificationHub;

import javax.inject.Named;

/**
 * Created by AmyDuan on 25/03/16.
 */
public class PushModel {
    private GoogleCloudMessaging gcm;
    private NotificationHub hub;
    private String userId;
    private String senderId;
    private int versionCode;

    public PushModel(GoogleCloudMessaging gcm, NotificationHub hub, String userId, String senderId, int versionCode) {
        this.gcm = gcm;
        this.hub = hub;
        this.userId = userId;
        this.senderId = senderId;
        this.versionCode = versionCode;
    }

    public GoogleCloudMessaging getGcm() {
        return gcm;
    }

    public NotificationHub getHub() {
        return hub;
    }

    public String getUserId() {
        return userId;
    }

    public String getSenderId() {
        return senderId;
    }

    public int getVersionCode() {
        return versionCode;
    }
}
