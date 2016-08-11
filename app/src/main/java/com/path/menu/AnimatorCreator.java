package com.path.menu;

import android.content.Context;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Factory class for creating satellite in/out animations
 *
 * @author Siyamed SINIR
 */
public class AnimatorCreator {

  private static final int START_DELAY_FACTOR = 25;

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

  public static Animation createItemInAnimation(int index, long expandDuration, int x, int y) {

    RotateAnimation rotate =
        new RotateAnimation(720, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
            0.5f);
    rotate.setInterpolator(IN_ROTATE_INTERPOLATOR);
    rotate.setDuration(expandDuration);

    long delay = 250;
    if (expandDuration <= 250) {
      delay = expandDuration / 3;
    }

    long duration = 400;
    if ((expandDuration - delay) > duration) {
      duration = expandDuration - delay;
    }

    TranslateAnimation translate = new TranslateAnimation(x, 0, y, 0);
    translate.setDuration(duration);
    translate.setStartOffset(delay);
    translate.setInterpolator(IN_ANTICIPATE_INTERPOLATOR);

    AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
    long alphaDuration = 10;
    if (expandDuration < 10) {
      alphaDuration = expandDuration / 10;
    }
    alphaAnimation.setDuration(alphaDuration);
    alphaAnimation.setStartOffset((delay + duration) - alphaDuration);

    AnimationSet animationSet = new AnimationSet(false);
    animationSet.setFillAfter(false);
    animationSet.setFillBefore(true);
    animationSet.setFillEnabled(true);

    animationSet.addAnimation(alphaAnimation);
    animationSet.addAnimation(rotate);
    animationSet.addAnimation(translate);
    animationSet.setStartOffset(START_DELAY_FACTOR * index);
    animationSet.start();
    animationSet.startNow();

    return animationSet;
  }

  public static Animation createItemOutAnimation(int index, long expandDuration, int x, int y) {

    AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
    long alphaDuration = 60;
    if (expandDuration < 60) {
      alphaDuration = expandDuration / 4;
    }
    alphaAnimation.setDuration(alphaDuration);
    alphaAnimation.setStartOffset(0);

    TranslateAnimation translate = new TranslateAnimation(0, x, 0, y);
    translate.setStartOffset(0);
    translate.setDuration(expandDuration);
    translate.setInterpolator(OUT_OVERSHOOT_INTERPOLATOR);

    RotateAnimation rotate =
        new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
            0.5f);
    rotate.setInterpolator(OUT_ROTATE_INTERPOLATOR);

    long duration = 100;
    if (expandDuration <= 150) {
      duration = expandDuration / 3;
    }

    rotate.setDuration(expandDuration - duration);
    rotate.setStartOffset(duration);

    AnimationSet animationSet = new AnimationSet(false);
    animationSet.setFillAfter(true);
    animationSet.setFillBefore(true);
    animationSet.setFillEnabled(true);

    animationSet.addAnimation(alphaAnimation);
    animationSet.addAnimation(rotate);
    animationSet.addAnimation(translate);
    animationSet.setStartOffset(START_DELAY_FACTOR * index);

    return animationSet;
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
