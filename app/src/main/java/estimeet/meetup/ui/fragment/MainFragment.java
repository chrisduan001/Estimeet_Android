package estimeet.meetup.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;
import javax.inject.Named;

import estimeet.meetup.R;
import estimeet.meetup.di.components.MainComponent;
import estimeet.meetup.model.User;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.model.database.SqliteContract;
import estimeet.meetup.ui.adapter.ManageFriendListAdapter;
import estimeet.meetup.ui.presenter.BasePresenter;
import estimeet.meetup.ui.presenter.MainPresenter;
import estimeet.meetup.util.AnimationUtil;

/**
 * Created by AmyDuan on 6/02/16.
 */
@EFragment(R.layout.fragment_main)
public class MainFragment extends BaseFragment implements MainPresenter.MainView, LoaderManager.LoaderCallbacks<Cursor>,
        ManageFriendListAdapter.ManageFriendAdapterCallback{

    public interface MainCallback {
        void navToFriendList();
        void navToManageProfile();
    }

    private static final int MAINCURSORLOADER = 1;

    private boolean isFabExpanded = false;
    private float fabAction1Offset = 0.f;
    private float fabAction2Offset = 0.f;

    @Inject MainPresenter presenter;
    @Inject @Named("currentUser") User user;
    @Inject ManageFriendListAdapter adapter;

    @ViewById(R.id.recycler)        RecyclerView recyclerView;
    @ViewById(R.id.fab)             FloatingActionButton fab;
    @ViewById(R.id.fab_action1)     ViewGroup fabAction1;
    @ViewById(R.id.fab_action2)     ViewGroup fabAction2;
    @ViewById(R.id.fab_container)   ViewGroup fabContainer;

    private MainCallback mainCallback;

    //region lifecycle
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainCallback) {
            this.mainCallback = (MainCallback) context;
        } else {
            throw new UnsupportedOperationException("Activity must implement " +
                    MainCallback.class.getSimpleName());
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();

        presenter.setView(this);
        adapter.setCallback(this);

        initRecycler();
        initFriendCursor();

    }

    private void initialize() {
        getComponent(MainComponent.class).inject(this);
    }

    private void initRecycler() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setCursor(null);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }
    //endregion

    //region cursor loader
    private void initFriendCursor() {
        getActivity().getSupportLoaderManager().initLoader(MAINCURSORLOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), SqliteContract.Friends.CONTENT_URI,
                DataHelper.FriendQuery.PROJECTION,getSelection(),null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    private String getSelection() {
        return SqliteContract.FriendColumns.FAVOURITE + " = 1";
    }
    //endregion

    //region presenter call
    @Override
    protected ProgressBar getProgressBar() {
        return null;
    }

    @Override
    public void showProgressDialog() {
        showProgressDialog(getString(R.string.progress_loading));
    }
    //endregion

    //region button
    @Click(R.id.fab)
    protected void onFabClicked() {
        if (fabAction1Offset <= 0 || fabAction2Offset <= 0) {
            fabAction1Offset = fab.getY() - fabAction1.getY();
            fabAction2Offset = fab.getY() - fabAction2.getY();
        }

        if (isFabExpanded) {
            collapseFab();
        } else {
            expandFab();
        }
    }

    @Click(R.id.fab_action1)
    protected void onFabAction1Clicked() {
        mainCallback.navToFriendList();
        collapseFab();
    }

    @Click(R.id.fab_action2)
    protected void onFabAction2Clicked() {
        mainCallback.navToManageProfile();
        collapseFab();
    }

    private void expandFab() {
        AnimationUtil.performFabExpandAnimation(new float[]{fabAction1Offset, fabAction2Offset},
                fabAction1, fabAction2);
        isFabExpanded = true;
    }

    private void collapseFab() {
        AnimationUtil.performFabCollapseAnimation(new float[]{fabAction1Offset, fabAction2Offset},
                fabAction1, fabAction2);
        isFabExpanded = false;
    }
    //endregion

    //region adapter callback
    @Override
    public void onRequest() {

    }
    //endregion
}
