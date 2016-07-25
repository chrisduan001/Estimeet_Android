package estimeet.meetup.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AmyDuan on 11/04/16.
 */
public class LocationModel extends BaseModel {
    @SerializedName("distance")
    public int distance;

    @SerializedName("eta")
    public int eta;

    @SerializedName("travelMode")
    public int travelMode;

    @SerializedName("location")
    public String location;
}
