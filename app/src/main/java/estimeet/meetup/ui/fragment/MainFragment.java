package estimeet.meetup.ui.fragment;

import android.os.Bundle;
import org.androidannotations.annotations.EFragment;
import javax.inject.Inject;
import javax.inject.Named;

import estimeet.meetup.R;
import estimeet.meetup.di.components.MainComponent;
import estimeet.meetup.model.User;
import estimeet.meetup.ui.presenter.BasePresenter;
import estimeet.meetup.ui.presenter.MainPresenter;

/**
 * Created by AmyDuan on 6/02/16.
 */
@EFragment(R.layout.fragment_main)
public class MainFragment extends BaseFragment implements MainPresenter.MainView {

    @Inject MainPresenter presenter;
    User user;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();

        presenter.setView(this);

        if (user == null) {
            getPresenter();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initialize() {
        getComponent(MainComponent.class).inject(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }
}
