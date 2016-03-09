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

    //if error code processed/error occurred return 0, else return errorcode for presenter to process
    protected int processErrorCode(String errorCode, BaseView baseView) {
        int result = 0;
        try {
            int code = Integer.parseInt(errorCode);
            switch (code) {
                case 100:
                    onAuthFailed();
                    break;
                case 500:
                    baseView.onServerError();
                    break;
                default:
                    result = code;
                    break;
            }
        } catch (NumberFormatException e) {
            //in case the error code is an actual error message instead of error code
            return 0;
        }
        return result;
    }
}
