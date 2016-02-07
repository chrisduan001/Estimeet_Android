package estimeet.meetup.gsonconverter;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by AmyDuan on 25/01/16.
 */
public class DateTimeConverter extends TypeAdapter<Date> {

    final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    @Override
    public void write(JsonWriter out, Date value) throws IOException {
        out.value(dateTimeFormat.format(value));
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        String value = in.nextString();

        try {
            return dateTimeFormat.parse(value);
        } catch (ParseException e) {
            try {
                return dateFormat.parse(value);
            } catch (ParseException e1) {
                throw new JsonSyntaxException(e1);
            }
        }
    }
}
