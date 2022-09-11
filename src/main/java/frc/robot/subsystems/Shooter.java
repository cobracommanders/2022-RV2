package frc.robot.subsystems;

import static frc.robot.Constants.ShooterConstants.kBackShooterID;
import static frc.robot.Constants.ShooterConstants.kFrontShooterID;
import static frc.robot.Constants.ShooterConstants.kShooterSpeed;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
	private final TalonFX leftMotor = new TalonFX(kFrontShooterID);
	private final TalonFX rightMotor = new TalonFX(kBackShooterID);

	private ShooterState currentState = ShooterState.IDLE;

	public enum ShooterState {
		SHOOT,
		IDLE;
	}

	public Shooter() {
		leftMotor.setInverted(false);
		rightMotor.follow(leftMotor, FollowerType.PercentOutput);
	}

	@Override
	public void periodic() {
		applyState(currentState);
	}

	private void applyState(ShooterState state) {
		switch (state) {
			case SHOOT:
				leftMotor.set(ControlMode.PercentOutput, kShooterSpeed);
				break;

			case IDLE:
				leftMotor.set(ControlMode.PercentOutput, 0);
				break;
		}
	}

	public void setState(ShooterState state) {
		currentState = state;
	}
}