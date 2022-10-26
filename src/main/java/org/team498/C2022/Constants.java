package org.team498.C2022;

public final class Constants {
	public static final class HopperConstants {
		public static final int kFrontHopperID = 20;
		public static final int kBackHopperID = 21;

		public static final double kColorSensorLeniency = 125; // Used to be 200

		public static final int kUpperSensorDIO = 0;
		public static final int kLowerSensorDIO = 1;
	}

	public static final class ShooterConstants {
		public static final int kFrontShooterID = 23;
		public static final int kBackShooterID = 22;
	}

	public static final class HoodConstants {
		public static final int kHoodLimitDIO = 2;
	}

	public static final class WristConstants {
		public static final double kPositionIn = 0;
		public static final double kPositionOut = 2;
		public static final int kLeftWristID = 31;
		public static final int kRightWristID = 37;
	}

	public static final class IntakeConstants {
		public static final int kIntakeID = 30;

		public static final double kIntakeSpeed = 0.85;
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

	public static final class LimelightConstants {
		public static final double kVisionTapeHeight = 101.625;
		public static final double kLimelightMountAngle = 30;
		public static final double kLimelightLensHeight = 0.247776 + 1.875 + 24; // May be slightly over
	}

	public static final class AutoConstants {
		public static final double kMaxSpeedMetersPerSecond = 3;
		public static final double kMaxAccelerationMetersPerSecondSquared = 3;
		public static final double kMaxAngularSpeedRadiansPerSecond = Math.PI;
		public static final double kMaxAngularSpeedRadiansPerSecondSquared = Math.PI;

		public static final double kPXController = 1;
		public static final double kPYController = 1;
		public static final double kPThetaController = 1;
	}

	public static final class DrivetrainConstants {
		// This is just copied from SDS, we need to find the real one still
		/*
		 * public static final double kMaxVelocityMetersPerSecond = 6380.0 / 60.0 *
		 * ((14.0 / 50.0) * (27.0 / 17.0) * (15.0 / 45.0)) * kDriveWheelDiameter *
		 * Math.PI;
		 */

		public static final double kMaxVelocityMetersPerSecond = 2;

		public static final double kSwerveModuleDistanceFromCenter = 10.75;

		public static final double kMK4IDriveReductionL2 = 6.75;
		public static final double kMK4ISteerReductionL2 = 21.428571428571428571428571428571; // 150 / 7

		public static final double kDriveWheelDiameter = 4;
		public static final double kDriveWheelCircumference = kDriveWheelDiameter * Math.PI;

		public static final int kFrontLeftDriveMotorID = 3;
		public static final int kFrontRightDriveMotorID = 5;
		public static final int kBackLeftDriveMotorID = 1;
		public static final int kBackRightDriveMotorID = 7;

		public static final int kFrontLeftSteerMotorID = 4;
		public static final int kFrontRightSteerMotorID = 6;
		public static final int kBackLeftSteerMotorID = 2;
		public static final int kBackRightSteerMotorID = 8;

		public static final int kFrontRightEncoderID = 13;
		public static final int kFrontLeftEncoderID = 12;
		public static final int kBackRightEncoderID = 14;
		public static final int kBackLeftEncoderID = 11;

		public static final double kFrontLeftModuleOffset = 137.1;
		public static final double kFrontRightModuleOffset = 312.4;
		public static final double kBackLeftModuleOffset = 74.5;
		public static final double kBackRightModuleOffset = 28.0;
	}
}
