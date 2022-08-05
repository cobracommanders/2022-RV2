package frc.robot;

public final class Constants {
	public static final class HopperConstants {
		public static final int kFrontHopperID = 0;
		public static final int kBackHopperID = 0;

		public static final double kColorSensorLeniency = 0.5;
		public static final double kProximitySensorLeniency = 0.5;
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

	public static final class SwerveConstants {
		/**
		 * The left-to-right distance between the drivetrain wheels
		 *
		 * Should be measured from center to center.
		 */
		public static final double kDrivetrainTrackwidthMeters = 1.0; // TODO Measure and set trackwidth
		/**
		 * The front-to-back distance between the drivetrain wheels.
		 *
		 * Should be measured from center to center.
		 */
		public static final double kDrivetrainWheelbaseMeters = 1.0; // TODO Measure and set wheelbase

		public static final int kPigeonID = 0; // TODO Set Pigeon ID

		public static final int kFrontLeftModuleDriveID = 0; // TODO Set front left module drive motor ID
		public static final int kFrontLeftModuleSteerID = 0; // TODO Set front left module steer motor ID
		public static final int kFrontLeftModuleEncoderID = 0; // TODO Set front left steer encoder ID
		public static final double kFrontLeftModuleOffset = -Math.toRadians(0.0); // TODO Measure and set front
																					// left steer offset

		public static final int kFrontRightModuleDriveID = 0; // TODO Set front right drive motor ID
		public static final int kFrontRightModuleSteerID = 0; // TODO Set front right steer motor ID
		public static final int kFrontRightModuleEncoderID = 0; // TODO Set front right steer encoder ID
		public static final double kFrontRightModuleOffset = -Math.toRadians(0.0); // TODO Measure and set
																					// front right steer offset

		public static final int kBackLeftModuleDriveID = 0; // TODO Set back left drive motor ID
		public static final int kBackLeftModuleSteerID = 0; // TODO Set back left steer motor ID
		public static final int kBackLeftModuleEncoderID = 0; // TODO Set back left steer encoder ID
		public static final double kBackLeftModuleOffset = -Math.toRadians(0.0); // TODO Measure and set back
																					// left steer offset

		public static final int kBackRightModuleDriveID = 0; // TODO Set back right drive motor ID
		public static final int kBackRightModuleSteerID = 0; // TODO Set back right steer motor ID
		public static final int kBackRightModuleEncoderID = 0; // TODO Set back right steer encoder ID
		public static final double kBackRightModuleOffset = -Math.toRadians(0.0); // TODO Measure and set back
																					// right steer offset
	}
}
