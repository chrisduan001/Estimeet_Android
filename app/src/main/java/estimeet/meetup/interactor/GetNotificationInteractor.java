package estimeet.meetup.interactor;

import javax.inject.Inject;
import javax.inject.Named;

import estimeet.meetup.DefaultSubscriber;
import estimeet.meetup.model.Friend;
import estimeet.meetup.model.FriendSession;
import estimeet.meetup.model.ListItem;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.NotificationEntity;
import estimeet.meetup.model.User;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import estimeet.meetup.ui.adapter.FriendListAdapter;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by AmyDuan on 29/03/16.
 */
public class GetNotificationInteractor extends BaseInteractor<ListItem<NotificationEntity>> {
    private static final int NOTIFICATION_FRIEND_REQUEST = 0;
    private static final int NOTIFICATION_SESSION_REQUEST = 1;
    private static final int NOTIFICATION_SESSION_ACCEPTANCE = 2;

    private User user;
    @Inject
    public GetNotificationInteractor(ServiceHelper service, DataHelper data, MeetUpSharedPreference sp,
                                     @Named("currentUser") User user) {
        super(service, data, sp);
        this.user = user;
    }

    //region presenter call
    public void getNotifications() {
        makeRequest(user, new GetNotificationsSubscriber(), true);
    }
    //endregion

    @Override
    protected Observable<ListItem<NotificationEntity>> getObservable(User user) {
        return serviceHelper.getAllNotifications(user.token, user.id, user.userId);
    }

    private class GetNotificationsSubscriber extends DefaultSubscriber<ListItem<NotificationEntity>> {

        @Override
        public void onNext(ListItem<NotificationEntity> notificationEntityListItem) {
            super.onNext(notificationEntityListItem);

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
            }
        }

        @Override
        protected void onAuthError() {
        }

        @Override
        protected void onError(String err) {
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
            //// TODO: 3/04/16 server need to give a string with following format "0 or 1 or 2"
            //request length to share
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
            //// TODO: 3/04/16 server need to provide expire time in milliseconds (time to expire in millis = time to expire in millis - (time notification received - time session created)
            if (dataHelper.getFriend(friendId) != null) {
                dataHelper.insertSession(SessionFactory.createActiveSession(friendId, sessionId, sessionLId));
            }
        }
    }
}
