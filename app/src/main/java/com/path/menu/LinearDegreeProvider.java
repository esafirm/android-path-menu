package com.path.menu;

/**
 * Linearly distributes satellites in the given total degree.
 *
 * @author Siyamed SINIR
 */
public class LinearDegreeProvider implements DegreeProvider {
  public float[] getDegrees(int count, float totalDegrees) {
    if (count < 1) {
      return new float[] {};
    }

    if (count == 1) {
      return new float[] { 45f };
    }

    float[] result;
    int tmpCount = count - 1;

    result = new float[count];
    float delta = totalDegrees / tmpCount;

    for (int index = 0; index < count; index++) {
      result[index] = index * delta;
    }

    return result;
  }
}
