package com.path.menu;

import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.widget.ImageView;

/**
 * Menu Item.
 *
 * @author Siyamed SINIR
 */
public class PathMenuItem {

  private Drawable imgDrawable;
  private ImageView view;
  private Animation outAnimation;
  private Animation inAnimation;
  private Animation clickAnimation;

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

  void setInAnimation(Animation inAnimation) {
    this.inAnimation = inAnimation;
  }

  Animation getInAnimation() {
    return inAnimation;
  }

  void setOutAnimation(Animation outAnimation) {
    this.outAnimation = outAnimation;
  }

  Animation getOutAnimation() {
    return outAnimation;
  }

  void setClickAnimation(Animation clickAnim) {
    this.clickAnimation = clickAnim;
  }

  Animation getClickAnimation() {
    return clickAnimation;
  }
}