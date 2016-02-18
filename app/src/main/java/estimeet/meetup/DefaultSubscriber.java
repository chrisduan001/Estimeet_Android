package estimeet.meetup;

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

    }

    @Override
    public void onNext(T t) {
    }

    protected void throwError(String errorMessage) {
        onError(new Throwable(errorMessage));
    }
}
