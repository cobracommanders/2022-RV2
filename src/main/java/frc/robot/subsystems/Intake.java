package frc.robot.subsystems;

import static frc.robot.Constants.IntakeConstants.kIntakeID;
import static frc.robot.Constants.IntakeConstants.kIntakeSpeed;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
	private final TalonSRX motor = new TalonSRX(kIntakeID);
	private IntakeState currentState = IntakeState.IDLE;

	public enum IntakeState {
		INTAKE(kIntakeSpeed),
		OUTTAKE(-kIntakeSpeed),
		IDLE(0);

		private double speed;

		private IntakeState(double speed) {
			this.speed = speed;
		}
	}

	public Intake() {
		motor.setNeutralMode(NeutralMode.Coast);
		motor.setInverted(true);
	}

	@Override
	public void periodic() {
		motor.set(ControlMode.PercentOutput, currentState.speed);
	}

	public void setState(IntakeState state) {
		currentState = state;
	}

}