package estimeet.meetup.interactor;

import javax.inject.Inject;

import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.User;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import rx.Observable;

/**
 * Created by AmyDuan on 29/03/16.
 */
public class GetNotificationInteractor extends BaseInteractor {
    @Inject
    public GetNotificationInteractor(ServiceHelper service, DataHelper data, MeetUpSharedPreference sp) {
        super(service, data, sp);
    }

    @Override
    protected Observable getObservable(User user) {
        return null;
    }
}
