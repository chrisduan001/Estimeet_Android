package estimeet.meetup.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AmyDuan on 15/02/16.
 */
public class BaseModel {
    @SerializedName("errorCode")
    public int errorCode;

    @SerializedName("errorMessage")
    public String errorMessage;

    public boolean hasError() {
        return errorCode != 0;
    }
}
