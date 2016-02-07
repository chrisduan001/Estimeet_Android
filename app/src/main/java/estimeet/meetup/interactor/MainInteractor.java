package estimeet.meetup.interactor;

import javax.inject.Inject;

import estimeet.meetup.DefaultSubscriber;
import estimeet.meetup.model.User;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by AmyDuan on 6/02/16.
 */
public class MainInteractor extends BaseInteractor {

    @Inject
    public MainInteractor(ServiceHelper serviceHelper, DataHelper dataHelper) {
        super(serviceHelper, dataHelper);
    }

    public void queryUser() {
        serviceHelper.getUser(3).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new UserSubscriber(dataHelper));
    }

    private static class UserSubscriber extends DefaultSubscriber<User> {
        private final DataHelper dataHelper;

        public UserSubscriber(DataHelper dataHelper) {
            this.dataHelper = dataHelper;
        }

        @Override
        public void onNext(User user) {
            //todo..dummy data for template only
            User user1 = new User();
            user1.firstName = "";
            user1.lastName = "";
            user1.userID = 1;
            dataHelper.insertUserData(user1);
        }

        @Override
        public void onCompleted() {
            super.onCompleted();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }
    }

    private static class PauseUserSubscriber extends DefaultSubscriber<String> {
        private final DataHelper dataHelper;

        public PauseUserSubscriber(DataHelper dataHelper) {
            this.dataHelper = dataHelper;
        }

        @Override
        public void onNext(String s) {
            super.onNext(s);
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }
    }
}
