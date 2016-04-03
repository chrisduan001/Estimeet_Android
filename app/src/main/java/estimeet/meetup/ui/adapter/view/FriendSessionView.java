package estimeet.meetup.ui.adapter.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import estimeet.meetup.R;
import estimeet.meetup.model.FriendSession;
import estimeet.meetup.ui.adapter.FriendListAdapter;

/**
 * Created by AmyDuan on 2/04/16.
 */
@EViewGroup(R.layout.item_friend_session)
public class FriendSessionView extends RelativeLayout {

    @ViewById(R.id.friend_dp)                       ImageView dpImage;
    @ViewById(R.id.pending_session)                 ViewGroup pendingSession;
    @ViewById(R.id.friend_name)                     TextView  friendName;
    @ViewById(R.id.estimeet_sent_message)           TextView  sentMessage;
    @ViewById(R.id.receive_request_action_group)    ViewGroup actionGroup;
    @ViewById(R.id.session_active)                  ViewGroup activeSessionView;
    @ViewById(R.id.session_distance)                TextView  sessionDistance;
    @ViewById(R.id.session_eta)                     TextView  sessionEta;
    @ViewById(R.id.session_location)                TextView  sessionLocation;

    public FriendSessionView(Context context) {
        super(context);
    }

    public void bindView(FriendSession friendSession) {
        switch (friendSession.getType()) {
            case FriendListAdapter.SENT_SESSION:
            case FriendListAdapter.RECEIVED_SESSION:
                setupPendingSessionView(friendSession);
                break;
            case FriendListAdapter.ACTIVE_SESSION:
                setupSessionInfoView(friendSession);
                break;
            default:
                throw new RuntimeException("invalid session type");
        }

        loadImageAsync(friendSession.getFriendDp());
    }

    private void setupPendingSessionView(FriendSession friendSession) {
        friendName.setText(friendSession.getFriendName());
        setViewVisibility(friendSession.getType() == FriendListAdapter.RECEIVED_SESSION ?
                FriendListAdapter.RECEIVED_SESSION : FriendListAdapter.SENT_SESSION);
    }

    private void setupSessionInfoView(FriendSession friendSession) {
        sessionDistance.setText(friendSession.getDistance());
        sessionEta.setText(friendSession.getEta());
        sessionLocation.setText(friendSession.getLocation());
        setViewVisibility(FriendListAdapter.ACTIVE_SESSION);
    }

    private void setViewVisibility(int type) {
        switch (type) {
            case FriendListAdapter.SENT_SESSION:
                setVisibility(GONE, activeSessionView, actionGroup);
                setVisibility(VISIBLE, pendingSession, friendName, sentMessage);
                break;
            case FriendListAdapter.RECEIVED_SESSION:
                setVisibility(GONE, activeSessionView, sentMessage);
                setVisibility(VISIBLE, pendingSession, friendName, actionGroup);
                break;
            case FriendListAdapter.ACTIVE_SESSION:
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

    @Click(R.id.btn_cancel_session)
    protected void onCancelSession() {

    }

    @Click(R.id.action_accept_request)
    protected void onAcceptRequest() {

    }

    @Click(R.id.action_ignore_request)
    protected void onIgnoreRequest() {

    }

    @Background
    void loadImageAsync(byte[] image) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        displayImage(bitmap);
    }

    @UiThread
    void displayImage(Bitmap bitmap) {
        dpImage.setImageBitmap(bitmap);
    }
}
