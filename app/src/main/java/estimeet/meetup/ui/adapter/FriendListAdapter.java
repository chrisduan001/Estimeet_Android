package estimeet.meetup.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import estimeet.meetup.R;
import estimeet.meetup.model.Friend;
import estimeet.meetup.ui.adapter.util.CursorRecyclerAdapter;
import estimeet.meetup.ui.adapter.util.ItemTouchListener;
import estimeet.meetup.ui.adapter.util.ViewWrapper;
import estimeet.meetup.ui.adapter.view.FriendListView;
import estimeet.meetup.ui.adapter.view.FriendListView_;
import estimeet.meetup.ui.adapter.view.SimpleHeaderView;
import estimeet.meetup.ui.adapter.view.SimpleHeaderView_;

/**
 * Created by AmyDuan on 19/03/16.
 */
public class FriendListAdapter extends CursorRecyclerAdapter implements ItemTouchListener {
    private static final int VIEWTYPE_SECTION = 0;
    private static final int VIEWTYPE_ITEM = 1;

    private Context context;

    private WeakReference<ManageFriendAdapterCallback> callback;

    private int itemSelected = Adapter.NO_SELECTION;

    @Inject
    public FriendListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void onBindViewHolder(ViewWrapper holder, Cursor cursor, int position) {
        View view = holder.getView();

        if (view instanceof SimpleHeaderView) {
            SimpleHeaderView headerView = (SimpleHeaderView) view;
            headerView.bindHeader(context.getString(R.string.friend_header));
            currentSection++;
        } else {
            FriendListView friendView = (FriendListView)view;
            Friend friend = Friend.fromCursor(cursor);
            friendView.bindFriend(friend);

            if (position == itemSelected) {
                friendView.setBackground();
                itemSelected = Adapter.NO_SELECTION;
            }
        }
    }

    /**
     this method will find when should start a new section and at which position
     also find the how many sections in total, will be used for the total number of list
     */
    @Override
    public void buildSectionHash(Cursor cursor) {
        int position = 0;
        if (cursor.moveToFirst()) {
            //position + section
            sectionHash = new HashMap<>();
            sectionPos = new ArrayList<>();
            //first header section
            sectionHash.put(0, 0);
            sectionCount = 1;
            sectionPos.add(position);
            position++;
            sectionHash.put(position, sectionCount);
        } else return;

        while (cursor.moveToNext()) {
            position ++;
            if (Friend.isNewSection(cursor)) {
                sectionPos.add(position);
                sectionCount++;
                sectionHash.put(position, sectionCount);
                position++;
            }
            sectionHash.put(position, sectionCount);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isSection(position)) {
            return VIEWTYPE_SECTION;
        } else {
            return VIEWTYPE_ITEM;
        }
    }

    @Override
    public ViewWrapper onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewType == VIEWTYPE_SECTION ? new ViewWrapper(SimpleHeaderView_.build(context))
                                            : new ViewWrapper(FriendListView_.build(context));
    }

    @Override
    public void onItemMove(int position) {
        itemSelected = position;
        notifyItemChanged(position);
    }

    private boolean isSection(int position) {
        return sectionPos.contains(position);
    }

    public void setCallback(ManageFriendAdapterCallback callback) {
        this.callback = new WeakReference<>(callback);
    }

    public interface ManageFriendAdapterCallback {
        void onRequest();
    }
}
