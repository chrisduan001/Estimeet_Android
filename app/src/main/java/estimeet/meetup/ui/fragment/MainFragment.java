package estimeet.meetup.ui.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.microsoft.windowsazure.notifications.NotificationsManager;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;
import javax.inject.Named;

import estimeet.meetup.R;
import estimeet.meetup.di.components.MainComponent;
import estimeet.meetup.model.FriendSession;
import estimeet.meetup.model.User;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.model.database.SqliteContract;
import estimeet.meetup.ui.adapter.FriendListAdapter;
import estimeet.meetup.ui.adapter.util.ItemTouchHelperCallback;
import estimeet.meetup.ui.presenter.BasePresenter;
import estimeet.meetup.ui.presenter.MainPresenter;
import estimeet.meetup.util.AnimationUtil;
import estimeet.meetup.util.push.NotificationHandler;

/**
 * Created by AmyDuan on 6/02/16.
 */
@EFragment(R.layout.fragment_main)
public class MainFragment extends BaseFragment implements MainPresenter.MainView, LoaderManager.LoaderCallbacks<Cursor>,
        FriendListAdapter.FriendAdapterCallback {

    public interface MainCallback {
        void navToFriendList();
        void navToManageProfile();
        void onAuthFailed();
    }

    private static final int MAINCURSORLOADER = 1;

    private boolean isFabExpanded = false;
    private float fabAction1Offset = 0.f;
    private float fabAction2Offset = 0.f;

    @Inject MainPresenter presenter;
    @Inject @Named("currentUser") User user;
    @Inject FriendListAdapter adapter;

    @ViewById(R.id.recycler)        RecyclerView recyclerView;
    @ViewById(R.id.fab)             FloatingActionButton fab;
    @ViewById(R.id.fab_action1)     ViewGroup fabAction1;
    @ViewById(R.id.fab_action2)     ViewGroup fabAction2;

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

    @Receiver(actions = "android.intent.action.GENERAL_BROADCAST")
    protected void onReceiveBoradcast() {
        presenter.requestNotification();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();

        presenter.setView(this);
        adapter.setCallback(this);

        initRecycler();
        initFriendCursor();

        registerPushChannel();
    }

    private void initialize() {
        getComponent(MainComponent.class).inject(this);
    }

    private void initRecycler() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setCursor(null);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callBack = new ItemTouchHelperCallback(adapter, getContext());
        ItemTouchHelper touchHelper = new ItemTouchHelper(callBack);
        touchHelper.attachToRecyclerView(recyclerView);

        //show/hide fab when scroll
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 50) {
                    if (!isFabExpanded && fab.isShown()) {
                        fab.hide();
                    }
                } else if (dy < 0) {
                    if (!fab.isShown()) {
                        fab.show();
                    }
                }
            }
        });
    }

    @Background
    void registerPushChannel() {
        NotificationsManager.handleNotifications(getContext(), getString(R.string.push_SENDER_ID),
                NotificationHandler.class);
        presenter.registerPushChannel();
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

    //in case that user add friend or delete friend from managefriendfragment
    public void restartFriendCursor() {
        getActivity().getSupportLoaderManager().restartLoader(MAINCURSORLOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), SqliteContract.Sessions.CONTENT_URI,
                DataHelper.FriendSessionQuery.PROJECTION, getSelection(),null,
                SqliteContract.Sessions.DEFAULT_SORT);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
    //endregion

    //region presenter callback
    @Override
    protected ProgressBar getProgressBar() {
        return null;
    }

    @Override
    public void showProgressDialog() {
        showProgressDialog(getString(R.string.progress_loading));
    }

    @Override
    public void showToastMessage(String message) {
        showShortToastMessage(message);
    }

    private String getSelection() {
        return SqliteContract.FriendColumns.FAVOURITE + " = 1";
    }

    @Override
    public void onAuthFailed() {
        super.onAuthFailed();
        mainCallback.onAuthFailed();
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
    public void onSessionRequest(FriendSession friendSession) {
        presenter.onSessionRequest(friendSession);
    }

    @Override
    public void onCancelSession(FriendSession friendSession) {
        presenter.cancelSession(friendSession);
    }
    //endregion
}
