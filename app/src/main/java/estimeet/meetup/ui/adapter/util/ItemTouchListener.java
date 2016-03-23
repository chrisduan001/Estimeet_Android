package estimeet.meetup.ui.adapter.util;

import android.view.View;

/**
 * Created by AmyDuan on 21/03/16.
 */
public interface ItemTouchListener {
    void onItemMove(int position);
    void onStartSwipe(View view, int position);
    void onStopSwipe();
}
