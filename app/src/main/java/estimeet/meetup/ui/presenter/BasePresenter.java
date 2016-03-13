package estimeet.meetup.ui.presenter;

import estimeet.meetup.ui.BaseView;

/**
 * Created by AmyDuan on 6/02/16.
 */
public abstract class BasePresenter implements IBasePresenter {

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onDestory() {
    }

    @Override
    public void onPermissionResult(boolean isGranted) {}

    public abstract void onAuthFailed();
}
