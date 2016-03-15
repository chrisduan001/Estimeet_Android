package estimeet.meetup.interactor;

import javax.inject.Inject;

import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.User;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import rx.Observable;

/**
 * Created by AmyDuan on 15/03/16.
 */
public class AddFriendInteractor extends BaseInteractor<String> {

    @Inject
    public AddFriendInteractor(ServiceHelper service, DataHelper data, MeetUpSharedPreference sp) {
        super(service, data, sp);
    }

    @Override
    protected Observable<String> getObservable(User user) {
        return null;
    }
}
