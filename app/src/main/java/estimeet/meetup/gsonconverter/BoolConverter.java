package estimeet.meetup.gsonconverter;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by AmyDuan on 25/01/16.
 */
public class BoolConverter extends TypeAdapter<Boolean> {
    @Override
    public void write(JsonWriter out, Boolean value) throws IOException {
        out.value(value);
    }

    @Override
    public Boolean read(JsonReader in) throws IOException {
        try {
            String value = in.nextString();
            return value.toLowerCase().equals("yes") || value.toLowerCase().equals("true");
        } catch (NumberFormatException e) {
            throw new JsonSyntaxException(e);
        }
    }
}
