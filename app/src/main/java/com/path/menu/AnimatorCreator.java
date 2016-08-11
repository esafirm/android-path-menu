package com.path.menu;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * Factory class for creating satellite in/out animations
 *
 * @author Siyamed SINIR
 */
public class AnimatorCreator {

  private static final int START_DELAY_FACTOR = 25;

  /* --------------------------------------------------- */
  /* > OUT DURATION */
  /* --------------------------------------------------- */

  private static final int OUT_ALPHA_DURATION = 60; //60
  private static final int OUT_ROTATE_DURATION = 100; //100

  /* --------------------------------------------------- */
  /* > IN INTERPOLATOR */
  /* --------------------------------------------------- */

  private static final AccelerateInterpolator IN_ROTATE_INTERPOLATOR =
      new AccelerateInterpolator(1.2f);
  private static final AnticipateInterpolator IN_ANTICIPATE_INTERPOLATOR =
      new AnticipateInterpolator(3.0f);

  /* --------------------------------------------------- */
  /* > OUT INTERPOLATOR */
  /* --------------------------------------------------- */

  private static final DecelerateInterpolator OUT_ROTATE_INTERPOLATOR =
      new DecelerateInterpolator(1f);
  private static final OvershootInterpolator OUT_OVERSHOOT_INTERPOLATOR =
      new OvershootInterpolator(3f);


  /* --------------------------------------------------- */
  /* > Animation Factory */
  /* --------------------------------------------------- */

  public static Animator createItemInAnimation(View v, int index, long expandDur, int x, int y) {

    ObjectAnimator rotate = ObjectAnimator.ofFloat(v, "rotation", 720f);
    rotate.setInterpolator(IN_ROTATE_INTERPOLATOR);
    rotate.setDuration(expandDur);

    long delay = 250;
    if (expandDur <= 250) {
      delay = expandDur / 3;
    }

    long duration = 400;
    if ((expandDur - delay) > duration) {
      duration = expandDur - delay;
    }

    ObjectAnimator translateX = ObjectAnimator.ofFloat(v, "translationX", 0);
    ObjectAnimator translateY = ObjectAnimator.ofFloat(v, "translationY", 0);

    translateX.setDuration(duration);
    translateY.setDuration(duration);

    translateX.setInterpolator(IN_ANTICIPATE_INTERPOLATOR);
    translateY.setInterpolator(IN_ANTICIPATE_INTERPOLATOR);

    ObjectAnimator alpha = ObjectAnimator.ofFloat(v, "alpha", 0f);
    long alphaDuration = 10;
    if (expandDur < 10) {
      alphaDuration = expandDur / 10;
    }
    alpha.setDuration(alphaDuration);
    alpha.setStartDelay((delay + duration) - alphaDuration);

    AnimatorSet set = new AnimatorSet();
    set.playTogether(translateX, translateY, alpha);
    set.setStartDelay(START_DELAY_FACTOR * index);

    return set;
  }

  public static Animator createItemOutAnimation(View v, int index, long expandDur, int x, int y) {

    ObjectAnimator alpha = ObjectAnimator.ofFloat(v, "alpha", 1f);
    long alphaDuration = OUT_ALPHA_DURATION;
    if (expandDur < alphaDuration) {
      alphaDuration = expandDur / 4;
    }
    alpha.setDuration(alphaDuration);
    alpha.setStartDelay(0);

    ObjectAnimator translateX = ObjectAnimator.ofFloat(v, "translationX", x);
    ObjectAnimator translateY = ObjectAnimator.ofFloat(v, "translationY", y);

    translateX.setStartDelay(0);
    translateY.setStartDelay(0);

    translateX.setDuration(expandDur);
    translateY.setDuration(expandDur);

    translateX.setInterpolator(OUT_OVERSHOOT_INTERPOLATOR);
    translateY.setInterpolator(OUT_OVERSHOOT_INTERPOLATOR);

    ObjectAnimator rotate = ObjectAnimator.ofFloat(v, "rotation", 360f);
    rotate.setInterpolator(OUT_ROTATE_INTERPOLATOR);

    long duration = OUT_ROTATE_DURATION;
    if (expandDur <= 150) {
      duration = expandDur / 3;
    }

    rotate.setDuration(expandDur - duration);
    rotate.setStartDelay(duration);

    AnimatorSet set = new AnimatorSet();
    set.playTogether(alpha, rotate, translateX, translateY);
    set.setStartDelay(START_DELAY_FACTOR * index);

    return set;
  }

  public static Animation createMainButtonAnimation(Context context) {
    return AnimationUtils.loadAnimation(context, R.anim.sat_main_rotate_left);
  }

  public static Animation createMainButtonInverseAnimation(Context context) {
    return AnimationUtils.loadAnimation(context, R.anim.sat_main_rotate_right);
  }

  public static Animation createItemClickAnimation(Context context) {
    return AnimationUtils.loadAnimation(context, R.anim.sat_item_anim_click);
  }

  public static int getTranslateX(float degree, int distance) {
    return Double.valueOf(distance * Math.cos(Math.toRadians(degree))).intValue();
  }

  public static int getTranslateY(float degree, int distance) {
    return Double.valueOf(-1 * distance * Math.sin(Math.toRadians(degree))).intValue();
  }
}
