package frc.robot;

public final class Constants {
	public static final class HopperConstants {
		public static final int kFrontHopperID = 0;
		public static final int kBackHopperID = 0;

		public static final double kColorSensorLeniency = 0.5;
		public static final double kProximitySensorLeniency = 0.5;

		public static final double kHopperSpeed = 0.5;
	}

	public static final class WristConstants {
		public static final int kLowerWristLimitID = 0;
		public static final int kUpperWristLimitID = 0;
		public static final int kLeftWristID = 0;
		public static final int kRightWristID = 0;

		public static final double kWristSpeed = 0.25;
	}

	public static final class IntakeConstants {
		public static final int kLeftIntakeID = 0;
		public static final int kRightIntakeID = 0;

		public static final double kIntakeSpeed = 0.5;
	}

	public static final class CentererConstants {
		public static final int kCentererID = 0;

		public static final double kCentererSpeed = 0.5;
	}

	public static final class OIConstants {
		public static final int kDriverControllerID = 0;
		public static final int kOperatorControllerID = 1;

	}

	public static final class DrivetrainConstants {
		/**
		 * The left-to-right distance between the drivetrain wheels
		 *
		 * Should be measured from center to center.
		 */
		public static final double kDrivetrainTrackwidthMeters = 0.5461; // TODO Measure and set trackwidth
		/**
		 * The front-to-back distance between the drivetrain wheels.
		 *
		 * Should be measured from center to center.
		 */
		public static final double kDrivetrainWheelbaseMeters = 0.5461; // TODO Measure and set wheelbase

		public static final int kPigeonID = 0; // TODO Set Pigeon ID

		public static final int kFrontLeftModuleDriveID = 3;
		public static final int kFrontLeftModuleSteerID = 4;
		public static final int kFrontLeftModuleEncoderID = 12;
		public static final double kFrontLeftModuleOffset = -Math.toRadians(137.1); // TODO Measure and set front
																					// left steer offset

		public static final int kFrontRightModuleDriveID = 5;
		public static final int kFrontRightModuleSteerID = 6;
		public static final int kFrontRightModuleEncoderID = 13;
		public static final double kFrontRightModuleOffset = -Math.toRadians(312.4); // TODO Measure and set
																					// front right steer offset

		public static final int kBackLeftModuleDriveID = 1;
		public static final int kBackLeftModuleSteerID = 2;
		public static final int kBackLeftModuleEncoderID = 11;
		public static final double kBackLeftModuleOffset = -Math.toRadians(74.5); // TODO Measure and set back
																					// left steer offset

		public static final int kBackRightModuleDriveID = 7;
		public static final int kBackRightModuleSteerID = 8;
		public static final int kBackRightModuleEncoderID = 14;
		public static final double kBackRightModuleOffset = -Math.toRadians(28.0); // TODO Measure and set back
																					// right steer offset
	}
}
