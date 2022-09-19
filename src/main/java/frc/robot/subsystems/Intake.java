package frc.robot.subsystems;

import static frc.robot.Constants.IntakeConstants.kIntakeSpeed;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
	private final TalonSRX motor = new TalonSRX(30);
	private IntakeState currentState = IntakeState.IDLE;

	public enum IntakeState {
		INTAKE,
		OUTTAKE,
		IDLE;
	}

	public Intake() {
		motor.setNeutralMode(NeutralMode.Coast);
		motor.setInverted(true);
	}

	@Override
	public void periodic() {
		applyState(currentState);
	}

	private void applyState(IntakeState state) {
		switch (state) {
			case INTAKE:
				motor.set(ControlMode.PercentOutput, kIntakeSpeed);
				break;

			case OUTTAKE:
				motor.set(ControlMode.PercentOutput, -kIntakeSpeed);
				break;

			case IDLE:
				motor.neutralOutput();
				break;
		}
	}

	public void setState(IntakeState state) {
		currentState = state;
	}

}