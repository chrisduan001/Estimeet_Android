package estimeet.meetup.ui.adapter.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import estimeet.meetup.R;
import estimeet.meetup.ui.adapter.FriendListAdapter;

/**
 * Created by AmyDuan on 21/03/16.
 */
public class ItemTouchHelperCallback extends android.support.v7.widget.helper.ItemTouchHelper.Callback {

    private final ItemTouchListener listener;

    private Paint paint;
    private Bitmap icon;

    public ItemTouchHelperCallback(ItemTouchListener listener, Context context) {
        this.listener = listener;

        paint = new Paint();
        paint.setColor(Color.parseColor("#77A500"));
        icon = BitmapFactory.decodeResource(context.getResources(), android.R.drawable.ic_delete);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            //SWIPE
            if (viewHolder.getItemId() == RecyclerView.NO_ID ||
                    viewHolder.getItemViewType() == FriendListAdapter.VIEWTYPE_SESSION) return;
            listener.onStartSwipe(viewHolder.itemView, viewHolder.getAdapterPosition());

        } else if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
            //RELEASE
            listener.onStopSwipe();
        }
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (viewHolder.getItemId() == RecyclerView.NO_ID ||
                viewHolder.getItemViewType() == FriendListAdapter.VIEWTYPE_SESSION) return;
        listener.onItemMove(viewHolder.getAdapterPosition());
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (viewHolder.getItemId() == RecyclerView.NO_ID ||
                viewHolder.getItemViewType() == FriendListAdapter.VIEWTYPE_SESSION) return;

        View itemView = viewHolder.itemView;

        if (dX > 0) {
            c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom(), paint);
            //to left of itemview.getleft + x,
            //(float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight()) / 2 == center
            c.drawBitmap(icon, (float) itemView.getLeft() + 32,
                    (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight()) / 2, paint);
        } else {
            c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                    (float) itemView.getRight(), (float) itemView.getBottom(), paint);
            c.drawBitmap(icon, (float) itemView.getRight() - 32 - icon.getWidth(),
                    (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight()) / 2, paint);
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
