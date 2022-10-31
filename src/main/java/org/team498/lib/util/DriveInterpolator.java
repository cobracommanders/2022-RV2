package org.team498.lib.util;

import java.util.Arrays;

/*
 * linearInterpolator - given a table of x and y values, will interpolate values
 * of y between known values of x using linear interpolation.
 * 
 * Usage: private double[][] data = { {1.0, 10.0}, {3.0, 31.0}, {10,100} };
 * private linearInterpolator lt = new linearInterpolator(data);
 * 
 * double y = lt.getInterpolatedValue(1.5); // returns 15.25
 * 
 * Credit: FRC team 5013
 */
public class DriveInterpolator {

	// https://therevisionist.org/software-engineering/java/tutorials/passing-2d-arrays/
	private double[][] table;
	private boolean initialized = false;
	private int flexIndex = 0;

	/**
	 * create linearInterpolator class
	 * 
	 * @param data, a table of x -> y mappings to be interpolated
	 */
	public DriveInterpolator(double[][] data) {
		build_table(data);
	}

	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * Build the internal representation of the table data.
	 * 
	 * @param data a table of data to be interpolated
	 */
	private void build_table(double[][] data) {
		int rows = data.length;
		int cols = data[0].length;

		table = new double[rows][cols];
		for (int x = 0; x < data.length; x++) {
			for (int y = 0; y < data[x].length; y++) {
				table[x][y] = data[x][y];
			}
		}
		Arrays.sort(table, (a, b) -> Double.compare(a[0], b[0]));
		initialized = true;
	}

	/**
	 * getInterpolatedValue() - return the interpolated value of y given x.
	 * 
	 * If the value of x is in the table, that value is returned.
	 * 
	 * If the value of x is not in the table, the closest two values of x are chosen
	 * and the value of y returned is interpolated between the corresponding y
	 * values.
	 * 
	 * If the value of x is greater than max x value, the corresponding value of y
	 * for max x is returned. If the value of x is less than the min x value, the
	 * corresponding value of y for the min x is returned.
	 *
	 * @param x, the value of x to get an interpolated y value for
	 * @return the linear interpolated value y
	 */
    public boolean isFinished(double time) {
        if (flexIndex >= table.length) {
            return true;
        }
        return false;
    }
	public double[] getInterpolatedValue(double x) {

		if (!initialized) {
			System.out.println("ERROR: Not Initialized");
			return new double[0];
		}

		int index = getIndex(x);

		// System.out.println("index of " + x + " is " + index);

		if (index >= table.length) {
			return table[table.length - 1];
		}
        double high_x = table[index][1];
		double high_y = table[index][2];
        double high_r = table[index][3];
		double high_t = table[index][0];
		if ((high_t == x) || (index == 0)) {
			return new double[] {high_t, high_x, high_y, high_r};
		}
        double low_x = table[index - 1][1];
		double low_y = table[index - 1][2];
        double low_r = table[index - 1][3];
		double low_t = table[index - 1][0];

		return new double[] {
            (low_x + (x - low_t) * (high_x - low_x) / (high_t - low_t)), 
            (low_y + (x - low_t) * (high_y - low_y) / (high_t - low_t)),
            (low_r + (x - low_t) * (high_r - low_r) / (high_t - low_t))
        };
	}
    //changed from linear search to binary -- since we use massive tables, we want it to be faster
    public int getIndex(double time) {
        int min = 0;
        int max = table.length - 1;
        int mid = (max + min) / 2;
        while (time <= table[mid][0] - 0.1 || time >= table[mid][0] + 0.1) {
            if (time > table[mid][0]) {
                min = mid;
            } else if (time < table[mid][0]) {
                max = mid;
            }
            mid = min + max / 2;
        }
		flexIndex = mid;
        return mid;
    }
	public int getFlexIndex() {
		return flexIndex;
	}
}