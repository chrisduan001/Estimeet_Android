package estimeet.meetup.ui.adapter.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.ByteArrayOutputStream;

import estimeet.meetup.R;
import estimeet.meetup.model.Friend;

/**
 * Created by AmyDuan on 16/03/16.
 */
@EViewGroup(R.layout.item_friends_list)
public class FriendListView extends RelativeLayout {

    @ViewById(R.id.friend_name) TextView friendName;
    @ViewById(R.id.friend_dp) ImageView friendDp;
    @ViewById(R.id.friend_action) ImageButton actionButton;

    private FriendListViewCallback callback;
    private Friend friend;

    public FriendListView(Context context) {
        super(context);
    }

    public void bind(Friend friend, Picasso picasso, FriendListViewCallback callback) {
        friendName.setText(friend.userName);
        this.callback = callback;
        this.friend = friend;

        if (friend.isFavourite) {
            actionButton.setImageResource(android.R.drawable.ic_input_delete);
        } else {
            actionButton.setImageResource(android.R.drawable.ic_input_add);
        }

        if (friend.image == null || friend.image.length <= 0) {
            picasso.load(friend.dpUri).into(friendDp, new Callback() {
                @Override
                public void onSuccess() {
                    storeFriendImage();
                }

                @Override
                public void onError() {

                }
            });
        } else {
            loadImageAsync(friend.image);
        }
    }

    @Background
    void loadImageAsync(byte[] image) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        displayImage(bitmap);
    }

    @Background
    void storeFriendImage() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bm = ((BitmapDrawable)friendDp.getDrawable()).getBitmap();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        friend.image = stream.toByteArray();
        callback.onUpdateFriend(friend);
    }

    @UiThread
    void displayImage(Bitmap bitmap) {
        friendDp.setImageBitmap(bitmap);
    }

    @Click(R.id.friend_action)
    protected void friendActionClicked() {
        friend.isFavourite = !friend.isFavourite;
        callback.onUpdateFriend(friend);
    }

    public interface FriendListViewCallback {
        void onUpdateFriend(Friend friend);
    }
}
