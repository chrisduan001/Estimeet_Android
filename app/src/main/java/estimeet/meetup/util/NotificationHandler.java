package estimeet.meetup.util;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.microsoft.windowsazure.notifications.NotificationsHandler;

/**
 * Created by AmyDuan on 25/03/16.
 */
public class NotificationHandler extends NotificationsHandler {

    @Override
    public void onReceive(Context context, Bundle bundle) {
        String message = bundle.getString("message");

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
