package estimeet.meetup.ui.activity;

import android.os.Bundle;
import org.androidannotations.annotations.EActivity;
import estimeet.meetup.R;
import estimeet.meetup.di.HasComponent;
import estimeet.meetup.di.components.DaggerMainComponent;
import estimeet.meetup.di.components.MainComponent;
import estimeet.meetup.ui.fragment.MainFragment_;

/**
 * Created by AmyDuan on 6/02/16.
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements HasComponent<MainComponent> {

    private MainComponent mainComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeInjector();

        replaceFragment(R.id.container, new MainFragment_());
    }

    private void initializeInjector() {
        mainComponent = DaggerMainComponent.builder()
                .applicationComponent(getApplicationComponent())
                .build();
    }

    @Override
    public MainComponent getComponent() {
        return mainComponent;
    }
}
