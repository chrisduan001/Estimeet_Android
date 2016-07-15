package estimeet.meetup.ui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
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
import estimeet.meetup.util.AnimationUtil;

/**
 * Created by AmyDuan on 15/03/16.
 */
@EFragment(R.layout.fragment_manage_friend)
public class ManageFriendFragment extends BaseFragment implements MenuItemCompat.OnActionExpandListener,
        ManageFriendPresenter.ManageFriendView,
        LoaderManager.LoaderCallbacks<Cursor>, ManageFriendListAdapter.ManageFriendAdapterCallback {

    public static final int ACTIVITY_RESULT = 1000;
    public static final String RESULT_MESSAGE = "DATA";

    @Inject ManageFriendPresenter presenter;

    @Inject ManageFriendListAdapter manageFriendListAdapter;

    @ViewById(R.id.recyclerView) RecyclerView recyclerView;
    @ViewById(R.id.searchResult_list) RecyclerView searchResultList;
    @ViewById(R.id.progress_bar) ProgressBar progressBar;

    private SearchView searchView;

    //region lifecycle
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();

        presenter.setView(this);
        manageFriendListAdapter.setCallback(this);

        initRecyclerView();
        initSearchResultRecycler();

        initFriendCursor();

        getActivity().setResult(ACTIVITY_RESULT, getActivity().getIntent().putExtra(RESULT_MESSAGE, false));
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

    private void initSearchResultRecycler() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        setUpSearchView(menu, inflater);
        super.onCreateOptionsMenu(menu, inflater);
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
        getActivity().setResult(ACTIVITY_RESULT, getActivity().getIntent().putExtra(RESULT_MESSAGE, true));
    }
    //endregion

    //region searchview
    private void setUpSearchView(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.toolbar_search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint(getResources().getString(R.string.search_phone));

        MenuItemCompat.setOnActionExpandListener(menuItem, this);
        setupTextChangeListener();
    }

    private void setupTextChangeListener() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        AnimationUtil.performFadeOutAnimation(getContext(), new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                searchResultList.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        }, searchResultList);
        return true;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        AnimationUtil.performFadeInAnimation(getContext(), searchResultList);
        searchResultList.setVisibility(View.VISIBLE);
        return true;
    }

    //endregion
}
