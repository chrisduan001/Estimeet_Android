package estimeet.meetup;

import android.util.Log;

import estimeet.meetup.model.BaseModel;
import rx.Subscriber;

/**
 * Created by AmyDuan on 7/02/16.
 */
public class DefaultSubscriber<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        Log.d(getClass().getSimpleName(), "Error occurred");
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
}
