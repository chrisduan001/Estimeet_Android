package estimeet.meetup.interactor;

import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.User;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import rx.Observable;

/**
 * Created by AmyDuan on 6/07/16.
 */
public class PermissionInteractor extends BaseInteractor<User> {


    public PermissionInteractor(ServiceHelper service, DataHelper data, MeetUpSharedPreference sp) {
        super(service, data, sp);
    }

    @Override
    protected Observable<User> getObservable() {
        return null;
    }
}
