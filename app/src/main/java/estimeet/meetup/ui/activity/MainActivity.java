package estimeet.meetup.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;
import javax.inject.Named;

import estimeet.meetup.R;
import estimeet.meetup.di.HasComponent;
import estimeet.meetup.di.Modules.MainModule;
import estimeet.meetup.di.components.DaggerMainComponent;
import estimeet.meetup.di.components.MainComponent;
import estimeet.meetup.factory.TravelInfoFactory;
import estimeet.meetup.model.User;
import estimeet.meetup.ui.fragment.MainFragment;
import estimeet.meetup.ui.fragment.MainFragment_;
import estimeet.meetup.ui.fragment.ManageFriendFragment;
import estimeet.meetup.util.AnimationUtil;

/**
 * Created by AmyDuan on 6/02/16.
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements HasComponent<MainComponent>, MainFragment.MainCallback{

    private MainComponent mainComponent;

    @ViewById(R.id.tool_bar)                Toolbar toolbar;
    @ViewById(R.id.toolbar_icon)            ImageView toolbarAppIcon;
    @ViewById(R.id.toolbar_title)           TextView toolbarTitle;
    @ViewById(R.id.toolbar_action_group)    ViewGroup actionGroup;
    @ViewById(R.id.toolbar_action_walking)  ImageButton toolbarWalking;
    @ViewById(R.id.toolbar_action_transit)  ImageButton toolbarTransit;
    @ViewById(R.id.toolbar_action_car)      ImageButton toolbarCar;
    @ViewById(R.id.toolbar_message)      TextView toolbarMessage;

    @Inject @Named("currentUser") User user;

    private int currentTravelType = -1;

    //region layout&lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeInjector();

        if (user.userId == 0 || TextUtils.isEmpty(user.userName)) {
            //user not sigin or user haven't finished their profile yet
            startNewActivity(SignInActivity.getCallingIntent(this, user.userId));
            this.finish();
        } else {
            replaceFragment(R.id.container, new MainFragment_());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ManageFriendFragment.ACTIVITY_RESULT) {
            if (data.getBooleanExtra(ManageFriendFragment.RESULT_MESSAGE, false)) {
                ((MainFragment)getSupportFragmentManager().getFragments().get(0)).restartFriendCursor();
            }
        }
    }

    @AfterViews
    protected void setupToolbar() {
        setSupportActionBar(toolbar);
        setDefaultToolbar();
    }

    private void initializeInjector() {
        mainComponent = DaggerMainComponent.builder()
                .applicationComponent(getApplicationComponent())
                .mainModule(new MainModule(this))
                .build();

        mainComponent.inject(this);
    }

    @Override
    public MainComponent getComponent() {
        return mainComponent;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() <= 0) {
            this.finish();
        }
    }
    //endregion

    //region toolbar
    private void setDefaultToolbar() {
        if (actionGroup.getVisibility() != View.GONE) {
            AnimationUtil.performFadeOutAnimation(this, new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            resetToolbar();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    }, toolbarWalking, toolbarTransit, toolbarCar);
        } else {
            resetToolbar();
        }
    }

    private void resetToolbar() {
        toolbarAppIcon.setVisibility(View.VISIBLE);
        toolbarTitle.setVisibility(View.GONE);
        actionGroup.setVisibility(View.GONE);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }

        currentTravelType = -1;
    }
    //set up the init view
    private void setupActionGroup(int type) {
        if (type == currentTravelType) return;

        //fade in animation when set up for the first time
        if (toolbarAppIcon.getVisibility() != View.GONE) {
            toolbarAppIcon.setVisibility(View.GONE);
            actionGroup.setVisibility(View.VISIBLE);
            AnimationUtil.performFadeInAnimation(this, toolbarWalking, toolbarTransit, toolbarCar);
        }

        //reset icon to default and reselect
        setActionType(currentTravelType, true);
        setActionType(type, false);
        currentTravelType = type;
    }

    private void setActionType(int type, boolean reset) {
        switch (type) {
            case TravelInfoFactory.TRAVEL_MODE_WALK:
                toolbarWalking.setImageResource(reset ?
                        R.drawable.ic_directions_walk_grey_48dp : R.drawable.ic_directions_walk_green_48dp);
                toolbarMessage.setText(getString(R.string.travel_walk_message));
                break;
            case TravelInfoFactory.TRAVEL_MODE_DRIVE:
                toolbarCar.setImageResource(reset ?
                        R.drawable.ic_directions_car_grey_48dp : R.drawable.ic_directions_car_green_48dp);
                toolbarMessage.setText(getString(R.string.travel_car_message));
                break;
            case TravelInfoFactory.TRAVEL_MODE_TRANSIT:
                toolbarTransit.setImageResource(reset ?
                        R.drawable.ic_directions_bus_grey_48dp : R.drawable.ic_directions_bus_green_48dp);
                toolbarMessage.setText(getString(R.string.travel_bus_message));
                break;
            default:
                break;
        }
    }

    @Click(R.id.toolbar_action_walking)
    protected void onWalkingClicked() {
        setupActionGroup(TravelInfoFactory.TRAVEL_MODE_WALK);
        getMainFragment().setTravelMode(TravelInfoFactory.TRAVEL_MODE_WALK);
    }

    @Click(R.id.toolbar_action_car)
    protected void onCarClicked() {
        setupActionGroup(TravelInfoFactory.TRAVEL_MODE_DRIVE);
        getMainFragment().setTravelMode(TravelInfoFactory.TRAVEL_MODE_DRIVE);
    }

    @Click(R.id.toolbar_action_transit)
    protected void onTransitClicked() {
        setupActionGroup(TravelInfoFactory.TRAVEL_MODE_TRANSIT);
        getMainFragment().setTravelMode(TravelInfoFactory.TRAVEL_MODE_TRANSIT);
    }


    private MainFragment getMainFragment() {
        return (MainFragment) getSupportFragmentManager().getFragments().get(0);
    }
    //endregion

    //region from fragment
    @Override
    public void navToFriendList() {
        ManageFriendActivity_.intent(this).startForResult(ManageFriendFragment.ACTIVITY_RESULT);
    }

    @Override
    public void navToManageProfile() {
        ManageProfileActivity_.intent(this).start();
    }

    @Override
    public void onAuthFailed() {
        startNewActivity(SignInActivity.getCallingIntent(this, 0));
        this.finish();
    }

    @Override @UiThread
    public void showDefaultToolbar() {
        setDefaultToolbar();
    }

    @Override @UiThread
    public void showToolbarActionGroup(int type) {
        setupActionGroup(type);
    }

    //endregion
}
