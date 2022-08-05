package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.HopperConstants.*;

public class Hopper extends SubsystemBase {
	private final TalonSRX hopperFront = new TalonSRX(kFrontHopperID);
	private final TalonSRX hopperBack = new TalonSRX(kBackHopperID);
	private final ColorSensorV3 colorSensor = new ColorSensorV3(null);

	private HopperState currentState = HopperState.IDLE;
	private Alliance alliance = Alliance.Invalid;

	//TODO: implement with limit switches
	public static int currentBallCount;

	public enum HopperState {
		LOAD,
		OUTTAKE,
		PLACE,
		IDLE;
	}

	public Hopper() {
		hopperBack.setInverted(false);
		hopperFront.setInverted(false);
		alliance = DriverStation.getAlliance();
	}

	@Override
	public void periodic() {
		applyState(currentState);
	}

	public void setState(HopperState newState) {
		currentState = newState;
	}

	public boolean isCargoInHopperBottom() {
		return (colorSensor.getProximity() / 2047) > kProximitySensorLeniency;
	}

	public void isCargoInHopperTop() {
		return;
	}

	public boolean isCargoCorrectColor() {
		double r = (double) colorSensor.getRed();
		double g = (double) colorSensor.getGreen();
		double b = (double) colorSensor.getBlue();
		double mag = r + g + b;
		double blue = b / mag;
		double red = r / mag;

		return ((blue > kColorSensorLeniency && alliance == Alliance.Blue)
				|| ((red > kColorSensorLeniency && alliance == Alliance.Red)));
	}

	private void applyState(HopperState state) {
		switch (state) {
			// Set both hopper motors to pull the ball upwards
			case LOAD:
				hopperFront.set(ControlMode.PercentOutput, 1);
				hopperBack.set(ControlMode.PercentOutput, 1);
				break;

			// Set the front motor upwards, and the back motor downwards to spin the top
			// ball in place and eject any below it
			case OUTTAKE:
				hopperFront.set(ControlMode.PercentOutput, 1);
				hopperBack.set(ControlMode.PercentOutput, -1);
				break;

			// Same as above, but at a lower speed in the case where it is better to drop
			// the ball slowly
			case PLACE:
				hopperFront.set(ControlMode.PercentOutput, 0.25);
				hopperBack.set(ControlMode.PercentOutput, -0.25);
				break;

			// Idle state, pause both motors
			case IDLE:
				hopperFront.set(ControlMode.PercentOutput, 0);
				hopperBack.set(ControlMode.PercentOutput, 0);
				break;
		}
	}
}