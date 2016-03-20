package estimeet.meetup.ui.presenter;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import estimeet.meetup.ui.BaseView;

/**
 * Created by AmyDuan on 20/03/16.
 */
public class ManageProfilePresenter extends BasePresenter {

    private WeakReference<ManageProfileView> view;

    @Inject
    public ManageProfilePresenter() {

    }

    public void setView(ManageProfileView view) {
        this.view = new WeakReference<>(view);
    }

    @Override
    public void onAuthFailed() {
        view.get().onAuthFailed();
    }

    public interface ManageProfileView extends BaseView {

    }
}
