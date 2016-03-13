package estimeet.meetup.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by AmyDuan on 12/03/16.
 */
public class ListItem<T> extends BaseModel {
    @SerializedName("ListItem")
    public List<T> items;
}
