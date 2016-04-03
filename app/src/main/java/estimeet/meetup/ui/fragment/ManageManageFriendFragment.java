package estimeet.meetup.ui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import javax.inject.Inject;
import estimeet.meetup.R;
import estimeet.meetup.di.components.ManageFriendComponent;
import estimeet.meetup.model.Friend;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.model.database.SqliteContract;
import estimeet.meetup.ui.adapter.ManageFriendListAdapter;
import estimeet.meetup.ui.presenter.BasePresenter;
import estimeet.meetup.ui.presenter.ManageFriendPresenter;

/**
 * Created by AmyDuan on 15/03/16.
 */
@EFragment(R.layout.fragment_manage_friend)
public class ManageManageFriendFragment extends BaseFragment implements ManageFriendPresenter.ManageFriendView,
        LoaderManager.LoaderCallbacks<Cursor>, ManageFriendListAdapter.ManageFriendAdapterCallback {

    @Inject ManageFriendPresenter presenter;

    @Inject ManageFriendListAdapter manageFriendListAdapter;

    @ViewById(R.id.recyclerView) RecyclerView recyclerView;
    @ViewById(R.id.progress_bar) ProgressBar progressBar;

    //region lifecycle
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();

        presenter.setView(this);
        manageFriendListAdapter.setCallback(this);

        initRecyclerView();
        initFriendCursor();
    }

    private void initialize() {
        getComponent(ManageFriendComponent.class).inject(this);
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        manageFriendListAdapter.setCursor(null);
        recyclerView.setAdapter(manageFriendListAdapter);
    }
    //endregion

    //region presenter callback
    @Override
    public void onGetFriendsList() {
        restartFriendCursor();
    }

    //endregion

    //region base
    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    protected ProgressBar getProgressBar() {
        return progressBar;
    }

    @Override
    public void showProgressDialog() {
        showProgressDialog(getString(R.string.progress_loading));
    }
    //endregion

    //region cursor loader
    private void initFriendCursor() {
        getActivity().getSupportLoaderManager().initLoader(0, null, this);
    }

    private void restartFriendCursor() {
        getActivity().getSupportLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), SqliteContract.Friends.CONTENT_URI,
                DataHelper.FriendQuery.PROJECTION,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() <= 0) {
            showProgressDialog();
            presenter.requestFriendList();
        } else {
            manageFriendListAdapter.changeCursor(data);
            dismissProgressDialog();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        manageFriendListAdapter.swapCursor(null);
    }
    //endregion

    //region adapter action

    @Override
    public void onUpdateFriend(Friend friend) {
        presenter.onUpdateFriend(friend);
    }


    //endregion
}
