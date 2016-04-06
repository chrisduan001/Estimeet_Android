package estimeet.meetup.interactor;

import javax.inject.Inject;

import estimeet.meetup.DefaultSubscriber;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import rx.Observable;

/**
 * Created by AmyDuan on 6/04/16.
 */
public class DeleteNotificationInteractor extends BaseInteractor<Boolean> {

    @Inject
    public DeleteNotificationInteractor(ServiceHelper service, DataHelper data,
                                        MeetUpSharedPreference sp) {
        super(service, data, sp);
    }

    public void call() {
        //notification id will be set to 0 if the api call success
        if (sharedPreference.getNotificationid() != 0) {
            makeRequest(new DeleteSubscriber(), true);
        }
    }

    @Override
    protected Observable<Boolean> getObservable() {
        return serviceHelper.deleteNotifications(baseUser.token, baseUser.id, baseUser.userId,
                sharedPreference.getNotificationid());
    }

    private class DeleteSubscriber extends DefaultSubscriber<Boolean> {

        @Override
        public void onNext(Boolean aBoolean) {
            super.onNext(aBoolean);
            if (aBoolean) {
                sharedPreference.setNotificationid(0);
            }
        }

        @Override
        protected void onAuthError() {}

        @Override
        protected void onError(String err) {}
    }
}
