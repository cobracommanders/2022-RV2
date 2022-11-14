package org.team498.C2022.subsystems;

import static org.team498.C2022.Constants.DrivetrainConstants.kBackLeftDriveMotorID;
import static org.team498.C2022.Constants.DrivetrainConstants.kBackLeftEncoderID;
import static org.team498.C2022.Constants.DrivetrainConstants.kBackLeftModuleOffset;
import static org.team498.C2022.Constants.DrivetrainConstants.kBackLeftSteerMotorID;
import static org.team498.C2022.Constants.DrivetrainConstants.kBackRightDriveMotorID;
import static org.team498.C2022.Constants.DrivetrainConstants.kBackRightEncoderID;
import static org.team498.C2022.Constants.DrivetrainConstants.kBackRightModuleOffset;
import static org.team498.C2022.Constants.DrivetrainConstants.kBackRightSteerMotorID;
import static org.team498.C2022.Constants.DrivetrainConstants.kFrontLeftDriveMotorID;
import static org.team498.C2022.Constants.DrivetrainConstants.kFrontLeftEncoderID;
import static org.team498.C2022.Constants.DrivetrainConstants.kFrontLeftModuleOffset;
import static org.team498.C2022.Constants.DrivetrainConstants.kFrontLeftSteerMotorID;
import static org.team498.C2022.Constants.DrivetrainConstants.kFrontRightDriveMotorID;
import static org.team498.C2022.Constants.DrivetrainConstants.kFrontRightEncoderID;
import static org.team498.C2022.Constants.DrivetrainConstants.kFrontRightModuleOffset;
import static org.team498.C2022.Constants.DrivetrainConstants.kFrontRightSteerMotorID;
import static org.team498.C2022.Constants.DrivetrainConstants.kMaxVelocityMetersPerSecond;
import static org.team498.C2022.Constants.DrivetrainConstants.kSwerveModuleDistanceFromCenter;

import com.kauailabs.navx.frc.AHRS;

import org.team498.lib.drivers.SwerveModule;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.ADIS16448_IMU;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {

	private final SwerveModule frontLeft;
	private final SwerveModule frontRight;
	private final SwerveModule backLeft;
	private final SwerveModule backRight;
	private final SwerveModule[] swerveModules;

	private static SwerveDriveKinematics kinematics;

	// Distance of the swerve modules from the center of the robot converted to
	// meters
	private final double moduleDistance = Units.inchesToMeters(kSwerveModuleDistanceFromCenter);

	// Create positions for the swerve modules relative to the center of the robot

	// This is used by the kinematics and would be mainly helpful for determining
	// rotation on a non-square base or with more than 4 swerve modules
	private final Translation2d frontLeftModulePosition = new Translation2d(moduleDistance, moduleDistance);
	private final Translation2d frontRightModulePosition = new Translation2d(moduleDistance, -moduleDistance);
	private final Translation2d backLeftModulePosition = new Translation2d(-moduleDistance, moduleDistance);
	private final Translation2d backRightModulePosition = new Translation2d(-moduleDistance, -moduleDistance);

	private final SwerveDriveOdometry odometry;

	// public static AHRS IMU = new AHRS(Port.kUSB1);
	public static ADIS16448_IMU IMU = new ADIS16448_IMU();

	private final Field2d field = new Field2d();

	public void zeroGyro() {
		IMU.reset();
	}

	public void calibrateGyro() {
		IMU.calibrate();
	}

	public Drivetrain() {
		frontLeft = new SwerveModule(
				// Drive motor ID
				kFrontLeftDriveMotorID,
				// Steer Motor ID
				kFrontLeftSteerMotorID,
				// CANCoder ID
				kFrontLeftEncoderID,
				// Offset
				kFrontLeftModuleOffset);

		// Same for the rest of them
		frontRight = new SwerveModule(kFrontRightDriveMotorID, kFrontRightSteerMotorID, kFrontRightEncoderID,
				kFrontRightModuleOffset);
		backLeft = new SwerveModule(kBackLeftDriveMotorID, kBackLeftSteerMotorID, kBackLeftEncoderID,
				kBackLeftModuleOffset);
		backRight = new SwerveModule(kBackRightDriveMotorID, kBackRightSteerMotorID, kBackRightEncoderID,
				kBackRightModuleOffset);

		// Create an array of all the swerve modules to make editing them easier
		swerveModules = new SwerveModule[] {
				frontLeft,
				frontRight,
				backLeft,
				backRight
		};

		// Setup the kinematics
		kinematics = new SwerveDriveKinematics(
				frontLeftModulePosition,
				frontRightModulePosition,
				backLeftModulePosition,
				backRightModulePosition);

		odometry = new SwerveDriveOdometry(kinematics, Rotation2d.fromDegrees(0));

		matchEncoders();

		resetOdometry(new Pose2d(8, 4, Rotation2d.fromDegrees(0)));
	}

	public void matchEncoders() {
		for (SwerveModule swerveModule : swerveModules) {
			swerveModule.matchEncoders();
		}
	}

	public static SwerveDriveKinematics getKinematics() {
		return kinematics;
	}

	public Pose2d getPose() {
		return odometry.getPoseMeters();
	}

	public void resetOdometry(Pose2d pose) {
		odometry.resetPosition(pose, Rotation2d.fromDegrees(getYaw()));
	}

	public void setModuleStates(SwerveModuleState[] moduleStates) {
		setModuleStates(moduleStates, false);
	}

	public void setModuleStates(SwerveModuleState[] moduleStates, boolean force) {
		SwerveDriveKinematics.desaturateWheelSpeeds(
				// The optimized states
				moduleStates,
				// The maximum velocity of the robot
				kMaxVelocityMetersPerSecond);

		for (int i = 0; i < swerveModules.length; i++) {
			// Set the motors of the swerve module to the calculated state
			swerveModules[i].setState(moduleStates[i], force);
		}
	}

	public void drive(ChassisSpeeds chassisSpeeds) {
		drive(chassisSpeeds, new Translation2d(0, 0));
	}

	// Method to set the swerve drive to desired speed of direction and rotation
	public void drive(ChassisSpeeds chassisSpeeds, Translation2d rotation) {
		// Use the kinematics to set the desired speed and angle for each swerve module
		// using the input velocities for direction and rotation
		setModuleStates(kinematics.toSwerveModuleStates(chassisSpeeds, rotation), false);
	}

	/**
	 * @return The current yaw angle in degrees (-180 to 180)
	 */
	public double getYaw180() {
		// return -IMU.getYaw();
		return -IMU.getAngle();
	}

	/**
	 * @return The total accumulated yaw angle in degrees
	 */
	public double getYaw() {
		// return IMU.getAngle();
		return IMU.getAngle();
	}

	// Return an array of all the module states
	public SwerveModuleState[] getModuleStates() {
		return new SwerveModuleState[] {
				frontLeft.getState(),
				frontRight.getState(),
				backLeft.getState(),
				backRight.getState()
		};
	}

	public double getSpeedX() {
		ChassisSpeeds speed = kinematics.toChassisSpeeds(getModuleStates());
		return speed.vxMetersPerSecond;
	}
	public double getSpeedY() {
		ChassisSpeeds speed = kinematics.toChassisSpeeds(getModuleStates());
		return speed.vyMetersPerSecond;
	}

	public boolean isMoving() {
		ChassisSpeeds speed = kinematics.toChassisSpeeds(getModuleStates());
		return ((Math.abs(speed.vxMetersPerSecond) + Math.abs(speed.vyMetersPerSecond)
				+ Math.abs(speed.omegaRadiansPerSecond)) > 0.1);
	}

	// Set a number to 0 which counts up to 500 every 10 seconds to reset the
	// encoders
	private int encoderResetTimer = 0;
	private int idleResetTimer = 0;

	@Override
	public void periodic() {
		odometry.update(Rotation2d.fromDegrees(-getYaw()), getModuleStates());
		field.setRobotPose(getPose());
		if (encoderResetTimer++ > 500 && !isMoving() && idleResetTimer++ > 50) {
			matchEncoders();
			encoderResetTimer = 0;
			idleResetTimer = 0;
		}
		if (isMoving()) {
			idleResetTimer = 0;
		}

		// SmartDashboard.putBoolean("moving", isMoving());
		SmartDashboard.putData(field);
		SmartDashboard.putNumber("gyro", getYaw180());
		SmartDashboard.putData(this);
	}
}
