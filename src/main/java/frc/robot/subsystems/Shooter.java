package frc.robot.subsystems;

import static frc.robot.Constants.ShooterConstants.kBackShooterID;
import static frc.robot.Constants.ShooterConstants.kFrontShooterID;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
	private final TalonFX leftMotor = new TalonFX(kFrontShooterID);
	private final TalonFX rightMotor = new TalonFX(kBackShooterID);
	private final PIDController PID = new PIDController(0.00006, 0, 0);

	private double setpoint = 0;

	public enum ShooterSetting {
		TARMAC(13750, 1),
		FENDER(13250, 0.1),
		LAUNCHPAD(16500, 1.4),
		REVERSE(-3000, 0),
		IDLE(0, 0);

		public double RPM;
		public double angle;

		private ShooterSetting(double RPM, double angle) {
			this.RPM = RPM;
			this.angle = angle;
		}
	}

	public Shooter() {
		leftMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 100);
		leftMotor.setNeutralMode(NeutralMode.Coast);
		leftMotor.configOpenloopRamp(1);

		rightMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 100);
		rightMotor.setNeutralMode(NeutralMode.Coast);
		rightMotor.configOpenloopRamp(1);

		leftMotor.setInverted(false);
		rightMotor.follow(leftMotor, FollowerType.PercentOutput);

		// SmartDashboard.putNumber("Shooter RPM", 0);
	}

	@Override
	public void periodic() {
		if (setpoint != 0) {
			leftMotor.set(ControlMode.PercentOutput, PID.calculate(leftMotor.getSelectedSensorVelocity(), setpoint));
		} else {
			leftMotor.neutralOutput();
		}

		// SmartDashboard.putNumber("Actual RPM", leftMotor.getSelectedSensorVelocity()
		// / 2048 / 6);
	}

	public boolean atSetpoint(double range) {
		return Math.abs(PID.getVelocityError()) < range && (Math.signum(setpoint) != -1 && Math.signum(setpoint) != 0);
	}

	public void setSpeed(double speed) {
		setpoint = speed;
	}
}