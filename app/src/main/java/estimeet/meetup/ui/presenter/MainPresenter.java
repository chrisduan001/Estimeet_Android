package estimeet.meetup.ui.presenter;

import javax.inject.Inject;

import estimeet.meetup.interactor.MainInteractor;
import estimeet.meetup.ui.BaseView;

/**
 * Created by AmyDuan on 6/02/16.
 */
public class MainPresenter extends BasePresenter {

    private MainView view;

    @Inject MainInteractor interactor;

    @Inject
    public MainPresenter(MainInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onAuthFailed() {

    }

    //region fragment call
    public void setView(MainView view) {
        this.view = view;
    }

    public void registerPushChannel() {
        interactor.registerPushChannel();
    }
    //endregion
    public interface MainView extends BaseView {

    }
}
