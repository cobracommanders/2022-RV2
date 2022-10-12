package frc.robot.subsystems;

import static frc.robot.Constants.IntakeConstants.kIntakeID;
import static frc.robot.Constants.IntakeConstants.kIntakeSpeed;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.util.Counter;

public class Intake extends SubsystemBase {
	private final TalonSRX motor = new TalonSRX(kIntakeID);
	private IntakeState currentState = IntakeState.IDLE;
	private Counter counter = new Counter("Intake");

	public enum IntakeState {
		// TODO Variable intake speed based on robot speed
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

		counter.addToList(counter);
	}

	public Counter getCounter() {
		return counter;
	}

	@Override
	public void periodic() {
		motor.set(ControlMode.PercentOutput, currentState.speed);
	}

	public void setState(IntakeState state) {
		currentState = state;
		Robot.logger.log("Intake state set to " + state.toString());
	}

}