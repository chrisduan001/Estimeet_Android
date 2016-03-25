package estimeet.meetup.interactor;

import android.text.TextUtils;

import org.androidannotations.annotations.Background;
import javax.inject.Inject;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.PushModel;
import estimeet.meetup.model.User;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import rx.Observable;
/**
 * Created by AmyDuan on 6/02/16.
 */

public class MainInteractor extends BaseInteractor<User> {

    private PushModel pushModel;

    @Inject
    public MainInteractor(ServiceHelper serviceHelper, DataHelper dataHelper, MeetUpSharedPreference sp,
                          PushModel pushModel) {
        super(serviceHelper, dataHelper, sp);
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
    protected Observable<User> getObservable(User user) {
        return null;
    }
}
