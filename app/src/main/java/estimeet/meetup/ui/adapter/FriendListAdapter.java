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
import estimeet.meetup.ui.adapter.view.FriendSessionView;
import estimeet.meetup.ui.adapter.view.FriendSessionView_;
import estimeet.meetup.ui.adapter.view.SimpleHeaderView;
import estimeet.meetup.ui.adapter.view.SimpleHeaderView_;

/**
 * Created by AmyDuan on 19/03/16.
 */
public class FriendListAdapter extends CursorRecyclerAdapter implements ItemTouchListener {
    private static final int VIEWTYPE_SECTION = 0;
    private static final int VIEWTYPE_ITEM = 1;
    private static final int VIEWTYPE_SESSION = 2;

    private Context context;

    private WeakReference<ManageFriendAdapterCallback> callback;

    private int itemSelected = Adapter.NO_SELECTION;

    private FriendListView viewSwiped = null;

    @Inject
    public FriendListAdapter(Context context) {
        this.context = context;
    }

    //region override from cursorrecycleradapter
    @Override
    public void onBindViewHolder(ViewWrapper holder, Cursor cursor, int position) {
        View view = holder.getView();

        if (view instanceof SimpleHeaderView) {
            SimpleHeaderView headerView = (SimpleHeaderView) view;
            headerView.bindHeader(context.getString(R.string.friend_header));
        } else if (view instanceof FriendSessionView) {
            FriendSessionView sessionView = (FriendSessionView) view;

        } else {
            FriendListView friendView = (FriendListView)view;
            Friend friend = Friend.fromCursor(cursor);
            friendView.bindFriend(friend);

            if (position == itemSelected) {
                friendView.setSwipeView();
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
    //endregion

    //region recyclerview action
    @Override
    public int getItemViewType(int position) {
        if (isSection(position)) {
            return VIEWTYPE_SECTION;
        } else if (isFirstSection(position)) {
            return VIEWTYPE_SESSION;
        } else {
            return VIEWTYPE_ITEM;
        }
    }

    //first section ---> sectionpos > 1 (multiple sections) &&
    //get section pos from sectionhash name value pair
    private boolean isFirstSection(int position) {
        return sectionPos.size() > 1 && sectionHash.get(position) == 1;
    }

    @Override
    public ViewWrapper onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEWTYPE_SECTION:
                return new ViewWrapper(SimpleHeaderView_.build(context));
            case VIEWTYPE_ITEM:
                return new ViewWrapper(FriendListView_.build(context));
            case VIEWTYPE_SESSION:
                return new ViewWrapper(FriendSessionView_.build(context));
            default:
                throw new RuntimeException("invalid view type exception");
        }
    }
    //endregion

    //region item touch listener
    @Override
    public void onItemMove(int position) {
        if (position != Adapter.NO_SELECTION) {
            resetSelection(position);
        }
    }
    //swipe not complete(eg: user swiped half way and cancelled)
    @Override
    public void onStopSwipe() {
        if (itemSelected != Adapter.NO_SELECTION) {
            getCursor().moveToPosition(getCursorPosition(itemSelected));
            viewSwiped.bindFriend(Friend.fromCursor(getCursor()));
        }
    }

    private void resetSelection(int position) {
        itemSelected = Adapter.NO_SELECTION;
        notifyItemRemoved(position);
    }

    @Override
    public void onStartSwipe(View view, int position) {
        itemSelected = position;
        if (view instanceof FriendListView) {
            viewSwiped = ((FriendListView) view);
            viewSwiped.setSwipeView();
        }
    }
    //endregion

    public void setCallback(ManageFriendAdapterCallback callback) {
        this.callback = new WeakReference<>(callback);
    }

    public interface ManageFriendAdapterCallback {
        void onRequest();
    }
}
