package estimeet.meetup.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.EFragment;

import javax.inject.Inject;

import estimeet.meetup.R;
import estimeet.meetup.di.components.MainComponent;
import estimeet.meetup.ui.presenter.BasePresenter;
import estimeet.meetup.ui.presenter.MainPresenter;

/**
 * Created by AmyDuan on 6/02/16.
 */
@EFragment(R.layout.fragment_main)
public class MainFragment extends BaseFragment implements MainPresenter.MainView {

    @Inject MainPresenter presenter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();

        presenter.setView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return null;
    }

    private void initialize() {
        getComponent(MainComponent.class).inject(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }
}