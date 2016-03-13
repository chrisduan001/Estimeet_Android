package estimeet.meetup.ui;

/**
 * Created by AmyDuan on 6/02/16.
 */
public interface BaseView {
    void onError(String errCode);

    void checkPermission(String... permissions);

    void showProgressDialog();
    void dismissProgressDialog();

    void onAuthFailed();
}
