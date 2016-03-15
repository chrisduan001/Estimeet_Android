package estimeet.meetup.ui.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import estimeet.meetup.R;

/**
 * Created by AmyDuan on 15/03/16.
 */
public class FriendListAdapter extends CursorRecyclerAdapter<FriendListAdapter.ViewHolder> {

    public FriendListAdapter(Cursor c) {
        super(c);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        holder.friendName.setText("Simple name");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_friends_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {

        return super.swapCursor(newCursor);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

//        @ViewById(R.id.friend_dp) ImageView friendDp;
//        @ViewById(R.id.friend_action) ImageButton friendAction;
//        @ViewById(R.id.friend_header) TextView friendHeader;
        private TextView friendName;
//        @ViewById(R.id.separator_view) View separatorView;

        public ViewHolder(View itemView) {
            super(itemView);
            friendName = (TextView) itemView.findViewById(R.id.friend_name);
        }
    }
}
