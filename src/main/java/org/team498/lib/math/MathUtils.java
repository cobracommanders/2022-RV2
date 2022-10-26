package org.team498.lib.math;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class MathUtils {
	/**
	 * @param value value to clamp
	 * @param min   minimum return value
	 * @param max   maximum return value
	 * @return value within range (min, max) (inclusive)
	 */
	public static double clamp(double value, double min, double max) {
		return min(max, max(value, min));
	}

	/**
	 * @return true if value is within upperBound and lowerBound
	 */
	public static boolean isInRange(double value, double lowerBound, double upperBound) {
		return value >= lowerBound && value <= upperBound;
	}
}
