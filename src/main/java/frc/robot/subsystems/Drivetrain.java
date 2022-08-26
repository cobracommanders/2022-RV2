package frc.robot.subsystems;

//import com.ctre.phoenix.sensors.PigeonIMU;
import com.swervedrivespecialties.swervelib.Mk4SwerveModuleHelper;
import com.swervedrivespecialties.swervelib.SdsModuleConfigurations;
import com.swervedrivespecialties.swervelib.SwerveModule;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.ADIS16448_IMU;

import static frc.robot.Constants.DrivetrainConstants.*;

public class Drivetrain extends SubsystemBase {
	/**
	 * The maximum voltage that will be delivered to the drive motors.
	 * <p>
	 * This can be reduced to cap the robot's maximum speed. Typically, this is
	 * useful during initial testing of the robot.
	 */
	public static final double kMaxVoltage = 12.0;
	// TODO Measure the drivetrain's maximum velocity or calculate the theoretical.
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
	 * The maximum angular velocity of the robot in radians per second.
	 * <p>
	 * This is a measure of how fast the robot can rotate in place.
	 */
	// Here we calculate the theoretical maximum angular velocity. You can also
	// replace this with a measured amount.
	public static final double MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND = kMaxVelocityMetersPerSecond /
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

	// TODO Check that rotating the robot counter-clockwise causes the angle reading
	// to increase
	//private final PigeonIMU pigeon = new PigeonIMU(kPigeonID);
	private final ADIS16448_IMU IMU = new ADIS16448_IMU();

	// These are our modules. We initialize them in the constructor.
	private final SwerveModule frontLeftModule;
	private final SwerveModule frontRightModule;
	private final SwerveModule backLeftModule;
	private final SwerveModule backRightModule;

	private ChassisSpeeds chassisSpeeds = new ChassisSpeeds(0.0, 0.0, 0.0);

	public Drivetrain() {
		ShuffleboardTab tab = Shuffleboard.getTab("Drivetrain");
		frontLeftModule = Mk4SwerveModuleHelper.createFalcon500(
				// This parameter is optional, but will allow you to see the current state of
				// the module on the dashboard.
				tab.getLayout("Front Left Module", BuiltInLayouts.kList)
						.withSize(2, 4)
						.withPosition(0, 0),
				Mk4SwerveModuleHelper.GearRatio.L2,
				kFrontLeftModuleDriveID,
				kFrontLeftModuleSteerID,
				kFrontLeftModuleEncoderID,
				// This is how much the steer encoder is offset from true zero (In our case,
				// zero is facing straight forward)
				kFrontLeftModuleOffset);

		frontRightModule = Mk4SwerveModuleHelper.createFalcon500(
				tab.getLayout("Front Right Module", BuiltInLayouts.kList)
						.withSize(2, 4)
						.withPosition(2, 0),
				Mk4SwerveModuleHelper.GearRatio.L2,
				kFrontRightModuleDriveID,
				kFrontRightModuleSteerID,
				kFrontRightModuleEncoderID,
				kFrontRightModuleOffset);

		backLeftModule = Mk4SwerveModuleHelper.createFalcon500(
				tab.getLayout("Back Left Module", BuiltInLayouts.kList)
						.withSize(2, 4)
						.withPosition(4, 0),
				Mk4SwerveModuleHelper.GearRatio.L2,
				kBackLeftModuleDriveID,
				kBackLeftModuleSteerID,
				kBackLeftModuleEncoderID,
				kBackLeftModuleOffset);

		backRightModule = Mk4SwerveModuleHelper.createFalcon500(
				tab.getLayout("Back Right Module", BuiltInLayouts.kList)
						.withSize(2, 4)
						.withPosition(6, 0),
				Mk4SwerveModuleHelper.GearRatio.L2,
				kBackRightModuleDriveID,
				kBackRightModuleSteerID,
				kBackRightModuleEncoderID,
				kBackRightModuleOffset);
	}

	public void zeroGyro() {
		//pigeon.setFusedHeading(0.0);
		IMU.reset();
	}

	public Rotation2d getGyro() {
		//return Rotation2d.fromDegrees(pigeon.getFusedHeading());
		//return Rotation2d.fromDegrees(IMU.getGyroAngleZ());
		return Rotation2d.fromDegrees(0);
	}

	public void drive(ChassisSpeeds chassisSpeeds) {
		this.chassisSpeeds = chassisSpeeds;
	}

	@Override
	public void periodic() {
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
	}
}