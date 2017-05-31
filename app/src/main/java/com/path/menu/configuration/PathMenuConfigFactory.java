package com.path.menu.configuration;

import android.view.Gravity;
import com.path.menu.CounterDegreeProvider;
import com.path.menu.DegreeProvider;
import com.path.menu.LinearDegreeProvider;

/**
 * Created by esafirm on 8/11/16.
 */
public class PathMenuConfigFactory {

  private static final int UNDEFINED = -1;

  public static PathMenuConfig makeRightToLeftMenu() {
    return new PathMenuConfig() {
      @Override public float getTotalSpacingDegrees() {
        return UNDEFINED;
      }

      @Override public int getMenuDistance() {
        return UNDEFINED;
      }

      @Override public DegreeProvider getDegreeProvider() {
        return new CounterDegreeProvider();
      }

      @Override public int getGravity() {
        return Gravity.BOTTOM | Gravity.RIGHT;
      }
    };
  }

  public static PathMenuConfig makeRadialMenu() {
    return new PathMenuConfig() {
      @Override public float getTotalSpacingDegrees() {
        return 180f;
      }

      @Override public int getMenuDistance() {
        return UNDEFINED;
      }

      @Override public DegreeProvider getDegreeProvider() {
        return new LinearDegreeProvider();
      }

      @Override public int getGravity() {
        return UNDEFINED;
      }
    };
  }
}
