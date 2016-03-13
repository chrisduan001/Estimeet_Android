package estimeet.meetup;

import android.util.Log;

import estimeet.meetup.interactor.BaseListener;
import estimeet.meetup.model.BaseModel;
import rx.Subscriber;

/**
 * Created by AmyDuan on 7/02/16.
 */
public abstract class DefaultSubscriber<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        Log.d(getClass().getSimpleName(), "Error occurred");
        try {
            int errCode = Integer.parseInt(e.getLocalizedMessage());
            if (errCode >= 400 && errCode < 500 || errCode == 100) {
                onAuthError();
            } else {
                onError(errCode + "");
            }
        } catch (Exception e1) { //not digits
            onError("1000");
        }
    }

    @Override
    public void onNext(T t) {
        if (t instanceof BaseModel) {
            BaseModel model = (BaseModel) t;
            if (model.hasError()) {
                throw new RuntimeException(model.errorCode + "");
            }
        }
    }

    protected abstract void onAuthError();
    protected abstract void onError(String err);
}
