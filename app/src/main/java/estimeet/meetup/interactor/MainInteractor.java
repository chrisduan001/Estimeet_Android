package estimeet.meetup.interactor;

import javax.inject.Inject;

import estimeet.meetup.DefaultSubscriber;
import estimeet.meetup.factory.SessionActivityFactory;
import estimeet.meetup.model.FriendSession;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.PostModel.NotificationModel;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import estimeet.meetup.factory.SessionCreationFactory;
import rx.Observable;
/**
 * Created by AmyDuan on 6/02/16.
 */

public class MainInteractor extends BaseInteractor<Boolean> {

    private MainListener listener;

    private FriendSession session;

    @Inject
    public MainInteractor(ServiceHelper serviceHelper, DataHelper dataHelper,
                          MeetUpSharedPreference sp) {
        super(serviceHelper, dataHelper, sp);
    }

    //region fragment call
    public void call(MainListener listener) {
        this.listener = listener;
    }

    public void onSessionRequest(FriendSession session) {
        this.session = session;
        makeRequest(new SendSessionRequestSubscriber(), true);
    }

    public void insertSession(FriendSession session) {
        //create session
        //session will be deleted if request failed
        SessionCreationFactory.createRequestedSession(session);
        dataHelper.insertSession(session);
    }

    public void deleteSession(int id) {
        dataHelper.deleteSession(id);
    }

    public void onSessionIgnored(FriendSession friendSession) {
        dataHelper.deleteSession(friendSession.getFriendId());
    }

    public void checkSessionExpiration() {
        //null == no session available, no == no active session, yes == active session
        listener.onCheckSessionExpiration(SessionActivityFactory.checkSession(dataHelper));
    }

    public void onSessionFinished(int friendId) {
        dataHelper.deleteSession(friendId);
    }

    public void setTravelMode(int travelMode) {
        sharedPreference.saveTravelInfo(travelMode);
    }

    public void getTravelMode() {
        listener.onGetTravelMode(sharedPreference.getTravelMode());
    }
    //endregion

    @Override
    protected Observable<Boolean> getObservable() {
        return serviceHelper.sendSessionRequest(baseUser.token, session.getRequestedLength(),
                new NotificationModel(baseUser.id, session.getFriendId(),
                        dataHelper.getFriend(session.getFriendId()).userId));
    }

    private class SendSessionRequestSubscriber extends DefaultSubscriber<Boolean> {

        @Override
        public void onNext(Boolean aBoolean) {
            super.onNext(aBoolean);

            if (!aBoolean) {
                dataHelper.deleteSession(session.getFriendId());
                throw new RuntimeException("1000");
            }
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        protected void onAuthError() {
            sharedPreference.removeSharedPreference();
            listener.onAuthFailed();
        }

        @Override
        protected void onError(String err) {
            dataHelper.deleteSession(session.getFriendId());
            listener.onError(err);
        }
    }

    public interface MainListener extends BaseListener {
        void onCheckSessionExpiration(Boolean result);
        void onGetTravelMode(int travelMode);
    }
}
