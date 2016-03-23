package estimeet.meetup.ui.adapter.util;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by AmyDuan on 16/03/16.
 */
public class ViewWrapper extends RecyclerView.ViewHolder  {

    private View view;

    public ViewWrapper(View itemView) {
        super(itemView);

        this.view = itemView;
    }

    public View getView() {
        return view;
    }
}
