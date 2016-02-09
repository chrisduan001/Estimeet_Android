package estimeet.meetup.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import estimeet.meetup.di.HasComponent;
import estimeet.meetup.di.components.MainComponent;
import estimeet.meetup.ui.BaseView;
import estimeet.meetup.ui.PermissionType;
import estimeet.meetup.ui.presenter.BasePresenter;

/**
 * Created by AmyDuan on 6/02/16.
 */
public abstract class BaseFragment extends Fragment {

    private static final int PERMISSION_REQUEST_CODE = 100;

    //region lifecycle
    @Override
    public void onResume() {
        super.onResume();
        getPresenter().onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        getPresenter().onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPresenter().onDestory();
    }
    //endregion

    protected abstract BasePresenter getPresenter();

    @SuppressWarnings("unchecked")
    protected <T> T getComponent(Class<T> componentType) {
        return componentType.cast(((HasComponent<T>) getActivity()).getComponent());
    }

    //region presenter callback
    public void showShortToastMessage(String message) {
        showToastMessage(true, message);
    }

    public void showLongToastMessage(String message) {
        showToastMessage(false, message);
    }

    private void showToastMessage(boolean isShort, String message) {
        Toast.makeText(getActivity(), message, isShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
    }

    public void checkPermission(int type) {
        switch (type) {
            case PermissionType.CAMERA:
                checkSpecificPermission(Manifest.permission.CAMERA);
                break;

            case PermissionType.CONTACT:
                checkSpecificPermission(Manifest.permission.READ_CONTACTS);
                break;
        }
    }
    //endregion

    //region permission
    private void checkSpecificPermission(String permission) {
        int result = ContextCompat.checkSelfPermission(getActivity(), permission);

        if (result == PackageManager.PERMISSION_GRANTED) {
            onRequestPermissionResult(true);
        } else {
            //never ask again situation
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
                onRequestPermissionResult(false);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            onRequestPermissionResult(grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED);
        }
    }

    //endregion


    private void onRequestPermissionResult(boolean isGranted) {
        getPresenter().onRequestPermissionCallback(isGranted);
    }

}
