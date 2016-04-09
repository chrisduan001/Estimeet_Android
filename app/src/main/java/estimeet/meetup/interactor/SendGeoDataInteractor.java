package estimeet.meetup.interactor;

import javax.inject.Inject;

import estimeet.meetup.DefaultSubscriber;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.PostModel.NotificationModel;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import rx.Observable;

/**
 * Created by AmyDuan on 9/04/16.
 */
public class SendGeoDataInteractor extends BaseInteractor<Void> {

    private String geoCoord;

    @Inject
    public SendGeoDataInteractor(ServiceHelper service, DataHelper data, MeetUpSharedPreference sp) {
        super(service, data, sp);
    }

    public void sendGeoData(String geoCoord) {
        this.geoCoord = geoCoord;
        makeRequest(new DefaultSubscriber<Void>() {
            @Override
            protected void onAuthError() {}

            @Override
            protected void onError(String err) {}
        }, true);
    }

    @Override
    protected Observable<Void> getObservable() {
        return serviceHelper.sendGeodata(baseUser.token, geoCoord, baseUser.userId,
                new NotificationModel(baseUser.id, 0, 0));
    }
}
