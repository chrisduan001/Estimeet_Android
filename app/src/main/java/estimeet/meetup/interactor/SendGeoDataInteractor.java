package estimeet.meetup.interactor;

import javax.inject.Inject;

import estimeet.meetup.DefaultSubscriber;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.PostModel.NotificationModel;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import estimeet.meetup.util.MeetupLocationService;
import rx.Observable;

/**
 * Created by AmyDuan on 9/04/16.
 */
public class SendGeoDataInteractor extends BaseInteractor<Boolean> {

    private String geoCoord;
    private SendGeoListener listener;

    @Inject
    public SendGeoDataInteractor(ServiceHelper service, DataHelper data, MeetUpSharedPreference sp) {
        super(service, data, sp);
    }

    public void call(SendGeoListener listener) {
        this.listener = listener;
    }

    public void sendGeoData(String geoCoord) {
        this.geoCoord = geoCoord;
        sharedPreference.saveUserGeo(geoCoord);
        makeRequest(new SendGeoSubscriber(), true);
    }

    @Override
    protected Observable<Boolean> getObservable() {
        return serviceHelper.sendGeodata(baseUser.token, geoCoord, baseUser.userId, sharedPreference.getTravelMode(),
                new NotificationModel(baseUser.id, 0, 0));
    }

    private class SendGeoSubscriber extends DefaultSubscriber<Boolean> {
        @Override
        public void onNext(Boolean aBoolean) {
            super.onNext(aBoolean);
            if (!aBoolean && listener != null) {
                listener.onSendGeoFailed();
            }
        }

        @Override
        protected void onAuthError() {}

        @Override
        protected void onError(String err) {}
    }

    public interface SendGeoListener {
        //send geo returns false, (no active session, session was deleted or expired)
        void onSendGeoFailed();
    }
}
