package com.path.menu;

/**
 * Interface for providing degrees between satellites.
 *
 * @author Siyamed SINIR
 */
public interface IDegreeProvider {
  float[] getDegrees(int count, float totalDegrees);
}
