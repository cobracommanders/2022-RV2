/*package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.CentererConstants.*;

public class Centerer extends SubsystemBase {
	private final CANSparkMax motor = new CANSparkMax(kCentererID, MotorType.kBrushless);

	private CentererState currentState = CentererState.IDLE;

	public enum CentererState {
		CENTER,
		OUTTAKE,
		IDLE;
	}

	public Centerer() {
		motor.setIdleMode(IdleMode.kCoast);
		motor.setInverted(false);
	}

	@Override
	public void periodic() {
		applyState(currentState);
	}

	private void applyState(CentererState state) {
		switch (state) {
			case CENTER:
				motor.set(kCentererSpeed);
				break;

			case OUTTAKE:
				motor.set(-kCentererSpeed);
				break;

			case IDLE:
				motor.stopMotor();
				break;
		}
	}

	public void setState(CentererState state) {
		currentState = state;
	}
}
*/
