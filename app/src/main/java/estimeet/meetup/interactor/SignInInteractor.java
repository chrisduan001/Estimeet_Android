package estimeet.meetup.interactor;

import javax.inject.Inject;

import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;

/**
 * Created by AmyDuan on 8/02/16.
 */
public class SignInInteractor extends BaseInteractor {

    @Inject
    public SignInInteractor(ServiceHelper serviceHelper, DataHelper dataHelper) {
        super(serviceHelper, dataHelper);
    }
}
