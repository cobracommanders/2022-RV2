package org.team498.lib.drivers;

import static org.team498.C2022.Constants.DrivetrainConstants.kDriveWheelCircumference;
import static org.team498.C2022.Constants.DrivetrainConstants.kMK4IDriveReductionL2;
import static org.team498.C2022.Constants.DrivetrainConstants.kMK4ISteerReductionL2;
import static org.team498.C2022.Constants.DrivetrainConstants.*;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;

import org.team498.lib.util.Falcon500Conversions;
import org.team498.lib.util.Unit;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SwerveModule extends SubsystemBase {
	// Motors
	private final WPI_TalonFX driveMotor;
	private final WPI_TalonFX steerMotor;

	// CANCoder
	private final CANCoder encoder;

	private final double angleOffset;

	private double lastAngle;

	public SwerveModule(int driveMotorID, int steerMotorID, int CANCoderID, double angleOffset) {
		// Creates the motors with the given CAN IDs
		driveMotor = new WPI_TalonFX(driveMotorID);
		steerMotor = new WPI_TalonFX(steerMotorID);

		driveMotor.configFactoryDefault();
		steerMotor.configFactoryDefault();

		driveMotor.setNeutralMode(NeutralMode.Brake);
		driveMotor.configOpenloopRamp(1);

		steerMotor.setNeutralMode(NeutralMode.Brake);
		steerMotor.configOpenloopRamp(1);

		driveMotor.setSelectedSensorPosition(0);
		steerMotor.setSelectedSensorPosition(0);

		steerMotor.setSensorPhase(true);

		driveMotor.config_kP(0, 0.025);
		driveMotor.config_kI(0, 0.0);
		driveMotor.config_kD(0, 0.5);

		steerMotor.config_kP(0, 0.2);
		steerMotor.config_kI(0, 0.0);
		steerMotor.config_kD(0, 0.1);

		// steerMotor.setSensorPhase(true);

		// Create the CANCoder with the given ID
		encoder = new CANCoder(CANCoderID);

		// Set inversion of the motors
		driveMotor.setInverted(true);
		steerMotor.setInverted(true);

		// Sets the encoder to boot to the absoulte position instead of 0
		encoder.configSensorInitializationStrategy(SensorInitializationStrategy.BootToAbsolutePosition);

		// Set the encoder to return values from 0 to 360 instead of -180 to +180
		encoder.configAbsoluteSensorRange(AbsoluteSensorRange.Unsigned_0_to_360);

		// Drive current limit: 35
		// Steer current limit: 20
		// driveMotor.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(true, 35, 40, .5));
		// steerMotor.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(true, 20, 40, .5));

		this.angleOffset = angleOffset;
		this.lastAngle = getState().angle.getDegrees();

		matchEncoders();
	}

	public void matchEncoders() {
		steerMotor.setSelectedSensorPosition(Falcon500Conversions
				.degreesToFalcon(encoder.getAbsolutePosition() - angleOffset, kMK4ISteerReductionL2));
	}

	// Return the position of the wheel based on the integrated motor encoder
	public double getSteerEncoder() {
		return Falcon500Conversions
				.falconToDegrees(steerMotor.getSelectedSensorPosition(), kMK4ISteerReductionL2) + angleOffset;
	}

	public double currentSpeed = 0;
	public double currentAngle = 0;

	public boolean forcedAngle = false;

	// Sets the motors of the swerve module to a provided state
	public void setState(SwerveModuleState state, boolean force) {
		this.forcedAngle = force;

		SwerveModuleState newState = optimize(state, Rotation2d.fromDegrees(getSteerEncoder()));
		this.currentSpeed = newState.speedMetersPerSecond;
		this.currentAngle = newState.angle.getDegrees();
	}

	@Override
	public void periodic() {
		double velocity = Falcon500Conversions.MPSToFalcon(
				currentSpeed,
				Unit.inchesToMeters(kDriveWheelDiameter),
				kMK4IDriveReductionL2);
		driveMotor.set(ControlMode.Velocity, velocity);

		double angle = (Math.abs(velocity) <= kMaxVelocityMetersPerSecond * 0.01) && !forcedAngle ? lastAngle : currentAngle;
		steerMotor.set(ControlMode.Position, Falcon500Conversions
				.degreesToFalcon(angle - angleOffset, kMK4ISteerReductionL2));

		lastAngle = getState().angle.getDegrees();

	}

	// Get the velocity of the wheel in meters per second
	private double getVelocityMPS() {
		// Convert the value returned by the sensor (rotations per 100ms) to rotations
		// per second
		return Falcon500Conversions
				.falconToMPS(driveMotor.getSelectedSensorVelocity(), Unit.inchesToMeters(kDriveWheelCircumference), kMK4IDriveReductionL2);
	}

	// Get the current state of the swerve module
	public SwerveModuleState getState() {
		return new SwerveModuleState(
				// Velocity of the wheel
				getVelocityMPS(),
				// The value of the steering encoder
				Rotation2d.fromDegrees(getSteerEncoder()));
	}

	// Custom optimize method by team 364
	private SwerveModuleState optimize(SwerveModuleState desiredState, Rotation2d currentAngle) {
		double targetAngle = placeInAppropriate0To360Scope(currentAngle.getDegrees(), desiredState.angle.getDegrees());

		double targetSpeed = desiredState.speedMetersPerSecond;

		double delta = targetAngle - currentAngle.getDegrees();

		if (Math.abs(delta) > 90) {
			targetSpeed = -targetSpeed;
			targetAngle = delta > 90 ? (targetAngle -= 180) : (targetAngle += 180);
		}
		return new SwerveModuleState(targetSpeed, Rotation2d.fromDegrees(targetAngle));
	}

	private double placeInAppropriate0To360Scope(double scopeReference, double newAngle) {
		double lowerBound;
		double upperBound;
		double lowerOffset = scopeReference % 360;
		if (lowerOffset >= 0) {
			lowerBound = scopeReference - lowerOffset;
			upperBound = scopeReference + (360 - lowerOffset);
		} else {
			upperBound = scopeReference - lowerOffset;
			lowerBound = scopeReference - (360 + lowerOffset);
		}
		while (newAngle < lowerBound) {
			newAngle += 360;
		}
		while (newAngle > upperBound) {
			newAngle -= 360;
		}
		if (newAngle - scopeReference > 180) {
			newAngle -= 360;
		} else if (newAngle - scopeReference < -180) {
			newAngle += 360;
		}
		return newAngle;
	}

}