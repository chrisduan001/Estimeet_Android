package estimeet.meetup.util.push;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import com.microsoft.windowsazure.notifications.NotificationsHandler;
import estimeet.meetup.R;
import estimeet.meetup.ui.activity.MainActivity_;

/**
 * Created by AmyDuan on 25/03/16.
 */
public class NotificationHandler extends NotificationsHandler {

    @Override
    public void onReceive(Context context, Bundle bundle) {
        String message = bundle.getString("message");
        if (!TextUtils.isEmpty(message)) {
            String[] msgArray = message.split(",");
            int code = Integer.parseInt(msgArray[0]);

            //100 general notification, need to pull data from server
            switch (code) {
                //new friend join
                case 100:
                    sendGeneralPush(context);
                    break;
                //new session request
                case 101:
                    sendGeneralPush(context);
                    displayOnMainScreen(0, context, context.getString(R.string.app_name),
                            context.getString(R.string.push_session_request));
                    break;
                case 102:
                    sendGeneralPush(context);
                    break;
                default:
                    break;
            }
        }
    }

    //general broadcast will simple try to pull notifications from server
    private void sendGeneralPush(Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.GENERAL_BROADCAST");
        context.sendBroadcast(intent);
    }

    private void displayOnMainScreen(int notificationId, Context context, String title, String message) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent pIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity_.class), 0);

        Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.placeholder_icon)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setAutoCancel(true)
                .setContentText(message);

        mBuilder.setContentIntent(pIntent);
        mBuilder.setSound(notificationUri);
        notificationManager.notify(notificationId, mBuilder.build());
    }
}