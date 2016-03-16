package estimeet.meetup.ui.adapter.view;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EView;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import estimeet.meetup.R;
import estimeet.meetup.model.Friend;

/**
 * Created by AmyDuan on 16/03/16.
 */
@EViewGroup(R.layout.item_friends_list)
public class FriendListView extends RelativeLayout {

    @ViewById(R.id.friend_name) TextView friendName;
    @ViewById(R.id.friend_action) ImageButton button;

    public FriendListView(Context context) {
        super(context);
    }

    public void bind(Friend friend) {
        friendName.setText(friend.userName);

    }

    @Click(R.id.main)
    void actionClicked() {
        Log.d("tag", "actionClicked: ");
    }
}
