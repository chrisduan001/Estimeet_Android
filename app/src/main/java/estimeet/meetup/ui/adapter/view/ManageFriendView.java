package estimeet.meetup.ui.adapter.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;

import estimeet.meetup.R;
import estimeet.meetup.model.Friend;
import estimeet.meetup.util.CircleTransform;

/**
 * Created by AmyDuan on 16/03/16.
 */
@EViewGroup(R.layout.item_manage_friends_list)
public class ManageFriendView extends BaseFriendListView {

    private WeakReference<FriendListViewCallback> callback;

    public ManageFriendView(Context context) {
        super(context);
    }

    public void bind(Friend friend, Picasso picasso, CircleTransform circleTransform, FriendListViewCallback callback) {
        super.bind(friend);

        this.callback = new WeakReference<>(callback);
        loadUserDpImage(picasso, circleTransform);
    }

    private void loadUserDpImage(Picasso picasso, CircleTransform circleTransform) {
        //load image from imageuri and save to local db if it is not done yet
        if (friend.image == null || friend.image.length <= 0) {
            picasso.load(friend.dpUri).transform(circleTransform).into(friendDp, new Callback() {
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

    public void showSectionHeader(String header) {
        friendHeader.setVisibility(VISIBLE);
        friendHeader.setText(header);
    }

    @Background
    void loadImageAsync(byte[] image) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        displayImage(CircleTransform.transformToCircleBitmap(bitmap));
    }

    @Background
    void storeFriendImage() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bm = ((BitmapDrawable)friendDp.getDrawable()).getBitmap();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        friend.image = stream.toByteArray();
        callback.get().onUpdateFriend(friend);
    }

    @UiThread
    void displayImage(Bitmap bitmap) {
        friendDp.setImageBitmap(bitmap);
    }

    @Click(R.id.friend_action)
    protected void friendActionClicked() {
        friend.isFavourite = !friend.isFavourite;
        callback.get().onUpdateFriend(friend);
    }

    public interface FriendListViewCallback {
        void onUpdateFriend(Friend friend);
    }
}
