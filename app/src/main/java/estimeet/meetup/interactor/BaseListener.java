package estimeet.meetup.interactor;

/**
 * Created by AmyDuan on 17/02/16.
 */
public interface BaseListener {
    void onError(String errorMessage);
    void onAuthFailed();
}
