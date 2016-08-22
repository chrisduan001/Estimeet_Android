package estimeet.meetup.ui.fragment;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.microsoft.windowsazure.notifications.NotificationsManager;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;
import javax.inject.Named;

import estimeet.meetup.R;
import estimeet.meetup.di.components.MainComponent;
import estimeet.meetup.model.FriendSession;
import estimeet.meetup.model.MeetUpSharedPreference;
import estimeet.meetup.model.User;
import estimeet.meetup.model.database.DataHelper;
import estimeet.meetup.model.database.SqliteContract;
import estimeet.meetup.ui.adapter.FriendListAdapter;
import estimeet.meetup.ui.adapter.util.ItemTouchHelperCallback;
import estimeet.meetup.ui.presenter.BasePresenter;
import estimeet.meetup.ui.presenter.MainPresenter;
import estimeet.meetup.util.AnimationUtil;
import estimeet.meetup.util.MeetupLocationService;
import estimeet.meetup.util.push.NotificationHandler;

/**
 * Created by AmyDuan on 6/02/16.
 */
@EFragment(R.layout.fragment_main)
public class MainFragment extends BaseFragment implements MainPresenter.MainView, LoaderManager.LoaderCallbacks<Cursor>,
        FriendListAdapter.FriendAdapterCallback, OnMapReadyCallback {

    public interface MainCallback {
        void navToFriendList();
        void navToManageProfile();
        void onAuthFailed();
        void showDefaultToolbar();
        void showToolbarActionGroup(int type);
    }

    private static final int MAINCURSORLOADER = 1;

    private boolean isFabExpanded = false;
    private float fabAction1Offset = 0.f;
    private float fabAction2Offset = 0.f;

    @Inject MainPresenter presenter;
    @Inject @Named("currentUser") User user;
    @Inject FriendListAdapter adapter;
    @Inject MeetUpSharedPreference sharedPreference;

    @ViewById(R.id.recycler)        RecyclerView recyclerView;
    @ViewById(R.id.fab)             FloatingActionButton fab;
    @ViewById(R.id.fab_action1)     ViewGroup fabAction1;
    @ViewById(R.id.fab_action2)     ViewGroup fabAction2;

    @ViewById(R.id.no_friend_layout)FrameLayout noFriendLayout;

    @ViewById(R.id.google_map_static) ImageView googleMapStatic;
    @ViewById(R.id.map_message) TextView mapMessage;

    private MainCallback mainCallback;

    private GoogleMap mMap;

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

    @Receiver(actions = NotificationHandler.GENERAL_BROADCAST_ACTION)
    protected void onReceiveBoradcast() {
        presenter.requestNotification();
    }

    @Receiver(actions = NotificationHandler.NO_ACTIVITY_BROADCAST_ACTION)
    protected void onReceiveNoActivityBroadcast() {
        mainCallback.showDefaultToolbar();
    }

    @Receiver(actions = NotificationHandler.FRIEND_LOCATION_AVAILABLE_ACTION)
    protected void onReceiveFriendLocationAvailable() {
        presenter.requestPendingLocationData();
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

        initialiseGoogleMaps();

    }

    @Override
    public void onPause() {
        super.onPause();
        adapter.pauseTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.resumeTimer();
        removeNotification();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.destoryTimer();
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
    private void initialiseGoogleMaps(){
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @UiThread
    public void placeMarkerOnMap(FriendSession friendSession){
        String friendGeo = friendSession.getGeoCoordinate();


        if(friendGeo != null && friendSession.getDistance() <= 3000) {
            String[] latlong =  friendGeo.split(",");
            double latitude = Double.parseDouble(latlong[0]);
            double longitude = Double.parseDouble(latlong[1]);
            LatLng friendLocation = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(friendLocation).title(friendSession.getFriendName()));

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(friendLocation, 16.0f));
        }

    }

    @Background
    void registerPushChannel() {
        NotificationsManager.handleNotifications(getContext(), getString(R.string.push_SENDER_ID),
                NotificationHandler.class);
        presenter.registerPushChannel();
    }

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }

    //when icon on toolbar clicked
    public void setTravelMode(int travelMode) {
        presenter.setTravelMode(travelMode);
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

    //Removes all notification
    private void removeNotification(){
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) getContext().getSystemService(ns);
        nMgr.cancelAll();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), SqliteContract.Sessions.CONTENT_URI,
                DataHelper.FriendSessionQuery.PROJECTION, getSelection(),null,
                SqliteContract.Sessions.DEFAULT_SORT);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        boolean isAnyFriends = data.getCount() > 0;
        recyclerView.setVisibility(isAnyFriends ? View.VISIBLE : View.GONE);
        noFriendLayout.setVisibility(isAnyFriends ? View.GONE : View.VISIBLE);
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

    private String getSelection() {
        return SqliteContract.FriendColumns.FAVOURITE + " = 1 or " + SqliteContract.SessionColumns.FRIEND_ID + " not null";
    }

    @Override
    public void onAuthFailed() {
        super.onAuthFailed();
        mainCallback.onAuthFailed();
    }

    @Override
    public void onNoActiveSessions() {
        mainCallback.showDefaultToolbar();
        MeetupLocationService.getInstance(getActivity()).disconnectLocation();
    }

    @Override
    public void onTravelMode(int travelMode) {
        mainCallback.showToolbarActionGroup(travelMode);
    }

    @UiThread
    @Override
    public void checkGPSOn(){
        LocationManager manager = (LocationManager) this.getContext().getSystemService(Context.LOCATION_SERVICE );

        //Checks to see if GPS is off
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {

            showAlertDialog(getString(R.string.dialog_GPS_on_heading),getString(R.string.dialog_GPS_on),
            new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });

        }
    }

    @UiThread
    public void showMap(FriendSession friendSession){
        //Hayden testing location - delete after done
        // show The Image in a ImageView
        String googleMapKey = "AIzaSyDnQO1YQdPv9G1O3R_l_u74pqMvrKTDa5c";
        String friendGeo = friendSession.getGeoCoordinate();
        String userGeo = sharedPreference.getUserGeoCoord();

        if(friendGeo != null && friendSession.getDistance() <= 3000) {
            googleMapStatic.setVisibility(View.VISIBLE);
            mapMessage.setVisibility(View.VISIBLE);
            String mapurl = "http://maps.googleapis.com/maps/api/staticmap?center="+userGeo+"&scale=2&size=640x540&maptype=roadmap&key="+googleMapKey+"&format=png&visual_refresh=true&markers=color:0xf39c12%7Clabel:%7C"+friendGeo+ "&markers=color:0x77a500%7Clabel:%7C"+ userGeo;
            Picasso.with(getContext()).load(mapurl).into(googleMapStatic);

        }

    }



    @UiThread
    public void hideMap(){
        googleMapStatic.setVisibility(View.GONE);
        mapMessage.setVisibility(View.GONE);
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

    @Click(R.id.btn_add_friends)
    protected void addFriends() {
        mainCallback.navToFriendList();
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
    @Override @Background
    public void onSessionRequest(FriendSession friendSession) {
        presenter.onSessionRequest(friendSession);
    }

    @Override @Background
    public void onCancelSession(FriendSession friendSession) {
        presenter.cancelSession(friendSession);
        showSnackBarMessage(getString(R.string.cancel_session_message));
        //hideMap();
    }

    @Override @Background
    public void onAcceptSession(FriendSession friendSession) {
        presenter.createNewSession(friendSession);

    }

    @Override @Background
    public void onIgnoreRequest(FriendSession friendSession) {
        presenter.ignoreSession(friendSession);
    }

    @Override @Background
    public void onRequestLocation(FriendSession friendSession) {
        presenter.requestLocationData(friendSession);
        //showMap(friendSession);
        placeMarkerOnMap(friendSession);
    }
    //endregion
}
