package estimeet.meetup.ui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

import estimeet.meetup.R;
import estimeet.meetup.di.components.ManageFriendComponent;
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

    @ViewById(R.id.recyclerView) RecyclerView recyclerView;

    //region lifecycle
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();

        presenter.setView(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        getActivity().getSupportLoaderManager().initLoader(0, null, this);
    }

    private void initialize() {
        getComponent(ManageFriendComponent.class).inject(this);
    }
    //endregion

    //region presenter callback

    @Override
    public void onSetFriendCursor(Cursor cursor) {

    }

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
        return new CursorLoader(getContext(), SqliteContract.Friends.CONTENT_URI, DataHelper.FriendQuery.PROJECTION,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        FriendListAdapter adapter = new FriendListAdapter(data);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
