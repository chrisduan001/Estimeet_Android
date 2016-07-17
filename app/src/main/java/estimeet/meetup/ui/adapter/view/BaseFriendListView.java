package estimeet.meetup.ui.adapter.view;

import android.content.Context;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.lang.ref.WeakReference;

import estimeet.meetup.R;
import estimeet.meetup.model.Friend;

/**
 * Created by AmyDuan on 16/07/16.
 */
@EViewGroup
public class BaseFriendListView extends RelativeLayout {

    @ViewById(R.id.friend_name) TextView friendName;
    @ViewById(R.id.friend_dp) ImageView friendDp;
    @ViewById(R.id.friend_action) ImageButton actionButton;
    @ViewById(R.id.friend_header) TextView friendHeader;


    protected Friend friend;

    public BaseFriendListView(Context context) {
        super(context);
    }

    protected void bind(Friend friend) {
        this.friend = friend;

        setView();
        setManageFriendAction();
    }

    private void setView() {
        friendName.setText(friend.userName);
        friendHeader.setVisibility(GONE);
    }

    private void setManageFriendAction() {
        if (friend.isFavourite) {
            actionButton.setImageResource(R.drawable.ic_person_add_green_48dp);
        } else {
            actionButton.setImageResource(R.drawable.ic_person_add_grey_48dp);
        }
    }
}
