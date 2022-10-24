package org.team498.lib.math;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.abs;

public class MathUtils {
    public static final double EPSILON = 1e-9;

    private MathUtils() {}

    /**
     * @param value value to clamp
     * @param min minimum to clamp value
     * @param max maximum to clamp value
     * @return value within range (min, max) (inclusive)
     */
    public static double clamp(double value, double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("min cannot be grater than max");
        }
        return min(max, max(value, min));
    }

    /**
     * @param a first value
     * @param b second value
     * @param epsilon value threshold to check
     * @return is a within epsilon of b
     */
    public static boolean epsilonEquals(double a, double b, double epsilon) {
        return abs(a - b) < epsilon;
    }

    public static boolean epsilonEquals(double a, double b) {
        return epsilonEquals(a, b, EPSILON);
    }

    public static boolean isInRange(double lowerBound, double upperBound, double value) {
        return value >= lowerBound && value <= upperBound;
    }

    /**
     * @param a coefficient a
     * @param b coefficient b
     * @param c coefficient c
     * @return all real roots of the equation
     */
    public static double[] quadratic(double a, double b, double c) {
        double d = Math.sqrt(b * b - 4 * a * c);
        if(Double.isNaN(d)) {
            return new double[0];
        }
        return new double[] {
            (-b + d) / (2 * a),
            (-b - d) / (2 * a)
        };
    }

    /**
     * @param start 
     * @param end
     * @param t
     * @return linear interpolation at point t
     */
    public static double lerp(double start, double end, double t) {
        return start + (end - start) * t;
    }

    /**
     * @param start
     * @param end
     * @param query
     * @param shouldClamp
     * @return point t which query exists
     */
    public static double inverseLerp(double start, double end, double query, boolean shouldClamp) {
        double t = (query - start) / (end - start);
        return (shouldClamp) ? clamp(t, 0.0, 1.0) : t;
    }
}
