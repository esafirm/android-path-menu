package com.path.menu.configuration;

import com.path.menu.DegreeProvider;

/**
 * Created by esafirm on 8/11/16.
 */
public interface PathMenuConfig {
  float getTotalSpacingDegrees();
  int getMenuDistance();
  DegreeProvider getDegreeProvider();
  int getGravity();
}
