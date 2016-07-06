package estimeet.meetup.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ProgressBar;

import org.androidannotations.annotations.EFragment;

import estimeet.meetup.R;
import estimeet.meetup.di.components.SignInComponent;
import estimeet.meetup.ui.presenter.BasePresenter;

/**
 * Created by AmyDuan on 6/07/16.
 */
@EFragment(R.layout.fragment_permission)
public class PermissionFragment extends BaseFragment {

    

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initialize();
    }

    private void initialize() {
        getComponent(SignInComponent.class).inject(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected ProgressBar getProgressBar() {
        return null;
    }
}
