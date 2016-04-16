package estimeet.meetup.ui.fragment;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;
import javax.inject.Named;

import estimeet.meetup.R;
import estimeet.meetup.di.components.MainComponent;
import estimeet.meetup.di.components.ManageProfileComponent;
import estimeet.meetup.model.User;
import estimeet.meetup.ui.presenter.BasePresenter;
import estimeet.meetup.ui.presenter.ManageProfilePresenter;

/**
 * Created by AmyDuan on 20/03/16.
 */
@EFragment(R.layout.fragment_manage_profile)
public class ManageProfileFragment extends BaseFragment implements ManageProfilePresenter.ManageProfileView {

    @Inject ManageProfilePresenter presenter;
    @Inject @Named("currentUser") User user;

    @ViewById(R.id.profile_name) TextView profileName;
    @ViewById(R.id.profile_id) TextView profileId;
    @ViewById(R.id.profile_mobile_number) TextView profileMobile;
    
    //region lifecycle
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
        presenter.setView(this);

        initProfileData();
    }

    private void initProfileData() {
        profileName.setText(user.userName);
        profileMobile.setText(user.phoneNumber);
    }

    private void initialize() {
        getComponent(ManageProfileComponent.class).inject(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }
    //endregion

    //region presenter call
    @Override
    public void showProgressDialog() {
    }

    @Override
    protected ProgressBar getProgressBar() {
        return null;
    }
    //endregion
}
