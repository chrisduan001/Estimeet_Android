package estimeet.meetup.ui.adapter.util;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by AmyDuan on 21/03/16.
 */
public interface ItemTouchListener {
    void onItemMove(RecyclerView.ViewHolder position);
    void onStartSwipe(View view, int position);
    void onStopSwipe();
    boolean isViewSwipeable(View view, long id);
}
