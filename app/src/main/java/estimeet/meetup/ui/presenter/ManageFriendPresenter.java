package estimeet.meetup.ui.presenter;

import javax.inject.Inject;

import estimeet.meetup.interactor.AddFriendInteractor;
import estimeet.meetup.ui.BaseView;

/**
 * Created by AmyDuan on 15/03/16.
 */
public class ManageFriendPresenter extends BasePresenter {

    private AddFriendInteractor addFriendInteractor;
    private ManageFriendView view;

    @Inject
    public ManageFriendPresenter(AddFriendInteractor addFriendInteractor) {
        this.addFriendInteractor = addFriendInteractor;
    }

    public void setView(ManageFriendView view) {
        this.view = view;
    }

    @Override
    public void onAuthFailed() {
    }

    public interface ManageFriendView extends BaseView {

    }
}
