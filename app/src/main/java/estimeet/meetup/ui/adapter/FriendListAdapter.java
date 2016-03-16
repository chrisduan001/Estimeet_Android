package estimeet.meetup.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.ViewGroup;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.RootContext;

import javax.inject.Inject;

import estimeet.meetup.R;
import estimeet.meetup.model.Friend;
import estimeet.meetup.ui.adapter.view.FriendListView;
import estimeet.meetup.ui.adapter.view.FriendListView_;

/**
 * Created by AmyDuan on 15/03/16.
 */
public class FriendListAdapter extends CursorRecyclerAdapter<FriendListView> {

    private Context context;

    @Inject
    public FriendListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void onBindViewHolder(ViewWrapper<FriendListView> holder, Cursor cursor) {
        FriendListView view = holder.getView();
        Friend friend = Friend.fromCursor(cursor);
        view.bind(friend);
    }

    @Override
    public ViewWrapper<FriendListView> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewWrapper<>(FriendListView_.build(context));
    }
}
