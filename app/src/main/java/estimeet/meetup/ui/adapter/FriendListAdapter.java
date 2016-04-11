package estimeet.meetup.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
public class FriendListAdapter extends CursorRecyclerAdapter implements ItemTouchListener,
        FriendSessionView.SessionActionCallback {
    private static final String TAG = FriendListAdapter.class.getSimpleName();
    public static final int SENT_SESSION = 100;
    public static final int RECEIVED_SESSION = 101;
    public static final int ACTIVE_SESSION = 102;

    public static final int FRIEND_HEADER = 10;
    public static final int FRIEND_SECTION = 11;
    public static final int SESSION_HEADER = 20;
    public static final int SESSION_SECTION = 21;

    public static final int VIEWTYPE_SESSION = 2;
    private static final int VIEWTYPE_SECTION = 0;
    private static final int VIEWTYPE_ITEM = 1;

    private Context context;
    private WeakReference<FriendAdapterCallback> callback;
    private FriendListView viewSwiped = null;

    private CountDownTimer timer;
    private List<FriendSessionView> viewToUpdate;

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
            FriendSession friendSession = FriendSession.fromCursor(cursor);
            sessionView.bindView(friendSession, this);
            updateTimer(friendSession, sessionView);
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
        viewToUpdate = new ArrayList<>();

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

    //region item touch listener (recyclerview touch actions)
    @Override
    public void onItemMove(RecyclerView.ViewHolder viewHolder) {
        int adapterPos = viewHolder.getAdapterPosition();
        if (adapterPos != Adapter.NO_SELECTION) {
            resetSelection(viewHolder.itemView, adapterPos);
        }
    }
    //swipe not complete(eg: user swiped half way and cancelled)
    @Override
    public void onStopSwipe() {
        if (itemSelected != Adapter.NO_SELECTION && viewSwiped != null) {
            viewSwiped.bindFriend(viewSwiped.getFriendSession());
        }
    }

    @Override
    public boolean isViewSwipeable(View view, long id) {
        //disable view swipe when 1.recyclerview has no id (header section)
        // 2. view friend sessionview but is not active session
        if (id != RecyclerView.NO_ID) {
            if (view instanceof FriendSessionView) {
                FriendSessionView fSessionView = (FriendSessionView) view;
                return fSessionView.getFriendSession().getType() == ACTIVE_SESSION;
            }
            return true;
        } else return false;
    }

    private void resetSelection(View view, int position) {
        itemSelected = Adapter.NO_SELECTION;
        viewSwiped = null;

        FriendSession session;
        if (view instanceof FriendListView) {
            //// TODO: 4/04/16 change to actual request time later
            // TODO: 7/04/16 0 == 15 minutes 1 == 30minutes etc
            session = ((FriendListView) view).getFriendSession();
            session.setRequestedLength(0);
            callback.get().onSessionRequest(session);
            notifyItemRemoved(position);
        } else {
            session = ((FriendSessionView) view).getFriendSession();
            callback.get().onRequestLocation(session);
            notifyItemChanged(position);
        }
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

    //region view callback
    @Override
    public void onCancelSession(FriendSession friendSession) {
        callback.get().onCancelSession(friendSession);
    }

    @Override
    public void onAcceptRequest(FriendSession friendSession) {
        callback.get().onAcceptSession(friendSession);
    }

    @Override
    public void onIgnoreRequest(FriendSession friendSession) {
        callback.get().onIgnoreRequest(friendSession);
    }
    //endregion

    //region timer logic
    private void updateTimer(FriendSession friendSession, FriendSessionView view) {
        if (viewToUpdate == null) viewToUpdate = new ArrayList<>();

        if (friendSession.getType() == ACTIVE_SESSION) {
            viewToUpdate.add(view);

            if (timer == null) setProgressBarTimer();
        }
    }

    private void setProgressBarTimer() {
        timer = new CountDownTimer(10000000, 10000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(TAG, "onTick: ");
                if (viewToUpdate.size() <= 0) this.cancel();
                for (FriendSessionView view: viewToUpdate) {
                    view.setProgressBarView();
                }
            }
            @Override
            public void onFinish() {
            }
        }.start();
    }

    public void pauseTimer() {
        if (timer != null) timer.cancel();
    }

    public void resumeTimer() {
        if (timer != null) timer.start();
    }

    public void destoryTimer() {
        timer = null;
        viewToUpdate = null;
    }
    //endregion
    public void setCallback(FriendAdapterCallback callback) {
        this.callback = new WeakReference<>(callback);
    }

    public interface FriendAdapterCallback {
        void onSessionRequest(FriendSession friendSession);
        void onCancelSession(FriendSession friendSession);
        void onAcceptSession(FriendSession friendSession);
        void onIgnoreRequest(FriendSession friendSession);
        void onRequestLocation(FriendSession friendSession);
    }
}
