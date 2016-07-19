package estimeet.meetup.model.PostModel;

import android.support.annotation.Keep;

import com.google.android.gms.common.annotation.KeepName;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AmyDuan on 15/02/16.
 */

public class AuthUser {
    @SerializedName("authHeader")
    private String authHeader;
    @SerializedName("authUri")
    private String authUri;
    @SerializedName("phoneNumber")
    private String phoneNumber;
    @SerializedName("userId")
    private long userId;

    public AuthUser(){}

    public AuthUser(String authHeader, String authUri, String phoneNumber, long userId) {
        this.authHeader = authHeader;
        this.authUri = authUri;
        this.phoneNumber = phoneNumber;
        this.userId = userId;
    }
}
