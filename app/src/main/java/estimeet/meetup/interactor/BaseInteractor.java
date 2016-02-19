package estimeet.meetup.interactor;

import estimeet.meetup.DefaultSubscriber;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
/**
 * Created by AmyDuan on 6/02/16.
 */
public class BaseInteractor {

    protected static ServiceHelper serviceHelper;
    protected static DataHelper dataHelper;
    protected static MeetUpSharedPreference sharedPreference;

    public BaseInteractor(ServiceHelper service, DataHelper data) {
        this(service, data, null);
    }

    public BaseInteractor(ServiceHelper service, DataHelper data, MeetUpSharedPreference sp) {
        serviceHelper = service;
        dataHelper = data;
        sharedPreference = sp;
    }

    private static class RenewAuthToken extends DefaultSubscriber<String> {

    }

}
