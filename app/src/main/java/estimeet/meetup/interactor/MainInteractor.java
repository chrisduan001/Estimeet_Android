package estimeet.meetup.interactor;

import javax.inject.Inject;

import estimeet.meetup.DefaultSubscriber;
import estimeet.meetup.model.User;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by AmyDuan on 6/02/16.
 */
public class MainInteractor extends BaseInteractor {

    @Inject
    public MainInteractor(ServiceHelper serviceHelper, DataHelper dataHelper) {
        super(serviceHelper, dataHelper);
    }

}
