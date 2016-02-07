package estimeet.meetup.ui.fragment;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import estimeet.meetup.di.HasComponent;
import estimeet.meetup.di.components.MainComponent;
import estimeet.meetup.ui.presenter.BasePresenter;

/**
 * Created by AmyDuan on 6/02/16.
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        getPresenter().onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPresenter().onDestory();
    }

    @SuppressWarnings("unchecked")
    protected <T> T getComponent(Class<T> componentType) {
        return componentType.cast(((HasComponent<T>) getActivity()).getComponent());
    }

    protected abstract BasePresenter getPresenter();

    public void showShortToastMessage(String message) {
        showToastMessage(true, message);
    }

    public void showLongToastMessage(String message) {
        showToastMessage(false, message);
    }

    private void showToastMessage(boolean isShort, String message) {
        Toast.makeText(getActivity(), message, isShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
    }
}
