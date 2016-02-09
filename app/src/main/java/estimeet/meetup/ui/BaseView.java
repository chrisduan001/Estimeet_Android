package estimeet.meetup.ui;

/**
 * Created by AmyDuan on 6/02/16.
 */
public interface BaseView {
    void showShortToastMessage(String message);
    void showLongToastMessage(String message);

    void checkPermission(int permissionType);
}
