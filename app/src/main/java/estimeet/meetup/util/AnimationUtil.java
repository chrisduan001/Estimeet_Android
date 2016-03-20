package estimeet.meetup.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by AmyDuan on 20/03/16.
 */
public class AnimationUtil {

    public static void performFabCollapseAnimation(float[] offset, final View... views) {
        AnimatorSet animatorSet = new AnimatorSet();
        Animator[] animators = new Animator[views.length];
        for (int i=0; i < views.length; i++) {
            animators[i] = createCollapseAnimator(views[i], offset[i]);
        }

        animatorSet.playTogether(animators);

        animatorSet.start();

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                for (View view: views) {
                    view.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public static void performFabExpandAnimation(float[] offset, View... views) {
        AnimatorSet animatorSet = new AnimatorSet();
        Animator[] animators = new Animator[views.length];
        for (int i=0; i < views.length; i++) {
            animators[i] = createExpandAnimator(views[i], offset[i]);
        }

        animatorSet.playTogether(animators);

        animatorSet.start();

        for (View view: views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    private static Animator createExpandAnimator(View view, float offset) {
        return ObjectAnimator.ofFloat(view, "translationY", offset, 0).setDuration(300);
    }

    private static Animator createCollapseAnimator(View view, float offset) {
        return ObjectAnimator.ofFloat(view, "translationY", 0, offset).setDuration(300);
    }
}
