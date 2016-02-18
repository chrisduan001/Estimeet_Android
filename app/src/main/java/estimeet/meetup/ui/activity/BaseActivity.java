package estimeet.meetup.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import javax.inject.Inject;
import javax.inject.Named;

import estimeet.meetup.MainApplication;
import estimeet.meetup.di.components.ApplicationComponent;
import estimeet.meetup.model.User;
import estimeet.meetup.util.Navigator;

public class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationComponent().inject(this);
    }

    protected void startNewActivity(Intent intent) {
        startActivity(intent);
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((MainApplication) getApplication()).getApplicationComponent();
    }

    //region fragment action
    protected void replaceFragment(int layout, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(layout, fragment);
        transaction.commit();
    }
    //endregion
}
