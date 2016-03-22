package estimeet.meetup.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Adapter;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import estimeet.meetup.R;
import estimeet.meetup.model.Friend;
import estimeet.meetup.ui.adapter.util.CursorRecyclerAdapter;
import estimeet.meetup.ui.adapter.util.ItemTouchListener;
import estimeet.meetup.ui.adapter.util.ViewWrapper;
import estimeet.meetup.ui.adapter.view.FriendListView;
import estimeet.meetup.ui.adapter.view.FriendListView_;

/**
 * Created by AmyDuan on 19/03/16.
 */
public class FriendListAdapter extends CursorRecyclerAdapter<FriendListView> implements ItemTouchListener {

    private Context context;

    private WeakReference<ManageFriendAdapterCallback> callback;

    private int itemSelected = Adapter.NO_SELECTION;

    @Inject
    public FriendListAdapter(Context context) {
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

        if (position == itemSelected) {
            view.setBackground();
            itemSelected = Adapter.NO_SELECTION;
        }
    }

    @Override
    public ViewWrapper<FriendListView> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewWrapper<>(FriendListView_.build(context));
    }

    @Override
    public void onItemMove(int position) {
        itemSelected = position;
        notifyItemChanged(position);
    }

    public void setCallback(ManageFriendAdapterCallback callback) {
        this.callback = new WeakReference<>(callback);
    }

    public interface ManageFriendAdapterCallback {
        void onRequest();
    }
}
