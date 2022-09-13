package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.HopperConstants.*;

public class Hopper extends SubsystemBase {
	private final TalonSRX hopperFront = new TalonSRX(kFrontHopperID);
	private final TalonSRX hopperBack = new TalonSRX(kBackHopperID);
	private final ColorSensorV3 colorSensor = new ColorSensorV3(Port.kOnboard);
	private final DigitalInput upperSensor = new DigitalInput(kUpperSensorDIO);
	private final DigitalInput lowerSensor = new DigitalInput(kLowerSensorDIO);

	private HopperState currentState = HopperState.IDLE;
	private Alliance alliance = Alliance.Invalid;
	public static int currentBallCount;

	private double speed;

	public enum HopperState {
		LOAD,
		OUTTAKE,
		IDLE;
	}

	public enum CargoColor {
		CORRECT,
		INCORRECT,
		NONE
	}

	public Hopper() {
		hopperBack.setInverted(false);
		hopperFront.setInverted(true);
		alliance = DriverStation.getAlliance();
	}

	@Override
	public void periodic() {
		applyState(currentState);

		SmartDashboard.putNumber("proximity", colorSensor.getProximity());
		SmartDashboard.putBoolean("upper sensor", getUpperSensor());
		SmartDashboard.putBoolean("lower sensor", getLowerSensor());
		SmartDashboard.putNumber("cargo count", getCargoCount());
		SmartDashboard.putBoolean("correct color", getCargoColor() == CargoColor.CORRECT);
		SmartDashboard.putString("gt4rbe", getCargoColor().toString());
		if (getCurrentCommand() != null)
			SmartDashboard.putString("command", getCurrentCommand().getName());
	}

	public void setState(HopperState state, double speed) {
		currentState = state;
		this.speed = speed;
	}

	public CargoColor getCargoColor() {

		if (colorSensor.getBlue() > kColorSensorLeniency && alliance == Alliance.Blue
				|| ((colorSensor.getRed() > kColorSensorLeniency && alliance == Alliance.Red)))
			return CargoColor.CORRECT;

		if (!((colorSensor.getBlue() > kColorSensorLeniency)
				|| (colorSensor.getRed() > kColorSensorLeniency)))
			return CargoColor.NONE;

		return CargoColor.INCORRECT;
	}

	public boolean getUpperSensor() {
		return !upperSensor.get();
	}

	public boolean getLowerSensor() {
		return !lowerSensor.get();
	}

	public void addCargoCount() {
		currentBallCount++;
	}

	public void removeCargoCount() {
		currentBallCount -= 1;
	}

	public int getCargoCount() {
		return currentBallCount;
	}

	private void applyState(HopperState state) {
		switch (state) {
			// Set both hopper motors to pull the ball upwards
			case LOAD:
				hopperFront.set(ControlMode.PercentOutput, speed);
				hopperBack.set(ControlMode.PercentOutput, speed);
				break;

			// Set the front motor upwards, and the back motor downwards to spin the top
			// ball in place and eject any below it
			case OUTTAKE:
				hopperFront.set(ControlMode.PercentOutput, speed);
				hopperBack.set(ControlMode.PercentOutput, -speed);
				break;

			// Idle state, pause both motors
			case IDLE:
				hopperFront.set(ControlMode.PercentOutput, speed);
				hopperBack.set(ControlMode.PercentOutput, speed);
				break;
		}
	}
}
