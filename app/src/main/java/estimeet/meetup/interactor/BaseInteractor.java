package estimeet.meetup.interactor;

import android.support.annotation.NonNull;

import estimeet.meetup.DefaultSubscriber;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.TokenResponse;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by AmyDuan on 6/02/16.
 */
public class BaseInteractor<T> {

    protected ServiceHelper serviceHelper;
    protected DataHelper dataHelper;
    protected MeetUpSharedPreference sharedPreference;

    private Observable<T> observable;
    private DefaultSubscriber<T> subscriber;

    public BaseInteractor(ServiceHelper service, DataHelper data, MeetUpSharedPreference sp) {
        serviceHelper = service;
        dataHelper = data;
        sharedPreference = sp;
    }

    protected void cacheFunction(Observable<T> observable, DefaultSubscriber<T> subscriber) {
        this.observable = observable;
        this.subscriber = subscriber;
    }

    private Observable<T> getCachedObservable() {
        return observable;
    }

    private DefaultSubscriber<T> getCachedSubscriber() {
        return subscriber;
    }

    protected void clearCache() {
        observable = null;
        subscriber = null;
    }

    protected void execute(@NonNull Observable<T> observable, @NonNull DefaultSubscriber<T> subscriber,
                           boolean cacheObservable) {
        if (cacheObservable) {
            cacheFunction(observable, subscriber);
        }
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    protected void renewAuthToken(int id, String deviceId, BaseListener listener) {
        serviceHelper.renewToken(id, deviceId).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RenewTokenSubscriber(listener));
    }

    //sign out user if authentication fails
    private class RenewTokenSubscriber extends DefaultSubscriber<TokenResponse> {

        private BaseListener listener;
        public RenewTokenSubscriber(BaseListener listener) {
            this.listener = listener;
        }

        @Override
        public void onNext(TokenResponse tokenResponse) {
            sharedPreference.updateUserToken(tokenResponse.access_token);
            execute(getCachedObservable(), getCachedSubscriber(), false);
        }

        @Override
        public void onError(Throwable e) {
            if (e.getLocalizedMessage().equals("401")) {
                sharedPreference.removeSharedPreference();
                listener.onAuthFailed();
            }
        }
    }
}
