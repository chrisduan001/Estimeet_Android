package estimeet.meetup.ui.presenter;

import android.Manifest;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import estimeet.meetup.interactor.PermissionInteractor;
import estimeet.meetup.ui.BaseView;

/**
 * Created by AmyDuan on 6/07/16.
 */
public class PermissionPresenter extends BasePresenter implements PermissionInteractor.PermissionListener {
    private WeakReference<PermissionView> view;

    private PermissionInteractor interactor;
    //region lifecycle & view
    @Inject
    public PermissionPresenter(PermissionInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void onPermissionResult(boolean isGranted) {
        if (isGranted) {
            view.get().showProgressDialog();
            view.get().onReadContactPermissionGranted();
        } else {
            view.get().onContactPermissionRejected();
        }
    }

    //endregion

    //region fragment call
    public void setView(PermissionView view) {
        this.view = new WeakReference<>(view);
    }

    public void requestContactPermission() {
        view.get().checkPermission(Manifest.permission.READ_CONTACTS);
    }

    public void sendContactList(String contacts) {
        interactor.sendContacts(contacts, this);
    }
    //endregion

    @Override
    public void onAuthFailed() {}

    @Override
    public void onSendContactCompleted() {
        view.get().onSendContactCompleted();
    }

    public interface PermissionView extends BaseView {
        void onSendContactCompleted();
        void onReadContactPermissionGranted();
        void onContactPermissionRejected();
    }
}
