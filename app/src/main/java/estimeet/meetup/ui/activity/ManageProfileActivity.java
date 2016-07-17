package estimeet.meetup.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import estimeet.meetup.di.Modules.ManageProfileModule;
import estimeet.meetup.di.components.DaggerManageProfileComponent;
import estimeet.meetup.di.components.ManageProfileComponent;
import estimeet.meetup.ui.fragment.ManageProfileFragment;
import estimeet.meetup.ui.fragment.ManageProfileFragment_;

/**
 * Created by AmyDuan on 20/03/16.
 */
@EActivity(R.layout.activity_manage_profile)
public class ManageProfileActivity extends BaseActivity implements HasComponent<ManageProfileComponent>
, ManageProfileFragment.ManageProfileCallback{

    private ManageProfileComponent component;

    private MenuItem saveMenuItem;

    @ViewById(R.id.tool_bar) Toolbar toolbar;
    @ViewById(R.id.toolbar_icon) ImageView estimeetIcon;
    @ViewById(R.id.toolbar_title) TextView toolbarTitle;

    //region lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeInjector();

        replaceFragment(R.id.container, new ManageProfileFragment_());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_save, menu);
        saveMenuItem = menu.findItem(R.id.toolbar_save);
        saveMenuItem.setVisible(false);
        return true;
    }

    @SuppressWarnings("ConstantConditions")
    @AfterViews
    protected void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        estimeetIcon.setVisibility(View.GONE);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.title_profile_and_setting));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        } else if (item.getItemId() == R.id.toolbar_save) {
            ((ManageProfileFragment)getSupportFragmentManager().getFragments().get(0))
            .onSaveClicked();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //endregion

    //region di
    private void initializeInjector() {
        component = DaggerManageProfileComponent.builder()
                .applicationComponent(getApplicationComponent())
                .manageProfileModule(new ManageProfileModule())
                .build();
    }
    //endregion

    //region fragment callback
    @Override
    public void onUserDpChanged() {
        saveMenuItem.setVisible(true);
    }

    @Override
    public void onUserDpSaved() {
        saveMenuItem.setVisible(false);
    }
    //endregion

    @Override
    public ManageProfileComponent getComponent() {
        return component;
    }
}
