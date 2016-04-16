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
import estimeet.meetup.factory.SessionActivityFactory;
import estimeet.meetup.interactor.SendGeoDataInteractor;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.EstimeetApi;
import estimeet.meetup.network.ServiceHelper;
import estimeet.meetup.ui.activity.MainActivity_;
import estimeet.meetup.util.MeetupLocationService;

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
                //friend accepted session
                case 102:
                    sendGeneralPush(context);
                    startLocationService(context, Long.parseLong(msgArray[1]));
                    displayOnMainScreen(0, context, context.getString(R.string.app_name),
                            context.getString(R.string.push_session_starts));
                    break;
                //session cancelled
                //delete item from db
                case 103:
                    int friendId = Integer.parseInt(msgArray[1]);
                    onSessionCancelled(friendId, context);
                    break;
                case 999:
                    displayOnMainScreen(1, context, "test", "this is a test");
                    break;
                default:
                    break;
            }
        }
    }

    //process session cancelled notification
    private void onSessionCancelled(int friendId, Context context) {
        DataHelper dataHelper = new DataHelper(context.getContentResolver());
        dataHelper.deleteSession(friendId);

        Boolean isActiveSession = SessionActivityFactory.checkSession(dataHelper);
        if (isActiveSession == null) {
            sendNoSessionBroadCast(context);
        } else if (isActiveSession) {
            return;
        }
        MeetupLocationService.getInstance(context).disconnectLocation();
    }

    //general broadcast will simple try to pull notifications from server
    private void sendGeneralPush(Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.GENERAL_BROADCAST");
        context.sendBroadcast(intent);
    }

    private void sendNoSessionBroadCast(Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.NO_ACTIVITY_BROADCAST");
        context.sendBroadcast(intent);
    }

    private void startLocationService(Context context, long expiresInMillis) {
        if (expiresInMillis > 0) { //>0 is a continuous tracking
            MeetupLocationService.getInstance(context).startLocationService(expiresInMillis);
        }
    }

    private void displayOnMainScreen(int notificationId, Context context, String title, String message) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent pIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity_.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

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
