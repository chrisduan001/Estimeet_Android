package estimeet.meetup.ui.presenter;

import android.graphics.Bitmap;

import javax.inject.Inject;

import estimeet.meetup.interactor.SignInInteractor;
import estimeet.meetup.ui.BaseView;

/**
 * Created by AmyDuan on 8/02/16.
 */
public class SignInPresenter extends BasePresenter {

    private SignInView view;

    @Inject SignInInteractor signInInteractor;

    @Inject
    public SignInPresenter(SignInInteractor interactor) {
        this.signInInteractor = interactor;
    }

    @Override
    public void onResume() {

    }

    public void setView(SignInView view) {
        this.view = view;
    }

    public interface SignInView extends BaseView {

    }
}
