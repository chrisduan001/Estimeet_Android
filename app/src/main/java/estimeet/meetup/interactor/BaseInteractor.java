package estimeet.meetup.interactor;

import android.support.annotation.NonNull;

import java.util.Calendar;

import estimeet.meetup.DefaultSubscriber;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.TokenResponse;
import estimeet.meetup.model.User;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import rx.Observable;
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

    public BaseInteractor(ServiceHelper service, DataHelper data, MeetUpSharedPreference sp) {
        serviceHelper = service;
        dataHelper = data;
        sharedPreference = sp;
    }

    protected void cacheFunction(Observable<T> observable) {
        this.observable = observable;
    }

    private Observable<T> getCachedObservable() {
        return observable;
    }


    protected void clearCache() {
        observable = null;
    }

    protected void execute(@NonNull Observable<T> observable, @NonNull DefaultSubscriber<T> subscriber,
                           boolean cacheObservable) {
        if (cacheObservable) {
            cacheFunction(observable);
        }
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    protected Observable<TokenResponse> getTokenObservable(User user) {
        return serviceHelper.renewToken(user.id, user.password);
    }

    protected void makeRequest(User user, final Observable<T> observable,
                                           DefaultSubscriber<T> subscriber) {
        if (isTokenExpired(user.expiresTime)) {
            getTokenObservable(user)
                    .flatMap(new Func1<TokenResponse, Observable<T>>() {
                        @Override
                        public Observable<T> call(TokenResponse tokenResponse) {
                            sharedPreference.updateUserToken(tokenResponse.access_token,
                                    tokenResponse.expires_in);
                            return observable;
                        }
                    }).subscribe(subscriber);
        } else {
            execute(observable, subscriber, false);
        }
    }

    private boolean isTokenExpired(long expiresTime) {
        return Calendar.getInstance().getTimeInMillis()/1000 > expiresTime;
    }
}
