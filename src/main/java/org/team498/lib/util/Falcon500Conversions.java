package org.team498.lib.util;

// Credit to team 364 for this class
public class Falcon500Conversions {

	/**
	 * @param counts    Falcon Counts
	 * @param gearRatio Gear Ratio between Falcon and mechanism
	 * @return Degrees of Rotation of mechanism
	 */
	public static double falconToDegrees(double counts, double gearRatio) {
		return counts * (360.0 / (gearRatio * 2048.0));
	}

	/**
	 * @param degrees   Degrees of rotation of mechanism
	 * @param gearRatio Gear Ratio between Falcon and mechanism
	 * @return Falcon Counts
	 */
	public static double degreesToFalcon(double degrees, double gearRatio) {
		return degrees / (360.0 / (gearRatio * 2048.0));
	}

	/**
	 * @param velocityCounts Falcon Velocity Counts
	 * @param gearRatio      Gear Ratio between Falcon and mechanism (set to 1 for
	 *                       Falcon RPM)
	 * @return RPM of mechanism
	 */
	public static double falconToRPM(double velocityCounts, double gearRatio) {
		double motorRPM = velocityCounts * (600.0 / 2048.0);
		return motorRPM / gearRatio;
	}

	/**
	 * @param RPM       RPM of mechanism
	 * @param gearRatio Gear Ratio between Falcon and mechanism (set to 1 for Falcon
	 *                  RPM)
	 * @return RPM of mechanism
	 */
	public static double RPMToFalcon(double RPM, double gearRatio) {
		double motorRPM = RPM * gearRatio;
		return motorRPM * (2048.0 / 600.0);
	}

	/**
	 * @param velocitycounts Falcon Velocity Counts
	 * @param circumference  Circumference of Wheel
	 * @param gearRatio      Gear Ratio between Falcon and mechanism (set to 1 for
	 *                       Falcon RPM)
	 * @return Falcon Velocity Counts
	 */
	public static double falconToMPS(double velocitycounts, double circumference, double gearRatio) {
		double wheelRPM = falconToRPM(velocitycounts, gearRatio);
		return (wheelRPM * circumference) / 60;
	}

	/**
	 * @param velocity      Velocity MPS
	 * @param circumference Circumference of Wheel
	 * @param gearRatio     Gear Ratio between Falcon and mechanism (set to 1 for
	 *                      Falcon RPM)
	 * @return Falcon Velocity Counts
	 */
	public static double MPSToFalcon(double velocity, double circumference, double gearRatio) {
		double wheelRPM = ((velocity * 60) / circumference);
		return RPMToFalcon(wheelRPM, gearRatio);
	}
}
