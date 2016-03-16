package estimeet.meetup.ui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

import estimeet.meetup.R;
import estimeet.meetup.di.components.ManageFriendComponent;
import estimeet.meetup.model.Friend;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.model.database.SqliteContract;
import estimeet.meetup.ui.adapter.FriendListAdapter;
import estimeet.meetup.ui.presenter.BasePresenter;
import estimeet.meetup.ui.presenter.ManageFriendPresenter;

/**
 * Created by AmyDuan on 15/03/16.
 */
@EFragment(R.layout.fragment_manage_friend)
public class ManageFriendFragment extends BaseFragment implements ManageFriendPresenter.ManageFriendView, LoaderManager.LoaderCallbacks<Cursor> {

    @Inject ManageFriendPresenter presenter;

    @Inject FriendListAdapter friendListAdapter;

    @ViewById(R.id.recyclerView) RecyclerView recyclerView;

    //region lifecycle
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();

        presenter.setView(this);
        initRecyclerView();
        initFriendCursor();
    }

    private void initialize() {
        getComponent(ManageFriendComponent.class).inject(this);
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        friendListAdapter.setCursor(null);
        recyclerView.setAdapter(friendListAdapter);
    }

    private void initFriendCursor() {
        getActivity().getSupportLoaderManager().initLoader(0, null, this);
    }
    //endregion

    //region presenter callback

    //endregion

    //region base
    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    protected ProgressBar getProgressBar() {
        return null;
    }

    @Override
    public void showProgressDialog() {}
    //endregion

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), SqliteContract.Friends.CONTENT_URI,
                DataHelper.FriendQuery.PROJECTION,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        friendListAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        friendListAdapter.swapCursor(null);
    }
}
