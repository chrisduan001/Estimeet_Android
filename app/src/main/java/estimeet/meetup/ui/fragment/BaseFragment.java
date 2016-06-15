package estimeet.meetup.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.digits.sdk.android.Digits;

import java.util.ArrayList;
import java.util.List;

import estimeet.meetup.R;
import estimeet.meetup.di.HasComponent;
import estimeet.meetup.ui.presenter.BasePresenter;

/**
 * Created by AmyDuan on 6/02/16.
 */
public abstract class BaseFragment extends Fragment {

    private static final int PERMISSION_REQUEST_CODE = 100;

    public static final int ERROR_CODE_500 = 500;
    public static final int ERROR_SESSION_GENERIC = 700;
    public static final int ERROR_SESSION_EXPIRED = 701;
    public static final int ERROR_GENERIC = 1000;
    public static final int ERROR_LOCATION_PERMISSION = 1001;
    public static final int ERROR_PLAY_SERVICE = 1002;
    public static final int ERROR_NETWORK_REQUEST = 2013;

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

    protected abstract ProgressBar getProgressBar();

    @SuppressWarnings("unchecked")
    protected <T> T getComponent(Class<T> componentType) {
        return componentType.cast(((HasComponent<T>) getActivity()).getComponent());
    }

    //region fragment call
    protected void showShortToastMessage(String message) {
        showToastMessage(true, message);
    }

    protected void showProgressDialog(String message) {
        getProgressBar().setVisibility(View.VISIBLE);
    }

    //with dialog dismiss listener
    protected void showAlertDialog(String title, String message, DialogInterface.OnDismissListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(getString(R.string.button_ok), null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setOnDismissListener(listener);
    }

    public void onError(String errCode) {
        int code = Integer.parseInt(errCode);

        switch (code) {
            case 500:
                showShortToastMessage(getString(R.string.error_500));
                break;
            case 1000:
                showShortToastMessage(getString(R.string.error_generic));
                break;
            case ERROR_SESSION_GENERIC:
                showShortToastMessage(getString(R.string.error_session_generic));
                break;
            case ERROR_LOCATION_PERMISSION:
                showShortToastMessage(getString(R.string.error_location_permission));
                break;
            case ERROR_PLAY_SERVICE:
                showShortToastMessage(getString(R.string.error_play_service));
                break;
            case ERROR_SESSION_EXPIRED:
                showShortToastMessage(getString(R.string.error_session_expired));
                break;
            case 2013:
                showShortToastMessage(getString(R.string.error_network));
                break;
        }
    }

    private void showToastMessage(boolean isShort, String message) {
        Toast.makeText(getActivity(), message, isShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
    }
    //endregion

    //region presenter callback
    public void dismissProgressDialog() {
        getProgressBar().setVisibility(View.INVISIBLE);
    }

    public void onAuthFailed() {
        Digits.getSessionManager().clearActiveSession();
    }
    //endregion

    //region permission
    //called from the presenter
    public void checkPermission(String... permissions) {
        List<String> permissionsToRequest = new ArrayList<>();
        for (String permission: permissions) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                //never ask again situation
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
                    onPermissionResult(false);
                    return;
                }
                permissionsToRequest.add(permission);
            }
        }

        if (permissionsToRequest.size() > 0) {
            requestPermission(permissionsToRequest.toArray(new String[permissionsToRequest.size()]));
        } else {
            onPermissionResult(true);
        }
    }

    private void requestPermission(String... permissionsRequired) {
        ActivityCompat.requestPermissions(getActivity(), permissionsRequired, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean isPermissionGranted = true;
            if (grantResults.length > 0) {
                for (int result: grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        isPermissionGranted = false;
                    }
                }
            }
            onPermissionResult(isPermissionGranted);
        }
    }

    private void onPermissionResult(boolean result) {
        getPresenter().onPermissionResult(result);
    }
    //endregion
}
