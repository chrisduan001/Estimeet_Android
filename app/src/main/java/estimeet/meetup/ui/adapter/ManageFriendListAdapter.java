package estimeet.meetup.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import estimeet.meetup.R;
import estimeet.meetup.model.Friend;
import estimeet.meetup.ui.adapter.view.FriendListView;
import estimeet.meetup.ui.adapter.view.FriendListView_;

/**
 * Created by AmyDuan on 19/03/16.
 */
public class ManageFriendListAdapter extends CursorRecyclerAdapter<FriendListView> {

    private Context context;

    private WeakReference<ManageFriendAdapterCallback> callback;

    @Inject
    public ManageFriendListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void onBindViewHolder(ViewWrapper<FriendListView> holder, Cursor cursor, int position) {
        FriendListView view = holder.getView();
        Friend friend = Friend.fromCursor(cursor);
        view.bindFriend(friend);
        if (position == 0) {
            view.showSectionHeader(context.getString(R.string.friend_header));
        }
    }

    @Override
    public ViewWrapper<FriendListView> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewWrapper<>(FriendListView_.build(context));
    }

    public void setCallback(ManageFriendAdapterCallback callback) {
        this.callback = new WeakReference<>(callback);
    }

    public interface ManageFriendAdapterCallback {
        void onRequest();
    }
}
