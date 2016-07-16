package estimeet.meetup.ui.adapter.view;

import android.content.Context;

import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;

import java.lang.ref.WeakReference;

import estimeet.meetup.R;
import estimeet.meetup.model.Friend;
import estimeet.meetup.util.CircleTransform;

/**
 * Created by AmyDuan on 16/07/16.
 */
@EViewGroup(R.layout.item_manage_friends_list)
public class SearchFriendView extends BaseFriendListView {

    private WeakReference<SearchFriendViewCallback> callback;

    public SearchFriendView(Context context) {
        super(context);
    }

    public void bind(Friend friend, Picasso picasso, CircleTransform circleTransform, SearchFriendViewCallback callback) {
        super.bind(friend);
        this.callback = new WeakReference<>(callback);
        loadUserDpImage(picasso, circleTransform);
    }

    private void loadUserDpImage(Picasso picasso, CircleTransform circleTransform) {
        picasso.load(friend.dpUri).transform(circleTransform).into(friendDp, null);
    }

    @Click(R.id.friend_action)
    protected void friendActionClicked() {
        callback.get().onAddFriend();
    }

    public interface SearchFriendViewCallback {
        void onAddFriend();
    }
}
