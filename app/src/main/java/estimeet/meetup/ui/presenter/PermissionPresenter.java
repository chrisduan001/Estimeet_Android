package estimeet.meetup.ui.presenter;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import estimeet.meetup.ui.BaseView;

/**
 * Created by AmyDuan on 6/07/16.
 */
public class PermissionPresenter extends BasePresenter {
    private WeakReference<PermissionView> view;

    //region lifecycle & view
    @Inject
    public PermissionPresenter() {

    }
    //endregion

    @Override
    public void onAuthFailed() {

    }

    public interface PermissionView extends BaseView {

    }
}
