package estimeet.meetup.ui.adapter.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.lang.ref.WeakReference;

import estimeet.meetup.R;
import estimeet.meetup.model.FriendSession;
import estimeet.meetup.ui.adapter.FriendListAdapter;
import estimeet.meetup.util.SessionFactory;

/**
 * Created by AmyDuan on 2/04/16.
 */
@EViewGroup(R.layout.item_friend_session)
public class FriendSessionView extends RelativeLayout {
    private static final String TAG = FriendSessionView.class.getSimpleName();

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
            case FriendListAdapter.SENT_SESSION:
                setupRequestSessionView();
                break;
            case FriendListAdapter.RECEIVED_SESSION:
                setupReceivedSessionView();
                break;
            case FriendListAdapter.ACTIVE_SESSION:
                setupSessionInfoView();
                break;
            default:
                throw new RuntimeException("invalid session type");
        }

        loadImageAsync(friendSession.getFriendDp());
    }

    private void setupRequestSessionView() {
        friendName.setText(friendSession.getFriendName());
        setViewVisibility(FriendListAdapter.SENT_SESSION);
    }

    private void setupReceivedSessionView() {
        //// TODO: 7/04/16 need a proper layout
        friendName.setText(friendSession.getFriendName() + " " +
                SessionFactory.getSessionLengthString(friendSession.getRequestedLength(), getContext()));
        setViewVisibility(FriendListAdapter.RECEIVED_SESSION);
    }

    private void setupSessionInfoView() {
        setViewVisibility(FriendListAdapter.ACTIVE_SESSION);
        showEmptyActivitySessionView();
    }

    private void showEmptyActivitySessionView() {
        sessionFriendName.setText(friendSession.getFriendName());
        setVisibility(VISIBLE, sessionFriendName);
        setVisibility(GONE, sessionDistance, sessionEta, sessionLocation);
    }

    private void showActivitySessionView() {
        sessionDistance.setText(friendSession.getDistance());
        sessionEta.setText(friendSession.getEta());
        sessionLocation.setText(friendSession.getLocation());
        setVisibility(GONE, sessionFriendName);
        setVisibility(VISIBLE, sessionDistance, sessionEta, sessionLocation);
    }

    private void setViewVisibility(int type) {
        switch (type) {
            case FriendListAdapter.SENT_SESSION:
                setVisibility(GONE, activeSessionView, actionGroup, progressBar);
                setVisibility(VISIBLE, pendingSession, friendName, sentMessage);
                break;
            case FriendListAdapter.RECEIVED_SESSION:
                setVisibility(GONE, activeSessionView, sentMessage, progressBar);
                setVisibility(VISIBLE, pendingSession, friendName, actionGroup);
                break;
            case FriendListAdapter.ACTIVE_SESSION:
                setVisibility(GONE, pendingSession);
                setVisibility(VISIBLE, activeSessionView, progressBar);
                break;
        }
    }

    private void setVisibility(int visibility, View...views) {
        for (View view: views) {
            view.setVisibility(visibility);
        }
    }

    public void setProgressBarView() {
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
        displayImage(bitmap);
    }
    //endregion

    public interface SessionActionCallback {
        void onCancelSession(FriendSession friendSession);
        void onAcceptRequest(FriendSession friendSession);
        void onIgnoreRequest(FriendSession friendSession);
    }
}
