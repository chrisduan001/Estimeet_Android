package estimeet.meetup.interactor;

import javax.inject.Inject;

import estimeet.meetup.model.FriendSession;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.User;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import estimeet.meetup.ui.adapter.FriendListAdapter;
import rx.Observable;
/**
 * Created by AmyDuan on 6/02/16.
 */

public class MainInteractor extends BaseInteractor<User> {

    private MainListener listener;

    @Inject
    public MainInteractor(ServiceHelper serviceHelper, DataHelper dataHelper, MeetUpSharedPreference sp) {
        super(serviceHelper, dataHelper, sp);
    }

    //region fragment call
    public void call(MainListener listener) {
        this.listener = listener;
    }

    public void onSessionRequest(FriendSession session) {
        session.setType(FriendListAdapter.SENT_SESSION);
        session.setTimeToExpire(5);
        session.setDateCreated(System.currentTimeMillis());
        session.setSessionFriendId(session.getFriendId());
        dataHelper.insertSession(session);
    }
    //endregion

    @Override
    protected Observable<User> getObservable(User user) {
        return null;
    }

    public interface MainListener extends BaseListener {

    }
}
