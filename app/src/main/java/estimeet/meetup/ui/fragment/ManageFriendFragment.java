package estimeet.meetup.ui.fragment;

import android.os.Bundle;
import android.widget.ProgressBar;

import org.androidannotations.annotations.EFragment;

import javax.inject.Inject;

import estimeet.meetup.R;
import estimeet.meetup.di.components.ManageFriendComponent;
import estimeet.meetup.ui.presenter.BasePresenter;
import estimeet.meetup.ui.presenter.ManageFriendPresenter;

/**
 * Created by AmyDuan on 15/03/16.
 */
@EFragment(R.layout.fragment_manage_friend)
public class ManageFriendFragment extends BaseFragment implements ManageFriendPresenter.ManageFriendView {

    @Inject ManageFriendPresenter presenter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();

        presenter.setView(this);
    }

    private void initialize() {
        getComponent(ManageFriendComponent.class).inject(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    protected ProgressBar getProgressBar() {
        return null;
    }

    @Override
    public void showProgressDialog() {

    }
}
