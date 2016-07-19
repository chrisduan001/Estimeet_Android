package estimeet.meetup.gsonconverter;

import android.support.annotation.Keep;
import android.text.TextUtils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by AmyDuan on 25/01/16.
 */
@Keep
public class StringConverter extends TypeAdapter<String> {
    @Override
    public void write(JsonWriter out, String value) throws IOException {
        out.value(value);
    }

    @Override
    public String read(JsonReader in) throws IOException {
        if (in.peek() != JsonToken.NULL) {
            String value = in.nextString();
            return TextUtils.isEmpty(value) ? null : value;
        } else {
            in.skipValue();
            return null;
        }

    }
}
