package estimeet.meetup.ui.adapter.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import estimeet.meetup.R;

/**
 * Created by AmyDuan on 2/04/16.
 */
@EViewGroup(R.layout.item_friend_session)
public class FriendSessionView extends RelativeLayout {

    private static final int SENT_SESSION = 0;
    private static final int RECEIVED_SESSION = 1;
    private static final int ACTIVE_SESSION = 2;

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

    public void setupRequestSentView() {
        setViewVisibility(SENT_SESSION);
    }

    public void setupRequestReceivedView() {
        setViewVisibility(RECEIVED_SESSION);
    }

    public void setupSessionInfoView() {
        setViewVisibility(ACTIVE_SESSION);
    }

    private void setViewVisibility(int type) {
        dpImage.setImageBitmap(null);
        switch (type) {
            case SENT_SESSION:
                setVisibility(GONE, activeSessionView, actionGroup);
                setVisibility(VISIBLE, friendName, sentMessage);
                break;
            case RECEIVED_SESSION:
                setVisibility(GONE, activeSessionView, sentMessage);
                setVisibility(VISIBLE, friendName, actionGroup);
                break;
            case ACTIVE_SESSION:
                setVisibility(GONE, pendingSession);
                setVisibility(VISIBLE, activeSessionView, sessionDistance, sessionEta, sessionLocation);
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
}
