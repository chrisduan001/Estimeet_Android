package estimeet.meetup.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import estimeet.meetup.model.UserFromSearch;
import estimeet.meetup.ui.adapter.util.ViewWrapper;
import estimeet.meetup.ui.adapter.view.SearchFriendView;
import estimeet.meetup.ui.adapter.view.SearchFriendView_;
import estimeet.meetup.util.CircleTransform;


/**
 * Created by AmyDuan on 16/07/16.
 */
public class SearchFriendListAdapter extends RecyclerView.Adapter<ViewWrapper>
        implements SearchFriendView.SearchFriendViewCallback {

    private List<UserFromSearch> users;

    private Picasso picasso;
    private CircleTransform circleTransform;

    private WeakReference<SearchFriendCallback> callback;

    @Inject
    public SearchFriendListAdapter(Picasso picasso, CircleTransform circleTransform) {
        this.picasso = picasso;
        this.circleTransform = circleTransform;
    }

    @Override
    public ViewWrapper onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewWrapper(SearchFriendView_.build(parent.getContext()));
    }

    public void setListData(List<UserFromSearch> users) {
        this.users = users;
    }

    public void setCallback(SearchFriendCallback callback) {
        this.callback = new WeakReference<>(callback);
    }

    public void onAddFriendFailed(int userId) {
        if (users != null) {
            for (UserFromSearch user: users) {
                if (user.id == userId) {
                    user.isFriend = false;
                }
            }
        }

        notifyAdapterDataChange();
    }

    public void notifyAdapterDataChange() {
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewWrapper holder, int position) {
        SearchFriendView view = (SearchFriendView)holder.getView();
        view.bind(users.get(position), picasso, circleTransform, this);
    }

    @Override
    public int getItemCount() {
        return users == null ? 0 : users.size();
    }

    @Override
    public void onAddFriend(UserFromSearch user) {
        notifyDataSetChanged();
        callback.get().onAddFriend(user);
    }

    public interface SearchFriendCallback {
        void onAddFriend(UserFromSearch user);
    }
}
