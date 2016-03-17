package estimeet.meetup.interactor;

import android.support.annotation.UiThread;

import javax.inject.Inject;

import estimeet.meetup.model.Friend;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.User;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import rx.Observable;

/**
 * Created by AmyDuan on 15/03/16.
 */
public class ManageFriendInteractor extends BaseInteractor<String> {

    @Inject
    public ManageFriendInteractor(ServiceHelper service, DataHelper data, MeetUpSharedPreference sp) {
        super(service, data, sp);
    }

    //region presenter call
    public void updateFriendData(Friend friend) {
        dataHelper.updateFriendData(friend);
    }
    //endregion

    @Override
    protected Observable<String> getObservable(User user) {
        return null;
    }
}
