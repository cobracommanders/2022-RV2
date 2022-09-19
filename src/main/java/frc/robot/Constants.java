package frc.robot;

public final class Constants {
	public static final class HopperConstants {
		public static final int kFrontHopperID = 20;
		public static final int kBackHopperID = 21;

		public static final double kColorSensorLeniency = 300;
		public static final double kProximitySensorLeniency = 150;

		// 0.35
		public static final double kHopperEjectSpeed = 0.35;
		public static final double kHopperLoadSpeed = 0.25;
		public static final double kHopperAlignSpeed = 0.2;

		public static final int kUpperSensorDIO = 0;
		public static final int kLowerSensorDIO = 3;
	}

	public static final class ShooterConstants {
		public static final int kFrontShooterID = 23;
		public static final int kBackShooterID = 22;

		public static final double kP = 0.02;
	}

	public static final class WristConstants {
		public static final double kP = 0.14;
		public static final double kPositionIn = 0;
		public static final double kPositionOut = 2;
		public static final int kLeftWristID = 31;
		public static final int kRightWristID = 37;
	}

	public static final class IntakeConstants {
		public static final int kLeftIntakeID = 30;
		public static final int kRightIntakeID = 33;

		public static final double kIntakeSpeed = 0.75;
		// NEO 550 at 0.75
	}

	public static final class CentererConstants {
		public static final int kCentererID = 34;

		public static final double kCentererSpeed = 0.7;
	}
	

	public static final class OIConstants {
		public static final int kDriverControllerID = 0;
		public static final int kOperatorControllerID = 1;
		public static final double kControllerRumbleRange = 1000;
	}

	public static final class DrivetrainConstants {
		/**
		 * The left-to-right distance between the drivetrain wheels
		 *
		 * Should be measured from center to center.
		 */
		public static final double kDrivetrainTrackwidthMeters = 0.5461;
		/**
		 * The front-to-back distance between the drivetrain wheels.
		 *
		 * Should be measured from center to center.
		 */
		public static final double kDrivetrainWheelbaseMeters = 0.5461;

		// public static final int kPigeonID = 0;

		public static final int kFrontLeftModuleDriveID = 3;
		public static final int kFrontLeftModuleSteerID = 4;
		public static final int kFrontLeftModuleEncoderID = 12;
		public static final double kFrontLeftModuleOffset = -Math.toRadians(137.1);

		public static final int kFrontRightModuleDriveID = 5;
		public static final int kFrontRightModuleSteerID = 6;
		public static final int kFrontRightModuleEncoderID = 13;
		public static final double kFrontRightModuleOffset = -Math.toRadians(312.4);

		public static final int kBackLeftModuleDriveID = 1;
		public static final int kBackLeftModuleSteerID = 2;
		public static final int kBackLeftModuleEncoderID = 11;
		public static final double kBackLeftModuleOffset = -Math.toRadians(74.5);

		public static final int kBackRightModuleDriveID = 7;
		public static final int kBackRightModuleSteerID = 8;
		public static final int kBackRightModuleEncoderID = 14;
		public static final double kBackRightModuleOffset = -Math.toRadians(28.0);

	}
}
