package estimeet.meetup.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ProgressBar;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

import estimeet.meetup.R;
import estimeet.meetup.di.components.SignInComponent;
import estimeet.meetup.ui.presenter.BasePresenter;
import estimeet.meetup.ui.presenter.PermissionPresenter;
import estimeet.meetup.util.ContactList;

/**
 * Created by AmyDuan on 6/07/16.
 */
@EFragment(R.layout.fragment_permission)
public class PermissionFragment extends BaseFragment implements PermissionPresenter.PermissionView {

    //call back to activity listener
    public interface PermissionCallback {
        void onPermissionFinished();
    }

    @Inject PermissionPresenter presenter;

    @ViewById(R.id.progress_bar)    ProgressBar progressBar;

    private PermissionCallback permissionCallback;
    //region lifecycle & view


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PermissionCallback) {
            this.permissionCallback = (PermissionCallback) context;
        } else {
            throw new UnsupportedOperationException("Activity must implement " +
                    PermissionCallback.class.getSimpleName());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();

        setView();
    }

    private void setView() {
        presenter.setView(this);
    }

    private void initialize() {
        getComponent(SignInComponent.class).inject(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    protected ProgressBar getProgressBar() {
        return progressBar;
    }
    //endregion

    @Click(R.id.btn_grant_permission)
    protected void onGrantPermission() {
        presenter.requestContactPermission();
    }

    //region presenter callback
    @Override
    public void onReadContactPermissionGranted() {
        presenter.sendContactList(ContactList.getUserContactList(getActivity()));
    }

    @Override
    public void onContactPermissionRejected() {
        permissionCallback.onPermissionFinished();
    }

    @Override
    public void onSendContactCompleted() {
        permissionCallback.onPermissionFinished();
    }

    @Override
    public void showProgressDialog() {
        showProgressDialog(getString(R.string.progress_loading));
    }
    //endregion
}
