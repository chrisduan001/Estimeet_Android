package estimeet.meetup.ui.presenter;

/**
 * Created by AmyDuan on 6/02/16.
 */
public interface IBasePresenter {

    void onResume();
    void onPause();
    void onDestory();

    void onRequestPermissionCallback(boolean isGranted);
}
