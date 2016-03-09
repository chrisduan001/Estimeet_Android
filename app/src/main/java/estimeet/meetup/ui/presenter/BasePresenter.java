package estimeet.meetup.ui.presenter;

import android.content.res.Resources;

import estimeet.meetup.MainApplication;
import estimeet.meetup.R;

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

    protected String getErrorString(String errorCode) {
        try {
            int code = Integer.parseInt(errorCode);
            switch (code) {
                case 100:
                    onAuthFailed();
                    return null;
                case 500:
                    return MainApplication.getContext().getString(R.string.error_500);
                default:
                    return null;
            }
        } catch (NumberFormatException e) {
            //in case the error code is an actual error message instead of error code
            return errorCode;
        }
    }
}
