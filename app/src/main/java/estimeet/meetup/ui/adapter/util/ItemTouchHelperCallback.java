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
        paint.setColor(Color.parseColor("#F3F3F3"));
        icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_refresh_grey_48dp);
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
            if (!listener.isViewSwipeable(viewHolder.itemView, viewHolder.getItemId())) return;
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
        if (!listener.isViewSwipeable(viewHolder.itemView, viewHolder.getItemId())) return;
        listener.onItemMove(viewHolder);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View itemView = viewHolder.itemView;
        if (!listener.isViewSwipeable(itemView, viewHolder.getItemId())) return;

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
