package estimeet.meetup.ui.adapter.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.lang.ref.WeakReference;

import estimeet.meetup.R;
import estimeet.meetup.factory.SessionActivityFactory;
import estimeet.meetup.factory.TravelInfoFactory;
import estimeet.meetup.model.FriendSession;
import estimeet.meetup.factory.SessionCreationFactory;
import estimeet.meetup.util.CircleTransform;

/**
 * Created by AmyDuan on 2/04/16.
 */
@EViewGroup(R.layout.item_friend_session)
public class FriendSessionView extends RelativeLayout {
    private static final String TAG = FriendSessionView.class.getSimpleName();
    //time to waiting before show friend location not available message (milliseconds)
    private static final int MAX_WAITING_TIME = 1000 * 3 * 60; //3 minutes

    @ViewById(R.id.friend_dp)                       ImageView dpImage;
    @ViewById(R.id.pending_session)                 ViewGroup pendingSession;
    @ViewById(R.id.friend_name)                     TextView  friendName;
    @ViewById(R.id.estimeet_sent_message)           TextView  sentMessage;
    @ViewById(R.id.receive_request_action_group)    ViewGroup actionGroup;
    @ViewById(R.id.session_active)                  ViewGroup activeSessionView;
    @ViewById(R.id.session_distance)                TextView  sessionDistance;
    @ViewById(R.id.session_eta)                     TextView  sessionEta;
    @ViewById(R.id.session_location)                TextView  sessionLocation;
    @ViewById(R.id.active_session_friend_name)      TextView  sessionFriendName;
    @ViewById(R.id.active_session_refresh_message)      TextView  sessionRefreshMessage;
    @ViewById(R.id.reward_progress)                 ProgressBar progressBar;

    private FriendSession friendSession;
    private WeakReference<SessionActionCallback> callback;

    public FriendSessionView(Context context) {
        super(context);
    }

    //region view
    public void bindView(FriendSession friendSession, SessionActionCallback callback) {
        this.friendSession = friendSession;
        this.callback = new WeakReference<>(callback);

        switch (friendSession.getType()) {
            case SessionActivityFactory.SENT_SESSION:
                setupRequestSessionView();
                break;
            case SessionActivityFactory.RECEIVED_SESSION:
                setupReceivedSessionView();
                break;
            case SessionActivityFactory.ACTIVE_SESSION:
                setupSessionInfoView();
                break;
            default:
                throw new RuntimeException("invalid session type");
        }

        if (friendSession.getFriendDp() != null) {
            loadImageAsync(friendSession.getFriendDp());
        }
    }

    private void setupRequestSessionView() {
        friendName.setText(friendSession.getFriendName());
        setViewVisibility(SessionActivityFactory.SENT_SESSION);
    }

    private void setupReceivedSessionView() {
        //// TODO: 7/04/16 need a proper layout //HD removed duration text
        friendName.setText(friendSession.getFriendName());
        setViewVisibility(SessionActivityFactory.RECEIVED_SESSION);
    }

    private void setupSessionInfoView() {
        setViewVisibility(SessionActivityFactory.ACTIVE_SESSION);
        if (friendSession.getLocation() == null) {
            showEmptyActivitySessionView();
        } else {
            showActivitySessionView();
        }

        setProgressBarView();
    }

    //check how long has user been waiting for their friend location to update
    //if > MAX_WAITING_TIME, show location not available message
    private boolean isWaitingTimeExceeded() {
        return friendSession.getWaitingTime() > 0
                && System.currentTimeMillis() - friendSession.getWaitingTime() > MAX_WAITING_TIME;
    }

    private void showEmptyActivitySessionView() {
        //// TODO: 16/06/16 need to find a proper ui for when unable to get friend's location due to network connection or they turned off location manually
        sessionFriendName.setText(friendSession.getFriendName() + (isWaitingTimeExceeded() ? "Location unavailable" : ""));
        sessionRefreshMessage.setText(getContext().getString(R.string.session_refresh_message));
        setVisibility(VISIBLE, sessionFriendName, sessionRefreshMessage);
        setVisibility(GONE, sessionDistance, sessionEta, sessionLocation);
    }

    private void showActivitySessionView() {
        //// TODO: 16/06/16 find a proper ui to handle the message
        String expireString = TravelInfoFactory.isLocationDataExpired(friendSession.getDateUpdated()) ?
                getContext().getString(R.string.expired_string) : "";
        expireString = expireString + (isWaitingTimeExceeded() ? "(Location unavailable)" : "");

        sessionDistance.setText(String.format("%s %s %s",
                getContext().getString(R.string.distance_title),
                TravelInfoFactory.getDistanceString((double)friendSession.getDistance(), getContext()),
                expireString));

        String locationString = getContext().getString(R.string.location_title);
        sessionLocation.setText(String.format("%s %s", locationString,
                TextUtils.isEmpty(friendSession.getLocation()) ?
                getContext().getString(R.string.location_unknown) : friendSession.getLocation() + expireString));

        String etaString = getContext().getString(R.string.eta_title) +
                TravelInfoFactory.getEtaString(friendSession.getEta(), getContext())
                + TravelInfoFactory.getTravelModeString(friendSession.getTravelMode(),getContext())
                + expireString;
        sessionEta.setText(etaString);

        setVisibility(GONE, sessionFriendName, sessionRefreshMessage);
        setVisibility(VISIBLE, sessionDistance, sessionEta, sessionLocation);
    }

    private void setViewVisibility(int type) {
        switch (type) {
            case SessionActivityFactory.SENT_SESSION:
                setVisibility(GONE, activeSessionView, actionGroup, progressBar);
                setVisibility(VISIBLE, pendingSession, friendName, sentMessage);
                break;
            case SessionActivityFactory.RECEIVED_SESSION:
                setVisibility(GONE, activeSessionView, sentMessage, progressBar);
                setVisibility(VISIBLE, pendingSession, friendName, actionGroup);
                break;
            case SessionActivityFactory.ACTIVE_SESSION:
                setVisibility(GONE, pendingSession);
                setVisibility(VISIBLE, activeSessionView);
                break;
        }
    }

    private void setVisibility(int visibility, View...views) {
        for (View view: views) {
            view.setVisibility(visibility);
        }
    }

    public void setProgressBarView() {
        if (progressBar.getVisibility() == GONE) progressBar.setVisibility(VISIBLE);
        double systemTimeToExpire = friendSession.getDateCreated() + friendSession.getTimeToExpireInMilli();
        double timeLeft = systemTimeToExpire - System.currentTimeMillis();
        int percentage = (int)((timeLeft / friendSession.getTimeToExpireInMilli()) * 100);
        Log.d(TAG, "setProgressBarTimer: " + friendSession.getFriendName() + " precentage: " + percentage);
        progressBar.setProgress(percentage);
    }

    @UiThread
    void displayImage(Bitmap bitmap) {
        dpImage.setImageBitmap(bitmap);
    }

    //endregion

    @Click(R.id.btn_cancel_session)
    protected void onCancelSession() {
        cancelSession();
    }
    
    @Click(R.id.action_accept_request)
    protected void onAcceptRequest() {
        acceptRequest();
    }

    @Click(R.id.action_ignore_request)
    protected void onIgnoreRequest() {
        ignoreRequest();
    }

    //region logic
    @Background
    void cancelSession() {
        callback.get().onCancelSession(friendSession);
    }

    @Background
    void acceptRequest() {
        callback.get().onAcceptRequest(friendSession);
    }

    @Background
    void ignoreRequest() {
        callback.get().onIgnoreRequest(friendSession);
    }

    @Background
    void loadImageAsync(byte[] image) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        displayImage(CircleTransform.transformToCircleBitmap(bitmap));
    }

    public FriendSession getFriendSession() {
        return friendSession;
    }
    //endregion

    public interface SessionActionCallback {
        void onCancelSession(FriendSession friendSession);
        void onAcceptRequest(FriendSession friendSession);
        void onIgnoreRequest(FriendSession friendSession);
    }
}
