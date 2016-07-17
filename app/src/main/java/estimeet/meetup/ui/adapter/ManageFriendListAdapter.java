package estimeet.meetup.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import estimeet.meetup.R;
import estimeet.meetup.model.Friend;
import estimeet.meetup.ui.adapter.util.CursorRecyclerAdapter;
import estimeet.meetup.ui.adapter.util.ViewWrapper;
import estimeet.meetup.ui.adapter.view.BaseFriendListView_;
import estimeet.meetup.ui.adapter.view.ManageFriendView;
import estimeet.meetup.ui.adapter.view.ManageFriendView_;
import estimeet.meetup.util.CircleTransform;

/**
 * Created by AmyDuan on 15/03/16.
 */
public class ManageFriendListAdapter extends CursorRecyclerAdapter
        implements ManageFriendView.FriendListViewCallback {

    private Context context;
    private Picasso picasso;
    private CircleTransform circleTransform;

    private WeakReference<ManageFriendAdapterCallback> callback;
    @Inject
    public ManageFriendListAdapter(Context context, Picasso picasso, CircleTransform circleTransform) {
        this.context = context;
        this.picasso = picasso;
        this.circleTransform = circleTransform;
    }

    @Override
    public void onBindViewHolder(ViewWrapper holder, Cursor cursor, int position) {
        ManageFriendView view = (ManageFriendView)holder.getView();
        Friend friend = Friend.fromCursor(cursor);
        view.bind(friend, picasso, circleTransform, this);
        if (position == 0) {
            view.showSectionHeader(context.getString(R.string.friend_recommend_friend));
        }
    }

    @Override
    public void buildSectionHash(Cursor cursor) {
        sectionHash = null;
    }

    @Override
    public ViewWrapper onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewWrapper(ManageFriendView_.build(context));
    }

    public void setCallback(ManageFriendAdapterCallback callback) {
        this.callback = new WeakReference<>(callback);
    }

    @Override
    public void onUpdateFriend(Friend friend) {
        callback.get().onUpdateFriend(friend);
    }

    public interface ManageFriendAdapterCallback {
        void onUpdateFriend(Friend friend);
    }
}
