package estimeet.meetup.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import estimeet.meetup.model.Friend;
import estimeet.meetup.ui.adapter.util.ViewWrapper;
import estimeet.meetup.ui.adapter.view.ManageFriendView;
import estimeet.meetup.ui.adapter.view.ManageFriendView_;

/**
 * Created by AmyDuan on 16/07/16.
 */
public class SearchFriendListAdapter extends RecyclerView.Adapter<ViewWrapper>
        implements ManageFriendView.FriendListViewCallback {


    @Override
    public ViewWrapper onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewWrapper(ManageFriendView_.build(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(ViewWrapper holder, int position) {
        ManageFriendView view = (ManageFriendView)holder.getView();
        
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onUpdateFriend(Friend friend) {

    }
}
