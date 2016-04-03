package estimeet.meetup.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import estimeet.meetup.R;
import estimeet.meetup.di.HasComponent;
import estimeet.meetup.di.Modules.ManageFriendModule;
import estimeet.meetup.di.components.DaggerManageFriendComponent;
import estimeet.meetup.di.components.ManageFriendComponent;
import estimeet.meetup.ui.fragment.ManageManageFriendFragment_;

/**
 * Created by AmyDuan on 13/03/16.
 */
@EActivity(R.layout.activity_manage_friend)
public class ManageFriendActivity extends BaseActivity implements HasComponent<ManageFriendComponent> {

    private ManageFriendComponent manageFriendComponent;

    @ViewById(R.id.tool_bar) Toolbar toolbar;
    @ViewById(R.id.toolbar_icon) ImageView estimeetIcon;
    @ViewById(R.id.toolbar_title) TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeInjector();

        replaceFragment(R.id.container, new ManageManageFriendFragment_());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @SuppressWarnings("ConstantConditions")
    @AfterViews
    public void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        estimeetIcon.setVisibility(View.GONE);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.title_manage_friend));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.toolbar_search:
                Log.d(this.getClass().getSimpleName(), "search clicked");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //region dagger
    private void initializeInjector() {
        manageFriendComponent = DaggerManageFriendComponent.builder()
                .applicationComponent(getApplicationComponent())
                .manageFriendModule(new ManageFriendModule())
                .build();
    }

    @Override
    public ManageFriendComponent getComponent() {
        return manageFriendComponent;
    }

    //endregion
}
