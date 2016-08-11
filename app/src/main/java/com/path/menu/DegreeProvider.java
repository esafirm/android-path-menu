package com.path.menu;

/**
 * Interface for providing degrees between satellites.
 *
 * @author Siyamed SINIR
 */
public interface DegreeProvider {
  float[] getDegrees(int count, float totalDegrees);
}
