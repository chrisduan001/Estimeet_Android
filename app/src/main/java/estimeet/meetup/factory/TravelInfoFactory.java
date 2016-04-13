package estimeet.meetup.factory;

import android.content.Context;
import estimeet.meetup.R;

/**
 * Created by AmyDuan on 12/04/16.
 */
public class TravelInfoFactory {
    public static final int TRAVEL_MODE_WALK = 0;
    public static final int TRAVEL_MODE_DRIVE = 1;
    public static final int TRAVEL_MODE_TRANSIT = 2;
    public static final int TRAVEL_MODE_BIKE = 3;

    private static final long ACTIVE_SESSION_EXPIRE_MILLIS = 180000; //3 minutes

    public static String getTravelModeString(int mode, Context context) {
        switch (mode) {
            case TRAVEL_MODE_WALK:
                return context.getString(R.string.travel_walk);
            case TRAVEL_MODE_DRIVE:
                return context.getString(R.string.travel_drive);
            case TRAVEL_MODE_TRANSIT:
                return "";
            case TRAVEL_MODE_BIKE:
                return "";
            default:
                return "";
        }
    }

    public static boolean isLocationDataExpired(long dateCreated) {
        return System.currentTimeMillis() - dateCreated > ACTIVE_SESSION_EXPIRE_MILLIS;
    }

    public static String getEtaString(int minutes, Context context) {
        String minuteString = context.getString(R.string.minute_string);
        String hourString = context.getString(R.string.hour_string);
        if (minutes <= 60) {
            return String.format("%d %s", minutes, minuteString);
        } else {
            int hours = minutes / 60;
            int remainder = minutes % 60;

            return String.format("%d %s %d %s", hours, hourString, remainder, minuteString);
        }
    }

    public static String getDistanceString(double distance, Context context) {
        return String.format("%.2f %s", distance / 1000,
                context.getString(R.string.distance_format));
    }
}
