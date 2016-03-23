package estimeet.meetup.ui.adapter.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

/**
 * Created by AmyDuan on 22/03/16.
 */
@EViewGroup(R.layout.item_friend_list)
public class FriendListView extends RelativeLayout {

    @ViewById(R.id.friend_name)             TextView friendName;
    @ViewById(R.id.friend_dp)               ImageView friendDp;
    @ViewById(R.id.viewgroup_view)          ViewGroup viewGroupView;

    public FriendListView(Context context) {
        super(context);
    }

    public void bindFriend(Friend friend) {
        friendName.setText(friend.userName);
        if (friend.image != null) {
            loadImageAsync(friend.image);
        }
    }

    public void setBackground() {
        viewGroupView.setBackgroundColor(Color.parseColor("#77a500"));
        friendDp.setVisibility(GONE);
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
