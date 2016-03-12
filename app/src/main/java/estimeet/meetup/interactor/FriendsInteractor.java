package estimeet.meetup.interactor;

import estimeet.meetup.model.Friends;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;

/**
 * Created by AmyDuan on 12/03/16.
 */
public class FriendsInteractor extends BaseInteractor<Friends> {
    public FriendsInteractor(ServiceHelper service, DataHelper data, MeetUpSharedPreference sp) {
        super(service, data, sp);
    }
}
