package estimeet.meetup.gsonconverter;

import android.text.TextUtils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by AmyDuan on 25/01/16.
 */
public class StringConverter extends TypeAdapter<String> {
    @Override
    public void write(JsonWriter out, String value) throws IOException {
        out.value(value);
    }

    @Override
    public String read(JsonReader in) throws IOException {
        String value = in.nextString();
        return TextUtils.isEmpty(value) ? null : value;
    }
}