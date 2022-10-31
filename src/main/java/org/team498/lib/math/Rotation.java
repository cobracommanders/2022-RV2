package org.team498.lib.math;

public class Rotation {
	private double degrees;

	/**
	 * Creates a new rotation
	 */
	public Rotation() {
		degrees = 0;
	}

	/**
	 * Creates a new rotation
	 * 
	 * @param degrees initial value in degrees
	 */
	public Rotation(double degrees) {
		this.degrees = degrees;
	}

	/**
	 * Gets the rotation in an unsigned value from 0 to 360 degrees
	 * 
	 * @return unsigned rotation
	 */
	public double getUnsignedDegrees() {
		double x = degrees % 360;
		return x < 0 ? x + 360 : x;
	}

	/**
	 * Gets the rotation in a signed value from -180 to 180 degreees
	 * 
	 * @return signed rotation
	 */
	public double getSignedDegrees() {
		double x = getUnsignedDegrees();
		return x > 180 ? x - 360 : x;
	}

	/**
	 * Get the rotation in degrees
	 * 
	 * @return the rotation in degrees
	 */
	public double getRawDegrees() {
		return degrees;
	}

	/**
	 * Get the rotation in radians
	 * 
	 * @return the rotation in radians
	 */
	public double getRawRadians() {
		return degrees * (Math.PI / 180);
	}
}
