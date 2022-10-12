package frc.robot.subsystems;

import static frc.robot.Constants.CentererConstants.kCentererID;
import static frc.robot.Constants.CentererConstants.kCentererSpeed;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.util.Counter;

public class Centerer extends SubsystemBase {
	private final CANSparkMax motor = new CANSparkMax(kCentererID, MotorType.kBrushless);
	private CentererState currentState = CentererState.IDLE;
	private Counter counter = new Counter("Centerer");

	public enum CentererState {
		CENTER(kCentererSpeed),
		OUTTAKE(-kCentererSpeed),
		IDLE(0);

		private double speed;

		private CentererState(double speed) {
			this.speed = speed;
		}
		
	}

	public Centerer() {
		motor.setIdleMode(IdleMode.kCoast);
		motor.setInverted(true);

		counter.addToList(counter);
	}

	public Counter getCounter() {
		return counter;
	}

	@Override
	public void periodic() {
		motor.set(currentState.speed);
	}

	public void setState(CentererState state) {
		currentState = state;
		Robot.logger.log("Centerer state set to " + state.toString());
	}
}
