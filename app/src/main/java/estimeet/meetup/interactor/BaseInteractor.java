package estimeet.meetup.interactor;

import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;

/**
 * Created by AmyDuan on 6/02/16.
 */
public class BaseInteractor {

    protected ServiceHelper serviceHelper;
    protected DataHelper dataHelper;

    public BaseInteractor(ServiceHelper serviceHelper, DataHelper dataHelper) {
        this.serviceHelper = serviceHelper;
        this.dataHelper = dataHelper;
    }
}
