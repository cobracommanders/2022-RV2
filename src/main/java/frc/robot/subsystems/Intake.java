package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.IntakeConstants.*;

public class Intake extends SubsystemBase {
	private final CANSparkMax leftMotor = new CANSparkMax(kLeftIntakeID, MotorType.kBrushless);
	private final CANSparkMax rightMotor = new CANSparkMax(kRightIntakeID, MotorType.kBrushless);

	private IntakeState currentState = IntakeState.IDLE;

	public enum IntakeState {
		INTAKE,
		OUTTAKE,
		IDLE;
	}

	public Intake() {
		leftMotor.setIdleMode(IdleMode.kCoast);
		rightMotor.setIdleMode(IdleMode.kCoast);
		leftMotor.setInverted(false);
		rightMotor.follow(leftMotor, true);
	}

	@Override
	public void periodic() {
		applyState(currentState);
	}

	private void applyState(IntakeState state) {
		switch (state) {
			case INTAKE:
				leftMotor.set(kIntakeSpeed);
				break;

			case OUTTAKE:
				leftMotor.set(-kIntakeSpeed);
				break;

			case IDLE:
				leftMotor.stopMotor();
				break;
		}
	}

	public void setState(IntakeState state) {
		currentState = state;
	}
}
