package org.team498.lib.drivers;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.net.PortForwarder;

public class Limelight {

	/*
	 * Left of target = positive
	 * Right of target = negative
	 */

	/*
	 * EXPOSURE:
	 * Used to black out most incoming data. For filtering out anything that
	 * doesn't reflect or emit light.
	 * 
	 * BLACK LEVEL OFFSET:
	 * Used to darken the camera input. For removing additional light sources such
	 * as arena lights.
	 * 
	 * THRESHOLDING:
	 * Used to filter out any pixels that are not the correct color. For filtering
	 * out the red and blue arena lights to only view the green reflection on the
	 * tape.
	 * 
	 * HUE:
	 * A pure color range
	 * 
	 * SATURATION:
	 * The white level of the color
	 * 
	 * VALUE:
	 * The black level of the color
	 * 
	 * EROSION:
	 * Removes some results, useful for multiple targets
	 * 
	 * DIALATION:
	 * Patches holes in a target
	 * 
	 * TARGET AREA:
	 * Used to filter out detected targets such as a large display screen
	 * 
	 * TARGET FULLNESS:
	 * A solid rectangle target will have a near-1.0 fullness, while a U-shaped
	 * target will have a low fullness.
	 * 
	 * ASPECT RATIO:
	 * A low aspect ratio describes a tall rectangle, while a high aspect ratio
	 * describes a wide rectangle.
	 * 
	 * DIRECTION FILTER:
	 * Removes targets at unwanted orientations
	 * 
	 * SPECKLE DETECTION:
	 * Rejects relatively small contours that have passed through all other filters.
	 * This is essential if a target must remain trackable from short-range and
	 * long-range.
	 * 
	 * TARGET GROUPING:
	 * Detects groups of targets, like the multiple sections of tape on the high hub
	 * 
	 * TARGETING REGION:
	 * Determines where to place the crosshair on the target
	 * 
	 * 
	 */
	private final NetworkTable limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
	private final NetworkTableEntry tx = limelightTable.getEntry("tx");
	private final NetworkTableEntry ty = limelightTable.getEntry("ty");
	private final NetworkTableEntry ta = limelightTable.getEntry("ta");
	private final NetworkTableEntry tv = limelightTable.getEntry("tv");
	private final NetworkTableEntry camMode = limelightTable.getEntry("camMode");
	private final NetworkTableEntry ledMode = limelightTable.getEntry("ledMode");
	// private final NetworkTableEntry pipeline = limelightTable.getEntry("pipeline");
	private final double lensHeight;
	private final double mountAngle;
	final double visionTapeHeight;

	/**
	 * Constructs Limelight
	 * @param lensHeight 
	 * @param mountAngle mount angle in degrees
	 * @param visionTapeHeght
	 */
	public Limelight(double lensHeight, double mountAngle, double visionTapeHeght) {
		PortForwarder.add(5800, "limelight.local", 5800);
		PortForwarder.add(5801, "limelight.local", 5801);
		PortForwarder.add(5802, "limelight.local", 5802);
		PortForwarder.add(5803, "limelight.local", 5803);
		PortForwarder.add(5804, "limelight.local", 5804);
		PortForwarder.add(5805, "limelight.local", 5805);
		this.lensHeight = lensHeight;
		this.mountAngle = mountAngle;
		this.visionTapeHeight = visionTapeHeght;
	}

	// LED modes of the limelight
	public static enum LEDMode {
		kOn, kOff, kBlink, kDefault;
	}

	/**
	 * @return Horizontal Offset From Crosshair To Target (-29.8 to 29.8 degrees)
	 */
	public double getTx() {
		return tx.getDouble(0);
	}

	/**
	 * @return Vertical Offset From Crosshair To Target (-24.85 to 24.85 degrees)
	 */
	public double getTy() {
		return ty.getDouble(0);
	}

	/**
	 * @return Target Area (0% of image to 100% of image)
	 */
	public double getTa() {
		return ta.getDouble(0);
	}

	/**
	 * @return Whether the limelight has any valid targets (0 or 1)
	 */
	public double getTv() {
		return tv.getDouble(0);
	}

	public double getDistance() {
		double angleToGoalDegrees = mountAngle + getTy();
		double angleToGoalRadians = angleToGoalDegrees * (3.14159 / 180.0);
		return (visionTapeHeight - lensHeight) / Math.tan(angleToGoalRadians);
	}

	/**
	 * @return Whether the limelight has any valid targets (true or false)
	 */
	public boolean hasTarget() {
		if (tv.getDouble(0) == 1)
			return true;
		return false;
	}

	// Disable the vision processing on the limelight
	public void disableProcessing() {
		camMode.setNumber(1);
	}

	// Enable the vision processing on the limelight
	public void enableProcessing() {
		camMode.setNumber(0);
	}

	// Sets the LED mode of the limelight
	public void setLEDMode(LEDMode mode) {
		switch (mode) {
			case kOn:
				ledMode.setNumber(3);
				break;

			case kOff:
				ledMode.setNumber(1);
				break;

			case kBlink:
				ledMode.setNumber(2);
				break;

			case kDefault:
				ledMode.setNumber(0);
				break;
		}
	}
}
