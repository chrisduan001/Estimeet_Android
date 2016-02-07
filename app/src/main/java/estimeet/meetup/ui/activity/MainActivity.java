package estimeet.meetup.ui.activity;

import android.os.Bundle;

import javax.inject.Inject;

import estimeet.meetup.R;
import estimeet.meetup.di.HasComponent;
import estimeet.meetup.di.Modules.MainModule;
import estimeet.meetup.di.components.DaggerMainComponent;
import estimeet.meetup.di.components.MainComponent;
import estimeet.meetup.ui.fragment.MainFragment;
import estimeet.meetup.util.Navigator;

/**
 * Created by AmyDuan on 6/02/16.
 */
public class MainActivity extends BaseActivity implements HasComponent<MainComponent> {

    private MainComponent mainComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeInjector();
        setContentView(R.layout.activity_main);

        replaceFragment(R.id.container, new MainFragment());
    }

    private void initializeInjector() {
        mainComponent = DaggerMainComponent.builder()
                .applicationComponent(getApplicationComponent())
                .mainModule(new MainModule())
                .build();
    }

    @Override
    public MainComponent getComponent() {
        return mainComponent;
    }
}
