package estimeet.meetup.interactor;

import android.text.TextUtils;

import javax.inject.Inject;

import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.PushModel;
import estimeet.meetup.model.User;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import rx.Observable;

/**
 * Created by AmyDuan on 25/03/16.
 */
public class PushInteractor extends BaseInteractor<String> {

    private PushModel pushModel;

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
            sharedPreference.setGcmRegId("");
        }
    }

    void startPushRegister() throws Exception {
        //re-register channel if the version number has changed
        if (sharedPreference.getVersionCode() == 0 ||
                sharedPreference.getVersionCode() != pushModel.getVersionCode() ||
                TextUtils.isEmpty(sharedPreference.getGcmRegId())) {

            @SuppressWarnings("deprecation")
            String regId = pushModel.getGcm().register(pushModel.getSenderId());
            pushModel.getHub().register(regId, pushModel.getUserId());
            sharedPreference.setVersionCode(pushModel.getVersionCode());
            sharedPreference.setGcmRegId(regId);
        } else {
            pushModel.getHub().register(sharedPreference.getGcmRegId(), pushModel.getUserId());
        }
    }
    //endregion

    @Override
    protected Observable getObservable(User user) {
        return null;
    }
}
