package estimeet.meetup.ui.adapter.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import estimeet.meetup.R;
import estimeet.meetup.model.Friend;
import estimeet.meetup.model.FriendSession;

/**
 * Created by AmyDuan on 22/03/16.
 */
@EViewGroup(R.layout.item_friend_list)
public class FriendListView extends RelativeLayout {

    @ViewById(R.id.friend_name)             TextView friendName;
    @ViewById(R.id.friend_dp)               ImageView friendDp;
    @ViewById(R.id.viewgroup_view)          ViewGroup viewGroupView;
    @ViewById(R.id.request_view)            ViewGroup requestView;

    private FriendSession friendSession;

    public FriendListView(Context context) {
        super(context);
    }

    public void bindFriend(FriendSession friend) {
        friendSession = friend;
        friendName.setVisibility(VISIBLE);
        requestView.setVisibility(GONE);
        viewGroupView.setBackgroundColor(Color.parseColor("#9e9e9e"));
        friendName.setText(friend.getFriendName());
        if (friend.getFriendDp() != null) {
            loadImageAsync(friend.getFriendDp());
        }
    }

    public void setSwipeView() {
        viewGroupView.setBackgroundColor(Color.parseColor("#77a500"));
        requestView.setVisibility(VISIBLE);
        friendName.setVisibility(GONE);
    }

    public FriendSession getFriendSession() {
        return friendSession;
    }

    @Background
    void loadImageAsync(byte[] image) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        displayImage(bitmap);
    }

    @UiThread
    void displayImage(Bitmap bitmap) {
        friendDp.setImageBitmap(bitmap);
    }
}
