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
public class PermissionInteractor extends BaseInteractor<Void> {

    private String contacts;

    private PermissionListener listener;

    @Inject
    public PermissionInteractor(ServiceHelper service, DataHelper data, MeetUpSharedPreference sp) {
        super(service, data, sp);
    }

    public void sendContacts(String contacts, PermissionListener listener) {
        this.contacts = contacts;
        this.listener = listener;

        if (TextUtils.isEmpty(contacts)) {
            sendContactCompleted();
            return;
        }
        baseUser = sharedPreference.getUserFromSp();

        makeRequest(new SendContactSubscriber(), true);
    }

    private void sendContactCompleted() {
        if (listener != null) {
            listener.onSendContactCompleted();
        }
    }

    @Override
    protected Observable<Void> getObservable() {
        SendContact contactModel = new SendContact(baseUser.id, baseUser.userId, contacts);
        return serviceHelper.sendContacts(baseUser.token, contactModel);
    }

    private class SendContactSubscriber extends DefaultSubscriber<Void> {
        @Override
        public void onNext(Void aVoid) {
            super.onNext(aVoid);
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        protected void onError(String err) {
            sendContactCompleted();
        }

        @Override
        protected void onAuthError() {
            sendContactCompleted();
        }
    }

    public interface PermissionListener {
        void onSendContactCompleted();
    }
}
