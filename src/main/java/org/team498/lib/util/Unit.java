package org.team498.lib.util;

public class Unit {
	/**
	 * Converts a value in inches to meters
	 * 
	 * @param inches the value in inches
	 * @return the value in meters
	 */
	public static double inchesToMeters(double inches) {
		return inches / 39.37;
	}

	/**
	 * Converts a value in meters to inches
	 * 
	 * @param meters the value in meters
	 * @return the value in inches
	 */
	public static double metersToInches(double meters) {
		return meters * 39.37;
	}

	/**
	 * Converts a value in feet to meters
	 * 
	 * @param feet the value in feet
	 * @return the value in meters
	 */
	public static double feetToMeters(double feet) {
		return feet / 3.281;
	}

	/**
	 * Converts a value in meters to feet
	 * 
	 * @param meters the value in meters
	 * @return the value in feet
	 */
	public static double metersToFeet(double meters) {
		return meters * 3.281;
	}
}
