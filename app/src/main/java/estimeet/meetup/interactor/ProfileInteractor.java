package estimeet.meetup.interactor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.inject.Inject;

import estimeet.meetup.DefaultSubscriber;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.PostModel.UpdateModel;
import estimeet.meetup.model.User;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import estimeet.meetup.util.CircleTransform;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by AmyDuan on 19/02/16.
 */
public class ProfileInteractor extends BaseInteractor<User> {

    private ProfileListener listener;
    private ProfileUpdateSubscriber subscriber;

    private String imageString;

    private boolean isRegister;

    @Inject
    public ProfileInteractor(ServiceHelper service, DataHelper data, MeetUpSharedPreference sp) {
        super(service, data, sp);
    }

    //region present call
    public void call(final ProfileListener listener) {
        this.listener = listener;
    }

    public void getUserDpString(String dpUri) {
        String dpString = sharedPreference.getUserDpImageString();

        if (dpString != null) {
            byte[] b = Base64.decode(sharedPreference.getUserDpImageString(), Base64.DEFAULT);
            processBitmapToCircle(BitmapFactory.decodeByteArray(b, 0, b.length));
        } else {
            Observable.just(dpUri)
                    .map(new Func1<String, Bitmap>() {
                        @Override
                        public Bitmap call(String s) {
                            try {
                                return BitmapFactory.decodeStream((InputStream)new URL(s).getContent());
                            } catch (IOException e) { return  null; }
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Action1<Bitmap>() {
                        @Override
                        public void call(Bitmap bitmap) {
                            sharedPreference.setUserDpData(changeBitmapToString(bitmap), null);
                            processBitmapToCircle(bitmap);
                        }
                    });
        }
    }

    private void processBitmapToCircle(Bitmap bitmap) {
        listener.onGetUserDp(CircleTransform.transformToCircleBitmap(bitmap));
    }

    public void initUpdateProfile(String name, Bitmap imgBitmap, boolean isRegister) {
        this.imageString = changeBitmapToString(imgBitmap);
        this.isRegister = isRegister;
        subscriber = new ProfileUpdateSubscriber();
        baseUser = sharedPreference.getUserFromSp();
        baseUser.userName = name;
        makeRequest(subscriber, true);
    }

    private String changeBitmapToString(Bitmap imageBitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
    }

    public void unSubscribe() {
        if (subscriber != null && !subscriber.isUnsubscribed()) {
            subscriber.unsubscribe();
        }
    }
    //endregion

    @Override
    protected Observable<User> getObservable() {
        UpdateModel updateModel = new UpdateModel(baseUser.id, baseUser.userId, baseUser.userName, imageString);
        return serviceHelper.updateProfile(baseUser.token, isRegister, updateModel);
    }

    //region networkcall
    public void initFbRequest(AccessToken accessToken) {
        Bundle bundle = new Bundle();
        bundle.putString("fields", "id,name,picture");

        new GraphRequest(accessToken, "me", bundle, HttpMethod.GET, new GraphRequest.Callback() {

            @Override
            public void onCompleted(GraphResponse response) {
                try {
                    JSONObject mainObj = response.getJSONObject();
                    String id = response.getJSONObject().getString("id");
                    String dpUri = "https://graph.facebook.com/" + id + "/picture?type=large";
                    String name = mainObj.getString("name");
                    listener.onFacebookResponse(name, dpUri);

                } catch (Exception e) {
                    throw new RuntimeException("Facebook parse error");
                }
            }
        }).executeAsync();
    }
    //endregion

    private class ProfileUpdateSubscriber extends DefaultSubscriber<User> {

        @Override
        public void onNext(User user) {
            //super.onnext checks if there is any error with model, if there is throw an error with error code
            super.onNext(user);

            sharedPreference.updateUserProfile(user.userName, user.dpUri, imageString);
            listener.onUpdateProfileSuccessful();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        protected void onAuthError() {
            sharedPreference.removeSharedPreference();
            listener.onAuthFailed();
        }

        @Override
        protected void onError(String err) {
            listener.onError(err);
        }
    }

    public interface ProfileListener extends BaseListener {
        void onFacebookResponse(String name, String dpUri);
        void onUpdateProfileSuccessful();
        void onGetUserDp(Bitmap bitmap);
    }
}
