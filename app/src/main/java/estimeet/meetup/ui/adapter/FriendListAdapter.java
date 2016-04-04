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
import estimeet.meetup.model.FriendSession;
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
    public static final int SENT_SESSION = 100;
    public static final int RECEIVED_SESSION = 101;
    public static final int ACTIVE_SESSION = 102;

    public static final int FRIEND_HEADER = 10;
    public static final int FRIEND_SECTION = 11;
    public static final int SESSION_HEADER = 20;
    public static final int SESSION_SECTION = 21;

    private static final int VIEWTYPE_SECTION = 0;
    private static final int VIEWTYPE_ITEM = 1;
    private static final int VIEWTYPE_SESSION = 2;

    private Context context;
    private WeakReference<FriendAdapterCallback> callback;
    private FriendListView viewSwiped = null;

    private int itemSelected = Adapter.NO_SELECTION;
    private int currentSection = Integer.MIN_VALUE;

    @Inject
    public FriendListAdapter(Context context) {
        this.context = context;
    }

    //region override from cursorrecycleradapter
    @Override
    public void onBindViewHolder(ViewWrapper holder, Cursor cursor, int position) {
        View view = holder.getView();

        if (isSection(position)) {
            SimpleHeaderView headerView = (SimpleHeaderView) view;
            headerView.bindHeader(sectionHash.get(position) == FRIEND_HEADER ?
                    context.getString(R.string.friend_header) :
                    context.getString(R.string.session_header));
        } else if (isSession(position)) {
            FriendSessionView sessionView = (FriendSessionView) view;
            sessionView.bindView(FriendSession.fromCursor(cursor));
        } else {
            FriendListView friendView = (FriendListView)view;
            FriendSession friend = FriendSession.fromCursor(cursor);
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
        sectionHash = new HashMap<>();
        sectionPos = new ArrayList<>();

        cursor.moveToPosition(-1);
        int position = 0;
        while (cursor.moveToNext()) {
            int section = FriendSession.getSection(cursor);
            if (isNewSectionStarts(section)) {
                //add a section header for new section
                sectionHash.put(position, section == FRIEND_SECTION ? FRIEND_HEADER : SESSION_HEADER);
                currentSection = section;
                sectionPos.add(position);
                position++;
            }
            sectionHash.put(position, section);
            position++;
        }
        currentSection = Integer.MIN_VALUE;
    }

    private boolean isNewSectionStarts(int section) {
        return currentSection != section;
    }
    //endregion

    //region recyclerview action
    @Override
    public int getItemViewType(int position) {
        if (isSection(position)) {
            return VIEWTYPE_SECTION;
        } else if (isSession(position)) {
            return VIEWTYPE_SESSION;
        } else {
            return VIEWTYPE_ITEM;
        }
    }

    private boolean isSession(int position) {
        return sectionHash.get(position) == SESSION_SECTION;
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
            viewSwiped.bindFriend(viewSwiped.getFriendSession());
        }
    }

    private void resetSelection(int position) {
        itemSelected = Adapter.NO_SELECTION;
        FriendSession session = viewSwiped.getFriendSession();
        //// TODO: 4/04/16 change to actual request time later
        session.setRequestedLength(0);
        callback.get().onSessionRequest(session);
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

    public void setCallback(FriendAdapterCallback callback) {
        this.callback = new WeakReference<>(callback);
    }

    public interface FriendAdapterCallback {
        void onSessionRequest(FriendSession friendSession);
    }
}
