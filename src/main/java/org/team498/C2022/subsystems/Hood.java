package org.team498.C2022.subsystems;

import static org.team498.C2022.Constants.HoodConstants.kHoodLimitDIO;
import org.team498.C2022.Tables.ShooterTable;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.team498.lib.drivers.Limelight;
import org.team498.lib.util.LinearInterpolator;

import com.revrobotics.RelativeEncoder;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Hood extends SubsystemBase {
	private final CANSparkMax motor;
	private final RelativeEncoder encoder;
	private final PIDController PID;
	private final DigitalInput limit;
	// private final SparkMaxPIDController PID1;
	private ControlMode currentControlMode = ControlMode.PID;
	private final Limelight limelight;
	private LinearInterpolator interpolator = new LinearInterpolator(ShooterTable.hoodAngleTable);

	public Hood(Limelight limelight) {
		motor = new CANSparkMax(36, MotorType.kBrushless);
		motor.restoreFactoryDefaults();
		motor.setInverted(true);
		encoder = motor.getEncoder();
		motor.setSmartCurrentLimit(20);
		encoder.setPosition(0);
		PID = new PIDController(0.1, 0, 0.0035); // P = 0.1 I = 0.0 D = 0.004
		limit = new DigitalInput(kHoodLimitDIO);
		motor.setIdleMode(IdleMode.kBrake);
		this.limelight = limelight;

		motor.setSoftLimit(SoftLimitDirection.kForward, 26F);

		motor.enableSoftLimit(SoftLimitDirection.kForward, true);

		// SmartDashboard.putNumber("Hood Angle", 0);
	}

	public enum ControlMode {
		PID,
		MANUAL
	}

	public double getInterpolatedValue() {
		return interpolator.getInterpolatedValue(limelight.getDistance());
	}

	public void setAngle(double angle) {
		// Makes it so that the given angle is a number from 0 (low) to 1 (high)
		PID.setSetpoint(angle * 26);
		// PID1.setReference(angle, ControlType.kPosition);
	}

	public boolean getLimit() {
		return !limit.get();
	}

	public void setMotor(double speed) {
		motor.set(speed);
	}

	public void resetEncoder() {
		encoder.setPosition(0);
	}

	public boolean atSetpoint() {
		return PID.atSetpoint();
	}

	public void setState(ControlMode state) {
		currentControlMode = state;
	}

	@Override
	public void periodic() {
		// POSITIVE VALUES LOWER THE HOOD
		double power = PID.calculate(encoder.getPosition());
		SmartDashboard.putNumber("PID Reading", power);
		if (currentControlMode == ControlMode.PID) {
			if (!(Math.signum(power) == -1 && getLimit())) {
				motor.set(power);
			} else {
				motor.set(0);
			}
		}
		SmartDashboard.putBoolean("Hood powered", !(Math.signum(power) == -1 && getLimit()));
		SmartDashboard.putBoolean("hood limit", getLimit());
		SmartDashboard.putData(this);
		SmartDashboard.putNumber("hood encoder", encoder.getPosition());
		SmartDashboard.putNumber("hood error", PID.getPositionError());
		SmartDashboard.putString("Control mode", currentControlMode.toString());
	}
}
