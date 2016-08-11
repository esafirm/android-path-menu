package com.path.menu.configuration;

import android.view.Gravity;
import com.path.menu.CounterDegreeProvider;
import com.path.menu.DegreeProvider;

/**
 * Created by esafirm on 8/11/16.
 */
public class PathMenuConfigFactory {

  public static PathMenuConfig makeRightToLeftMenu() {
    return new PathMenuConfig() {
      @Override public float getTotalSpacingDegrees() {
        return -1;
      }

      @Override public int getMenuDistance() {
        return -1;
      }

      @Override public DegreeProvider getDegreeProvider() {
        return new CounterDegreeProvider();
      }

      @Override public int getGravity() {
        return Gravity.BOTTOM | Gravity.RIGHT;
      }
    };
  }
}
