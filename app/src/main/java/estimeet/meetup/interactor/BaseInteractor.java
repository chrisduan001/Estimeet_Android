package estimeet.meetup.interactor;

import android.support.annotation.NonNull;

import estimeet.meetup.DefaultSubscriber;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
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

    protected void renewAuthToken(long id, String deviceId) {
        execute(getRenewTokenObservable(id, deviceId), getCachedSubscriber(), false);
    }

    private Observable<T> getRenewTokenObservable(long id, String deviceId) {
        return serviceHelper.renewToken(id, deviceId).flatMap(new Func1<String, Observable<T>>() {
            @Override
            public Observable<T> call(String s) {
                sharedPreference.updateUserToken(s);
                return getCachedObservable();
            }
        });
    }
}
