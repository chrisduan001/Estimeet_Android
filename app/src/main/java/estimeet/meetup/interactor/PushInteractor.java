package estimeet.meetup.interactor;

import android.text.TextUtils;

import javax.inject.Inject;
import javax.inject.Named;

import estimeet.meetup.DefaultSubscriber;
import estimeet.meetup.model.BaseModel;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.PushModel;
import estimeet.meetup.model.User;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import rx.Observable;

/**
 * Created by AmyDuan on 25/03/16.
 */
public class PushInteractor extends BaseInteractor<BaseModel> {

    private PushModel pushModel;

    private RegisterChannelSubscriber subscriber;

    @Inject
    public PushInteractor(ServiceHelper service, DataHelper data, MeetUpSharedPreference sp,
                          PushModel pushModel) {
        super(service, data, sp);

        this.pushModel = pushModel;
    }

    //region presenter call
    public void registerPushChannel() {
        try{
            startPushRegister();
        } catch (Exception e) {
            clearGcmRegId();
        }
    }

    private void startPushRegister() throws Exception {
        //re-register channel if the version number has changed
        if (sharedPreference.getVersionCode() == 0 ||
                sharedPreference.getVersionCode() != pushModel.getVersionCode() ||
                TextUtils.isEmpty(sharedPreference.getGcmRegId())) {

            @SuppressWarnings("deprecation")
            String regId = pushModel.getGcm().register(pushModel.getSenderId());
            pushModel.getHub().register(regId, pushModel.getUserId());
            sharedPreference.setVersionCode(pushModel.getVersionCode());
            sharedPreference.setGcmRegId(regId);

            subscriber = new RegisterChannelSubscriber();
            makeRequest(subscriber, true);
        } else {
            pushModel.getHub().register(sharedPreference.getGcmRegId(), pushModel.getUserId());
        }
    }
    //endregion

    //region logic
    private void clearGcmRegId() {
        sharedPreference.setGcmRegId("");
    }
    //endregion

    @Override
    protected Observable<BaseModel> getObservable() {
        return serviceHelper.registerChannel(baseUser.token, baseUser.id, baseUser.userId);
    }

    private class RegisterChannelSubscriber extends DefaultSubscriber<BaseModel> {

        @Override
        public void onNext(BaseModel baseModel) {
            super.onNext(baseModel);
        }

        @Override
        public void onError(Throwable e) {
            clearGcmRegId();
        }

        @Override
        protected void onAuthError() {}

        @Override
        protected void onError(String err) {}
    }
}
