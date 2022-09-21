package frc.robot.subsystems;

import static frc.robot.Constants.CentererConstants.kCentererID;
import static frc.robot.Constants.CentererConstants.kCentererSpeed;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Centerer extends SubsystemBase {
	private final CANSparkMax motor = new CANSparkMax(kCentererID, MotorType.kBrushless);

	private CentererState currentState = CentererState.IDLE;

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
	}

	@Override
	public void periodic() {
		motor.set(currentState.speed);
	}

	public void setState(CentererState state) {
		currentState = state;
	}
}
