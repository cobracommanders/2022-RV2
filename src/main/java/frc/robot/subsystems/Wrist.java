package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.WristConstants.*;

public class Wrist extends SubsystemBase {
	private final CANSparkMax leftMotor = new CANSparkMax(kLeftWristID, MotorType.kBrushless);
	private final CANSparkMax rightMotor = new CANSparkMax(kRightWristID, MotorType.kBrushless);

	private final DigitalInput lowerLimit = new DigitalInput(kLowerWristLimitID);
	private final DigitalInput upperLimit = new DigitalInput(kUpperWristLimitID);

	private WristState currentState = WristState.IN;

	public enum WristState {
		IN,
		OUT;
	}

	public Wrist() {
		leftMotor.setIdleMode(IdleMode.kBrake);
		rightMotor.setIdleMode(IdleMode.kBrake);
		leftMotor.setInverted(false);
		rightMotor.follow(leftMotor, true);
	}

	@Override
	public void periodic() {
		applyState(currentState);
	}

	private void applyState(WristState state) {
		// If the intake is set to in, check the upper limit switch before setting it to
		// move
		if (state == WristState.IN && !upperLimit.get()) {
			leftMotor.setVoltage(kWristSpeed * 12);
			return;
		}
		// If the intake is set to out, check the lower limit switch and verify that the
		// hopper has less than 2 balls
		if (state == WristState.OUT && !(lowerLimit.get() || Hopper.currentBallCount == 2)) {
			leftMotor.setVoltage(-kWristSpeed * 12);
			return;
		}
		
		// If none of the above are true, stop the motor
		leftMotor.stopMotor();
	}

	public void setState(WristState state) {
		currentState = state;
	}
}
