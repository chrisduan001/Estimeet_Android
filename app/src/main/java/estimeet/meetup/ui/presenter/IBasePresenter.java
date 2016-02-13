package estimeet.meetup.ui.presenter;

import android.graphics.Bitmap;

/**
 * Created by AmyDuan on 6/02/16.
 */
public interface IBasePresenter {

    void onResume();
    void onPause();
    void onDestory();

    void onPermissionResult(boolean isGranted);
}
