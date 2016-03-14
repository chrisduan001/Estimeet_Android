package estimeet.meetup.ui.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import estimeet.meetup.R;
import estimeet.meetup.di.HasComponent;

/**
 * Created by AmyDuan on 13/03/16.
 */
@EActivity(R.layout.activity_manage_friend)
public class ManageFriendActivity extends BaseActivity {

    @ViewById(R.id.tool_bar) Toolbar toolbar;
    @ViewById(R.id.toolbar_icon) ImageView estimeetIcon;
    @ViewById(R.id.toolbar_search) ImageView toolbarSearch;
    @ViewById(R.id.toolbar_title) TextView toolbarTitle;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @SuppressWarnings("ConstantConditions")
    @AfterViews
    public void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        estimeetIcon.setVisibility(View.GONE);
        toolbarSearch.setVisibility(View.VISIBLE);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.title_manage_friend));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Click(R.id.toolbar_search)
    protected void onSearchClicked() {
        Log.d(this.getClass().getSimpleName(), "Search in toolbar clicked");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
