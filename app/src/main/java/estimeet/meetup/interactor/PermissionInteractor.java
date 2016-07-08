package estimeet.meetup.interactor;

import android.text.TextUtils;

import javax.inject.Inject;

import estimeet.meetup.DefaultSubscriber;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.PostModel.SendContact;
import estimeet.meetup.model.User;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import rx.Observable;

/**
 * Created by AmyDuan on 6/07/16.
 */
public class PermissionInteractor extends BaseInteractor<User> {

    private String contacts;

    private PermissionListener listener;

    @Inject
    public PermissionInteractor(ServiceHelper service, DataHelper data, MeetUpSharedPreference sp) {
        super(service, data, sp);
    }

    public void sendContacts(String contacts, PermissionListener listener) {
        if (TextUtils.isEmpty(contacts)) {
            listener.onSendContactCompleted();
            return;
        }
        this.contacts = contacts;
        this.listener = listener;
        baseUser = sharedPreference.getUserFromSp();

        makeRequest(new SendContactSubscriber(), true);
    }

    @Override
    protected Observable<User> getObservable() {
        SendContact contactModel = new SendContact(baseUser.id, baseUser.userId, contacts);
        return serviceHelper.sendContacts(baseUser.token, contactModel);
    }

    private class SendContactSubscriber extends DefaultSubscriber<User> {
        @Override
        public void onNext(User user) {
            listener.onSendContactCompleted();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        protected void onError(String err) {
            listener.onSendContactCompleted();
        }

        @Override
        protected void onAuthError() {
            listener.onSendContactCompleted();
        }
    }

    public interface PermissionListener {
        void onSendContactCompleted();
    }
}
