package estimeet.meetup.interactor;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.inject.Inject;

import estimeet.meetup.DefaultSubscriber;
import estimeet.meetup.model.Friend;
import estimeet.meetup.model.ListItem;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.NotificationEntity;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import estimeet.meetup.factory.SessionCreationFactory;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by AmyDuan on 29/03/16.
 */
public class GetNotificationInteractor extends BaseInteractor<ListItem<NotificationEntity>> {
    private static final int NOTIFICATION_FRIEND_REQUEST = 0;
    private static final int NOTIFICATION_SESSION_REQUEST = 1;
    private static final int NOTIFICATION_SESSION_ACCEPTANCE = 2;
    private static final int NOTIFICATION_PROFILE_CHAGNE = 3;

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
                    case NOTIFICATION_PROFILE_CHAGNE:
                        onFriendProfileChanged(entity.appendix);
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
                dataHelper.insertSession(SessionCreationFactory.createPendingSession(friendId, length));
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
                dataHelper.insertSession(SessionCreationFactory.createActiveSession(friendId, sessionId,
                        sessionLId, expireTimeInMilli, length));

                listener.onCreateNewSession(expireTimeInMilli);
            }
        }

        private void onFriendProfileChanged(String appendix) {
            String[] appendixArray = appendix.split(",");
            Friend friend = dataHelper.getFriend(Integer.parseInt(appendixArray[0]));
            if (friend != null) {
                if (!friend.userName.equals(appendixArray[1])) {
                    friend.userName = appendixArray[1];
                }

                updateFriendDp(friend);
            }
        }

        private void updateFriendDp(final Friend friend) {
            Observable.just(friend.dpUri)
                    .map(new Func1<String, byte[]>() {
                        @Override
                        public byte[] call(String s) {
                            ByteArrayOutputStream bao = new ByteArrayOutputStream();
                            try {
                                InputStream is = (InputStream) new URL(s).getContent();
                                byte[] byteChunk = new byte[1024];
                                int n;

                                while ( (n = is.read(byteChunk)) > 0 ) {
                                    bao.write(byteChunk, 0, n);
                                }
                            } catch (IOException e) {
                                throw new RuntimeException("Error while get dp byte");
                            }

                            return bao.toByteArray();
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .doOnError(new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            //// TODO: 27/06/16 add error handling
                            Log.d("error", "call: " + throwable.getLocalizedMessage());
                        }
                    })
                    .subscribe(new Action1<byte[]>() {
                        @Override
                        public void call(byte[] bytes) {
                            friend.image = bytes;
                            dataHelper.updateFriendData(friend);
                        }
                    });
        }
    }

    public interface GetNotificationListener extends BaseListener {
        void getNotificationFinished();
        void onCreateNewSession(long expireTimeInMilli);
    }
}
