package estimeet.meetup.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AmyDuan on 21/02/16.
 */
public class TokenResponse {
    @SerializedName("access_token")
    public String access_token;

    @SerializedName("token_type")
    private String token_type;

    @SerializedName("expires_in")
    private long expires_in;
}
