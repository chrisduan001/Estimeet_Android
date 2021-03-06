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
public abstract class BaseInteractor<T> {

    protected ServiceHelper serviceHelper;
    protected DataHelper dataHelper;
    protected MeetUpSharedPreference sharedPreference;

    protected User baseUser;

    public BaseInteractor(ServiceHelper service, DataHelper data, MeetUpSharedPreference sp) {
        serviceHelper = service;
        dataHelper = data;
        sharedPreference = sp;

        baseUser = sp.getUserFromSp();
    }

    private void execute(@NonNull Observable<T> observable, @NonNull DefaultSubscriber<T> subscriber) {
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    protected Observable<TokenResponse> getTokenObservable() {
        return serviceHelper.renewToken(baseUser.id, baseUser.password);
    }

    protected abstract Observable<T> getObservable();

    protected void makeRequest(@NonNull DefaultSubscriber<T> subscriber, boolean needsAuth) {
        if (needsAuth && isTokenExpired(baseUser.expiresTime)) {
            //check token expire date first, if token is expired then needs to make request to renew token
            execute(getTokenObservable()
                    .flatMap(new Func1<TokenResponse, Observable<T>>() {
                        @Override
                        public Observable<T> call(TokenResponse tokenResponse) {
                            sharedPreference.updateUserToken(tokenResponse.access_token,
                                    tokenResponse.expires_in);
                            baseUser = sharedPreference.getUserFromSp();
                            return getObservable();
                        }
                    }), subscriber);
        } else {
            execute(getObservable(), subscriber);
        }
    }

    private boolean isTokenExpired(long expiresTime) {
        return Calendar.getInstance().getTimeInMillis()/1000 > expiresTime;
    }
}
