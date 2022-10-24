package org.team498.C2022.subsystems;

import static org.team498.C2022.Constants.ShooterConstants.kBackShooterID;
import static org.team498.C2022.Constants.ShooterConstants.kFrontShooterID;
import static org.team498.C2022.ShooterTable.shooterRPMTable;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import org.team498.C2022.util.Limelight;
import org.team498.C2022.util.LinearInterpolator;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
	private final TalonFX leftMotor = new TalonFX(kFrontShooterID);
	private final TalonFX rightMotor = new TalonFX(kBackShooterID);
	private final PIDController PID = new PIDController(0.00006, 0, 0);
	private final LinearInterpolator interpolator = new LinearInterpolator(shooterRPMTable);
	public final Limelight limelight;

	private ShooterSetting currentSetting = ShooterSetting.IDLE;

	public enum ShooterSetting {
		FENDER(2000, 0.1),
		// FENDER(500, 0.5), // Demo mode, this barely spits out the ball
		TARMAC(6800, 0.4),
		LAUNCHPAD(8400, 1.4),
		REVERSE(-3000, 0),
		IDLE(0, 0);

		public double RPM;
		public double angle;

		private ShooterSetting(double RPM, double angle) {
			this.RPM = RPM;
			this.angle = angle;
		}
	}

	public Shooter(Limelight limelight) {
		leftMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 30);
		leftMotor.setNeutralMode(NeutralMode.Coast);
		leftMotor.configOpenloopRamp(1);
		leftMotor.configVoltageCompSaturation(8);
		leftMotor.enableVoltageCompensation(true);

		leftMotor.config_kP(0, 0.5, 30);
		leftMotor.config_kI(0, 0.0, 30);
		leftMotor.config_kD(0, 0.03, 30);
		leftMotor.config_kF(0, 0.048, 30);

		rightMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 30);
		rightMotor.setNeutralMode(NeutralMode.Coast);
		rightMotor.configOpenloopRamp(1);
		rightMotor.configVoltageCompSaturation(8);
		rightMotor.enableVoltageCompensation(true);

		leftMotor.setInverted(false);
		rightMotor.follow(leftMotor, FollowerType.PercentOutput);

		SmartDashboard.putNumber("Shooter RPM", 0);
		this.limelight = limelight;
	}

	public double getInterpolatedValue() {
		System.out.println(limelight.getDistance());
		return interpolator.getInterpolatedValue(limelight.getDistance());
	}

	private double currentSetpoint;

	public boolean atSetpoint(double range) {
		return Math.abs(currentSetpoint - (leftMotor.getSelectedSensorVelocity() / 2048)) < range
				&& (Math.signum(currentSetpoint) != -1 && Math.signum(currentSetpoint) != 0);
	}

	public void set(double setpoint) {
		this.currentSetpoint = setpoint;
		if (setpoint != 0) {
			leftMotor.set(ControlMode.Velocity, setpoint / 0.29296875);
		} else {
			leftMotor.neutralOutput();
		}

		SmartDashboard.putNumber("Actual RPM", leftMotor.getSelectedSensorVelocity() * 0.29296875);
		SmartDashboard.putNumber("voltage used", leftMotor.getMotorOutputVoltage());
	}
}