package com.path.menu;

import android.animation.Animator;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Menu Item.
 *
 * @author Siyamed SINIR
 */
public class PathMenuItem {

  private Drawable imgDrawable;
  private ImageView view;

  private Animator clickAnimation;
  private Animator outAnimation;
  private Animator inAnimation;

  private int id;
  private int imgResourceId;

  public PathMenuItem(int id, int imgResourceId) {
    this.imgResourceId = imgResourceId;
    this.id = id;
  }

  public PathMenuItem(int id, Drawable imgDrawable) {
    this.imgDrawable = imgDrawable;
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getImgResourceId() {
    return imgResourceId;
  }

  public void setImgResourceId(int imgResourceId) {
    this.imgResourceId = imgResourceId;
  }

  public Drawable getImgDrawable() {
    return imgDrawable;
  }

  public void setImgDrawable(Drawable imgDrawable) {
    this.imgDrawable = imgDrawable;
  }

  void setView(ImageView view) {
    this.view = view;
  }

  ImageView getView() {
    return view;
  }

  void setInAnimation(Animator inAnimation) {
    this.inAnimation = inAnimation;
  }

  void setOutAnimation(Animator outAnimation) {
    this.outAnimation = outAnimation;
  }

  void setClickAnimation(Animator clickAnim) {
    this.clickAnimation = clickAnim;
  }

  /* --------------------------------------------------- */
  /* > HelperMethod */
  /* --------------------------------------------------- */

  public void startInAnimation() {
    outAnimation.end();
    inAnimation.start();
  }

  public void startOutAnimation() {
    inAnimation.end();
    outAnimation.start();
  }

  public void startClickAnimation(){
    clickAnimation.start();
  }
}