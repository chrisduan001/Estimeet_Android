package estimeet.meetup.interactor;

import javax.inject.Inject;

import estimeet.meetup.DefaultSubscriber;
import estimeet.meetup.model.Friend;
import estimeet.meetup.model.ListItem;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.NotificationEntity;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import estimeet.meetup.util.SessionFactory;
import rx.Observable;

/**
 * Created by AmyDuan on 29/03/16.
 */
public class GetNotificationInteractor extends BaseInteractor<ListItem<NotificationEntity>> {
    private static final int NOTIFICATION_FRIEND_REQUEST = 0;
    private static final int NOTIFICATION_SESSION_REQUEST = 1;
    private static final int NOTIFICATION_SESSION_ACCEPTANCE = 2;

    private GetNotificationListener listener;

    @Inject
    public GetNotificationInteractor(ServiceHelper service, DataHelper data, MeetUpSharedPreference sp) {
        super(service, data, sp);
    }

    //region presenter call
    public void call(GetNotificationListener listener) {
        this.listener = listener;
    }

    public void getNotifications() {
        makeRequest(new GetNotificationsSubscriber(), true);
    }
    //endregion

    @Override
    protected Observable<ListItem<NotificationEntity>> getObservable() {
        return serviceHelper.getAllNotifications(baseUser.token, baseUser.id, baseUser.userId);
    }

    private class GetNotificationsSubscriber extends DefaultSubscriber<ListItem<NotificationEntity>> {

        @Override
        public void onNext(ListItem<NotificationEntity> notificationEntityListItem) {
            super.onNext(notificationEntityListItem);

            long notificationId = 0;
            for (NotificationEntity entity : notificationEntityListItem.items) {
                switch (entity.identifier) {
                    case NOTIFICATION_FRIEND_REQUEST:
                        processFriendRequest(entity.appendix);
                        break;
                    case NOTIFICATION_SESSION_REQUEST:
                        processSessionRequest(entity.appendix);
                        break;
                    case NOTIFICATION_SESSION_ACCEPTANCE:
                        createNewSession(entity.appendix);
                        break;
                }
                if (entity.notificationId > notificationId) {
                    notificationId = entity.notificationId;
                }
            }
            sharedPreference.setNotificationid(notificationId);
            listener.getNotificationFinished();
        }

        @Override
        protected void onAuthError() {}

        @Override
        protected void onError(String err) {
            listener.getNotificationFinished();
        }

        private void processFriendRequest(String appendix) {
            String[] appendixArray = appendix.split(",");
            Friend friend = new Friend();
            friend.id = Integer.parseInt(appendixArray[0]);
            friend.userId = Long.parseLong(appendixArray[1]);
            friend.userName = appendixArray[2];
            friend.dpUri = appendixArray[3];

            dataHelper.insertFriend(friend);
        }

        private void processSessionRequest(String appendix) {
            String[] appendixArray = appendix.split(",");
            int friendId = Integer.parseInt(appendixArray[0]);
            //request length to share (0, 1, 2)
            int length = Integer.parseInt(appendixArray[1]);

            if (dataHelper.getFriend(friendId) != null) {
                dataHelper.insertSession(SessionFactory.createPendingSession(friendId, length));
            }
        }

        private void createNewSession(String appendix) {
            String[] appendixArray = appendix.split(",");
            int friendId = Integer.parseInt(appendixArray[0]);
            int sessionId = Integer.parseInt(appendixArray[1]);
            long sessionLId = Long.parseLong(appendixArray[2]);
            int length = Integer.parseInt(appendixArray[3]);
            //expires in minutes
//            int expiresInMinutes = Integer.parseInt(appendixArray[4]);
            long expireTimeInMilli = Long.parseLong(appendixArray[5]);

            if (dataHelper.getFriend(friendId) != null) {
                dataHelper.insertSession(SessionFactory.createActiveSession(friendId, sessionId,
                        sessionLId, expireTimeInMilli, length));

                listener.onCreateNewSession(expireTimeInMilli);
            }
        }
    }

    public interface GetNotificationListener extends BaseListener {
        void getNotificationFinished();
        void onCreateNewSession(long expireTimeInMilli);
    }
}
