package org.team498.C2022.subsystems;

import static org.team498.C2022.Constants.DrivetrainConstants.kBackLeftModuleDriveID;
import static org.team498.C2022.Constants.DrivetrainConstants.kBackLeftModuleEncoderID;
import static org.team498.C2022.Constants.DrivetrainConstants.kBackLeftModuleOffset;
import static org.team498.C2022.Constants.DrivetrainConstants.kBackLeftModuleSteerID;
import static org.team498.C2022.Constants.DrivetrainConstants.kBackRightModuleDriveID;
import static org.team498.C2022.Constants.DrivetrainConstants.kBackRightModuleEncoderID;
import static org.team498.C2022.Constants.DrivetrainConstants.kBackRightModuleOffset;
import static org.team498.C2022.Constants.DrivetrainConstants.kBackRightModuleSteerID;
import static org.team498.C2022.Constants.DrivetrainConstants.kDrivetrainTrackwidthMeters;
import static org.team498.C2022.Constants.DrivetrainConstants.kDrivetrainWheelbaseMeters;
import static org.team498.C2022.Constants.DrivetrainConstants.kFrontLeftModuleDriveID;
import static org.team498.C2022.Constants.DrivetrainConstants.kFrontLeftModuleEncoderID;
import static org.team498.C2022.Constants.DrivetrainConstants.kFrontLeftModuleOffset;
import static org.team498.C2022.Constants.DrivetrainConstants.kFrontLeftModuleSteerID;
import static org.team498.C2022.Constants.DrivetrainConstants.kFrontRightModuleDriveID;
import static org.team498.C2022.Constants.DrivetrainConstants.kFrontRightModuleEncoderID;
import static org.team498.C2022.Constants.DrivetrainConstants.kFrontRightModuleOffset;
import static org.team498.C2022.Constants.DrivetrainConstants.kFrontRightModuleSteerID;

import java.util.Optional;

import com.kauailabs.navx.frc.AHRS;
import com.swervedrivespecialties.swervelib.Mk4ModuleConfiguration;
import com.swervedrivespecialties.swervelib.Mk4iSwerveModuleHelper;
import com.swervedrivespecialties.swervelib.SdsModuleConfigurations;
import com.swervedrivespecialties.swervelib.SwerveModule;

import org.team498.lib.control.CentripetalAccelerationConstraint;
import org.team498.lib.control.FeedforwardConstraint;
import org.team498.lib.control.HolonomicMotionProfiledTrajectoryFollower;
import org.team498.lib.control.MaxAccelerationConstraint;
import org.team498.lib.control.PIDConstants;
import org.team498.lib.control.TrajectoryConstraint;
import org.team498.lib.drivers.Limelight;
import org.team498.lib.math.RigidTransform2;
import org.team498.lib.math.Rotation2;
import org.team498.lib.math.Vector2;
import org.team498.lib.util.DrivetrainFeedforwardConstants;
import org.team498.lib.util.HolonomicDriveSignal;
import org.team498.lib.util.HolonomicFeedforward;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {
	/**
	 * The maximum voltage that will be delivered to the drive motors.
	 * <p>
	 * This can be reduced to cap the robot's maximum speed. Typically, this is
	 * useful during initial testing of the robot.
	 */
	public static final double kMaxVoltage = 12.0;
	// The formula for calculating the theoretical maximum velocity is:
	// <Motor free speed RPM> / 60 * <Drive reduction> * <Wheel diameter meters> *
	// pi
	// By default this value is setup for a Mk3 standard module using Falcon500s to
	// drive.
	// An example of this constant for a Mk4 L2 module with NEOs to drive is:
	// 5880.0 / 60.0 / SdsModuleConfigurations.MK4_L2.getDriveReduction() *
	// SdsModuleConfigurations.MK4_L2.getWheelDiameter() * Math.PI
	/**
	 * The maximum velocity of the robot in meters per second.
	 * <p>
	 * This is a measure of how fast the robot should be able to drive in a straight
	 * line.
	 */
	public static final double kMaxVelocityMetersPerSecond = 6380.0 / 60.0 *
			SdsModuleConfigurations.MK4I_L2.getDriveReduction() *
			SdsModuleConfigurations.MK4I_L2.getWheelDiameter() * Math.PI;
	/**
	 * 0
	 * The maximum angular velocity of the robot in radians per second.
	 * <p>
	 * This is a measure of how fast the robot can rotate in place.
	 */
	// Here we calculate the theoretical maximum angular velocity. You can also
	// replace this with a measured amount.
	public static final double maxAngularVelocityRadiansPerSecond = kMaxVelocityMetersPerSecond /
			Math.hypot(kDrivetrainTrackwidthMeters / 2.0, kDrivetrainWheelbaseMeters / 2.0);

	private final SwerveDriveKinematics kinematics = new SwerveDriveKinematics(
			// Front left
			new Translation2d(kDrivetrainTrackwidthMeters / 2.0, kDrivetrainWheelbaseMeters / 2.0),
			// Front right
			new Translation2d(kDrivetrainTrackwidthMeters / 2.0, -kDrivetrainWheelbaseMeters / 2.0),
			// Back left
			new Translation2d(-kDrivetrainTrackwidthMeters / 2.0, kDrivetrainWheelbaseMeters / 2.0),
			// Back right
			new Translation2d(-kDrivetrainTrackwidthMeters / 2.0, -kDrivetrainWheelbaseMeters / 2.0));

	private SwerveDriveOdometry odometry = new SwerveDriveOdometry(kinematics, getGyroAngle(),
			new Pose2d(8, 4, Rotation2d.fromDegrees(0)));

	// private final PigeonIMU pigeon = new PigeonIMU(kPigeonID);
	// private final ADXRS450_Gyro IMU = new ADXRS450_Gyro();
	public static AHRS IMU = new AHRS(Port.kUSB1);

	// These are our modules. We initialize them in the constructor.
	private final SwerveModule frontLeftModule;
	private final SwerveModule frontRightModule;
	private final SwerveModule backLeftModule;
	private final SwerveModule backRightModule;

	private ChassisSpeeds chassisSpeeds = new ChassisSpeeds(0.0, 0.0, 0.0);

	private final Mk4ModuleConfiguration configuration = new Mk4ModuleConfiguration();

	public Drivetrain() {
		configuration.setDriveCurrentLimit(35);
		configuration.setSteerCurrentLimit(20);

		frontLeftModule = Mk4iSwerveModuleHelper.createFalcon500(
				configuration,
				Mk4iSwerveModuleHelper.GearRatio.L2,
				kFrontLeftModuleDriveID,
				kFrontLeftModuleSteerID,
				kFrontLeftModuleEncoderID,
				// This is how much the steer encoder is offset from true zero (In our case,
				// zero is facing straight forward)
				kFrontLeftModuleOffset);

		frontRightModule = Mk4iSwerveModuleHelper.createFalcon500(
				configuration,
				Mk4iSwerveModuleHelper.GearRatio.L2,
				kFrontRightModuleDriveID,
				kFrontRightModuleSteerID,
				kFrontRightModuleEncoderID,
				kFrontRightModuleOffset);

		backLeftModule = Mk4iSwerveModuleHelper.createFalcon500(
				configuration,
				Mk4iSwerveModuleHelper.GearRatio.L2,
				kBackLeftModuleDriveID,
				kBackLeftModuleSteerID,
				kBackLeftModuleEncoderID,
				kBackLeftModuleOffset);

		backRightModule = Mk4iSwerveModuleHelper.createFalcon500(
				configuration,
				Mk4iSwerveModuleHelper.GearRatio.L2,
				kBackRightModuleDriveID,
				kBackRightModuleSteerID,
				kBackRightModuleEncoderID,
				kBackRightModuleOffset);

		System.out.println(kMaxVelocityMetersPerSecond);
	}

	public void zeroGyro() {
		IMU.reset();
	}

	public void calibrateGyro() {
		IMU.calibrate();
	}

	/**
	 * Returns the reading of the gyro sensor from 180 to -180
	 */
	public Rotation2d getGyroAngle() {
		return Rotation2d.fromDegrees(-IMU.getYaw());
	}

	public double IMUAngle() {
		return IMU.getAngle();
	}

	public void drive(ChassisSpeeds chassisSpeeds) {
		this.chassisSpeeds = chassisSpeeds;
	}

	private Field2d field = new Field2d();

	private Rotation2 getAngularVelocity() {
		return Rotation2.fromDegrees(IMU.getRate());
	}

	HolonomicDriveSignal driveSignal = new HolonomicDriveSignal(Vector2.ZERO, Rotation2.ZERO, false);
	Vector2 velocity = Vector2.ZERO;

	@Override
	public void periodic() {
		Optional<HolonomicDriveSignal> trajectorySignal = follower.update(
				RigidTransform2.fromPose2d(odometry.getPoseMeters()),
				velocity,
				getAngularVelocity().toRadians(),
				.3,
				.1);
		if (trajectorySignal.isPresent()) {
			driveSignal = trajectorySignal.get();
			driveSignal = new HolonomicDriveSignal(
					driveSignal.getTranslation().scale(1.0 / RobotController.getBatteryVoltage()),
					driveSignal.getRotation().toRadians() / RobotController.getBatteryVoltage(),
					driveSignal.isFieldOriented());
			chassisSpeeds = new ChassisSpeeds(driveSignal.getTranslation().x, driveSignal.getTranslation().y,
					driveSignal.getRotation().toRadians());
		}

		SmartDashboard.putNumber("Gyro", IMU.getAngle());
		SwerveModuleState[] states = kinematics.toSwerveModuleStates(chassisSpeeds);
		SwerveDriveKinematics.desaturateWheelSpeeds(states, kMaxVelocityMetersPerSecond);

		frontLeftModule.set(states[0].speedMetersPerSecond / kMaxVelocityMetersPerSecond * kMaxVoltage,
				states[0].angle.getRadians());
		frontRightModule.set(states[1].speedMetersPerSecond / kMaxVelocityMetersPerSecond * kMaxVoltage,
				states[1].angle.getRadians());
		backLeftModule.set(states[2].speedMetersPerSecond / kMaxVelocityMetersPerSecond * kMaxVoltage,
				states[2].angle.getRadians());
		backRightModule.set(states[3].speedMetersPerSecond / kMaxVelocityMetersPerSecond * kMaxVoltage,
				states[3].angle.getRadians());

		odometry.update(getGyroAngle(), states);
		field.setRobotPose(odometry.getPoseMeters());
		velocity = new Vector2(kinematics.toChassisSpeeds(states).vxMetersPerSecond,
				kinematics.toChassisSpeeds(states).vyMetersPerSecond);
		// SmartDashboard.putData(field);
		SmartDashboard.putBoolean("Trajectory Active", trajectorySignal.isPresent());
		SmartDashboard.putNumber("trajectory Distance", driveSignal.getTranslation().length);
		SmartDashboard.putNumber("Chassis speeds",
				Math.hypot(chassisSpeeds.vxMetersPerSecond, chassisSpeeds.vyMetersPerSecond));
	}

	public static final DrivetrainFeedforwardConstants FEEDFORWARD_CONSTANTS = new DrivetrainFeedforwardConstants(
			0.042746,
			0.0032181,
			0.30764);

	public static final TrajectoryConstraint[] TRAJECTORY_CONSTRAINTS = {
			new FeedforwardConstraint(11.0, FEEDFORWARD_CONSTANTS.getVelocityConstant(),
					FEEDFORWARD_CONSTANTS.getAccelerationConstant(), false),
			new MaxAccelerationConstraint(12.5 * 12.0),
			new CentripetalAccelerationConstraint(15 * 12.0)
	};
	private final HolonomicMotionProfiledTrajectoryFollower follower = new HolonomicMotionProfiledTrajectoryFollower(
			new PIDConstants(0.4, 0.0, 0.025),
			new PIDConstants(5.0, 0.0, 0.0),
			new HolonomicFeedforward(FEEDFORWARD_CONSTANTS));

	public HolonomicMotionProfiledTrajectoryFollower getFollower() {
		return follower;
	}
}