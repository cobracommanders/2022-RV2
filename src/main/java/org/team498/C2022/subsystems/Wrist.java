package org.team498.C2022.subsystems;

import static org.team498.C2022.Constants.WristConstants.kLeftWristID;
import static org.team498.C2022.Constants.WristConstants.kPositionIn;
import static org.team498.C2022.Constants.WristConstants.kPositionOut;
import static org.team498.C2022.Constants.WristConstants.kRightWristID;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Wrist extends SubsystemBase {
	private final CANSparkMax leftMotor = new CANSparkMax(kLeftWristID, MotorType.kBrushless);
	private final CANSparkMax rightMotor = new CANSparkMax(kRightWristID, MotorType.kBrushless);
	private final RelativeEncoder encoder = leftMotor.getEncoder();

	private WristState currentState = WristState.IN;

	public enum WristState {
		IN(kPositionIn, 0.14, 0.05),
		OUT(kPositionOut, 0.14, 1);

		private double position;
		private double kP;
		private double deadzone;

		private WristState(double position, double kp, double deadzone) {
			this.position = position;
			this.kP = kp;
			this.deadzone = deadzone;
		}
	}

	public Wrist() {
		leftMotor.setIdleMode(IdleMode.kBrake);
		rightMotor.setIdleMode(IdleMode.kBrake);
		leftMotor.setInverted(false);
		leftMotor.setOpenLoopRampRate(0.25);
		rightMotor.follow(leftMotor, true);
		encoder.setPosition(0);
	}

	public void resetEncoders() {
		encoder.setPosition(0);
	}

	@Override
	public void periodic() {
		leftMotor.set(calculate(currentState.position, encoder.getPosition()));
		// SmartDashboard.putNumber("wrist encoder", encoder.getPosition());
	}

	public void setState(WristState state) {
		currentState = state;
	}

	public double calculate(double setpoint, double currentPosition) {
		double difference = setpoint - currentPosition;
		if (Math.abs(difference) < currentState.deadzone)
			return 0;
		return difference * currentState.kP;
	}
}