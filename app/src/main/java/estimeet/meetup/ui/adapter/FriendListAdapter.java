package estimeet.meetup.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import estimeet.meetup.R;
import estimeet.meetup.model.Friend;
import estimeet.meetup.ui.adapter.view.FriendListView;
import estimeet.meetup.ui.adapter.view.FriendListView_;

/**
 * Created by AmyDuan on 15/03/16.
 */
public class FriendListAdapter extends CursorRecyclerAdapter<FriendListView>
        implements FriendListView.FriendListViewCallback {

    private Context context;
    private Picasso picasso;

    private WeakReference<FriendAdapterCallback> callback;
    @Inject
    public FriendListAdapter(Context context, Picasso picasso) {
        this.context = context;
        this.picasso = picasso;
    }

    @Override
    public void onBindViewHolder(ViewWrapper<FriendListView> holder, Cursor cursor, int position) {
        FriendListView view = holder.getView();
        Friend friend = Friend.fromCursor(cursor);
        view.bind(friend, picasso, this);
        if (position == 0) {
            view.showSectionHeader(context.getString(R.string.friend_recommend_friend));
        }
    }

    @Override
    public ViewWrapper<FriendListView> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewWrapper<>(FriendListView_.build(context));
    }

    public void setCallback(FriendAdapterCallback callback) {
        this.callback = new WeakReference<>(callback);
    }

    @Override
    public void onUpdateFriend(Friend friend) {
        callback.get().onUpdateFriend(friend);
    }

    public interface FriendAdapterCallback {
        void onUpdateFriend(Friend friend);
    }
}
