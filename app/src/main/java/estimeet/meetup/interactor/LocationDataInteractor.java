package estimeet.meetup.interactor;

import javax.inject.Inject;

import estimeet.meetup.DefaultSubscriber;
import estimeet.meetup.model.FriendSession;
import estimeet.meetup.model.ListItem;
import estimeet.meetup.model.LocationModel;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.PostModel.RequestLocationModel;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.network.ServiceHelper;
import rx.Observable;

/**
 * Created by AmyDuan on 11/04/16.
 */
public class LocationDataInteractor extends BaseInteractor<ListItem<LocationModel>> {

    private FriendSession friendSession;
    private BaseListener listener;

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

    @Override
    protected Observable<ListItem<LocationModel>> getObservable() {
        return serviceHelper.getTravelInfo(baseUser.token, new RequestLocationModel(baseUser.id,
                friendSession.getFriendId(), dataHelper.getFriend(friendSession.getFriendId()).userId,
                friendSession.getSessionId(), friendSession.getSessionLId(), 0, ""));
    }

    private class RequestLocationSubscriber extends DefaultSubscriber<ListItem<LocationModel>> {

        @Override
        public void onNext(ListItem<LocationModel> locationModelListItem) {
            super.onNext(locationModelListItem);

            LocationModel model = locationModelListItem.items.get(0);
            friendSession.setDistance(model.distance + "");
            friendSession.setEta(model.eta + "");
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        protected void onAuthError() {}

        @Override
        protected void onError(String err) {
            listener.onError(err);
        }
    }
}
