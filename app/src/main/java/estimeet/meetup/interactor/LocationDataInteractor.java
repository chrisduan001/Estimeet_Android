package estimeet.meetup.interactor;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import estimeet.meetup.DefaultSubscriber;
import estimeet.meetup.model.FriendSession;
import estimeet.meetup.model.ListItem;
import estimeet.meetup.model.LocationModel;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.PostModel.RequestLocationModel;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import estimeet.meetup.factory.SessionCreationFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by AmyDuan on 11/04/16.
 */
public class LocationDataInteractor extends BaseInteractor<ListItem<LocationModel>> {

    private FriendSession friendSession;
    private BaseListener listener;

    private List<FriendSession> sessionList;

    @Inject
    public LocationDataInteractor(ServiceHelper service, DataHelper data, MeetUpSharedPreference sp) {
        super(service, data, sp);
    }

    public void call(BaseListener listener) {
        this.listener = listener;
    }

    public void onRequestLocation(FriendSession friendSession) {
        this.friendSession = friendSession;
        makeRequest(new RequestLocationSubscriber(), true);
    }

    public void requestPendingLocationData() {
        String friendsId = sharedPreference.getAvailableFriendsId();

        Observable.just(friendsId).map(new Func1<String, List<FriendSession>>() {
            @Override
            public List<FriendSession> call(String s) {
                String[] idArray = s.split(" ");
                List<FriendSession> friendSessions = new ArrayList<>();
                for (String id : idArray) {
                    FriendSession session = dataHelper.getSession(Integer.parseInt(id));
                    if (session != null) {
                        friendSessions.add(session);
                    }
                }
                return friendSessions;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<FriendSession>>() {
                    @Override
                    public void call(List<FriendSession> friendSessions) {
                        sessionList = friendSessions;
                        processLocationRequest();

                        sharedPreference.saveAvailableFriendId(null);
                    }
                });
    }

    //process a list of location request
    //need to process items one by one
    private void processLocationRequest() {
        if (sessionList != null && sessionList.size() > 0) {
            onRequestLocation(sessionList.get(0));
        } else {
            sessionList = null;
        }
    }

    //-1, travel mode not specified, will use the travel mode that provided by friend
    @Override
    protected Observable<ListItem<LocationModel>> getObservable() {
        return serviceHelper.getTravelInfo(baseUser.token, new RequestLocationModel(baseUser.id, baseUser.userId,
                friendSession.getFriendId(), dataHelper.getFriend(friendSession.getFriendId()).userId,
                friendSession.getSessionId(), friendSession.getSessionLId(), -1, sharedPreference.getUserGeoCoord()));
    }

    private class RequestLocationSubscriber extends DefaultSubscriber<ListItem<LocationModel>> {

        @Override
        public void onNext(ListItem<LocationModel> locationModelListItem) {
            super.onNext(locationModelListItem);

            LocationModel model = locationModelListItem.items.get(0);
            dataHelper.updateSession(SessionCreationFactory.updateDistanceEta(model, friendSession));

            processLocationRequest();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);

            processLocationRequest();
        }

        @Override
        protected void onAuthError() {}

        @Override
        protected void onError(String err) {
            listener.onError(err);
        }
    }
}
