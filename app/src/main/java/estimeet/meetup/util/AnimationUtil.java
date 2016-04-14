package estimeet.meetup.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import estimeet.meetup.R;

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

    public static void performFadeInAnimation(Context context, final View view) {
        Animation fadeInAnim = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        view.startAnimation(fadeInAnim);

        fadeInAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private static Animator createExpandAnimator(View view, float offset) {
        return ObjectAnimator.ofFloat(view, "translationY", offset, 0).setDuration(150);
    }

    private static Animator createCollapseAnimator(View view, float offset) {
        return ObjectAnimator.ofFloat(view, "translationY", 0, offset).setDuration(150);
    }
}
