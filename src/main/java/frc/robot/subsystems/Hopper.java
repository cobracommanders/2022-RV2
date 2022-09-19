package frc.robot.subsystems;

import static frc.robot.Constants.HopperConstants.kBackHopperID;
import static frc.robot.Constants.HopperConstants.kColorSensorLeniency;
import static frc.robot.Constants.HopperConstants.kFrontHopperID;
import static frc.robot.Constants.HopperConstants.kLowerSensorDIO;
import static frc.robot.Constants.HopperConstants.kUpperSensorDIO;

import java.util.LinkedList;
import java.util.Queue;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.hopper.IntakeHopper;
import frc.robot.commands.hopper.SaveCargoHigh;
import frc.robot.commands.hopper.EjectCargo;

public class Hopper extends SubsystemBase {
	private final TalonSRX hopperFront = new TalonSRX(kFrontHopperID);
	private final TalonSRX hopperBack = new TalonSRX(kBackHopperID);
	private final ColorSensorV3 colorSensor = new ColorSensorV3(Port.kOnboard);
	private final DigitalInput upperSensor = new DigitalInput(kUpperSensorDIO);
	private final DigitalInput lowerSensor = new DigitalInput(kLowerSensorDIO);

	private HopperSetting currentState = HopperSetting.IDLE;
	private Alliance alliance = Alliance.Invalid;
	private int currentBallCount;

	private double speed;
	private double backSpeed;

	private boolean autoEnabled = false;

	private IntakeHopper intakeCommand;
	// private SetHopper intakeCommand;

	// Different
	public enum HopperSetting {
		LOAD,
		OUTTAKE,
		IDLE;
	}

	// The state of any cargo in the hopper
	public enum HopperCargoState {
		CORRECT,
		INCORRECT,
		EMPTY
	}

	public Hopper() {
		hopperBack.setInverted(false);
		hopperFront.setInverted(true);
		intakeCommand = new IntakeHopper(this);
		// intakeCommand = new SetHopper(this, HopperSetting.OUTTAKE, 0.19);
		alliance = DriverStation.getAlliance();
		autoEnabled = true;
		hopperFront.setNeutralMode(NeutralMode.Brake);
		hopperBack.setNeutralMode(NeutralMode.Brake);
	}

	private boolean newCargo = true;
	private Queue<HopperCargoState> queue = new LinkedList<>();

	public void setAutoControl(boolean enabled) {
		autoEnabled = enabled;
	}

	public boolean getAutoEnabled() {
		return autoEnabled;
	}

	@Override
	public void periodic() {
		applyState(currentState);

		if (getUpperSensor() && getCargoState() == HopperCargoState.CORRECT && autoEnabled) {
			intakeCommand.cancel();
		}

		if (getCargoState() != HopperCargoState.EMPTY && newCargo && autoEnabled) {
			intakeCommand.cancel();
			newCargo = false;
			queue.add(getCargoState());
		}

		newCargo = getCargoState() == HopperCargoState.EMPTY;

		if (getCurrentCommand() == null) {
			HopperCargoState operation = getQueuedOperation();
			if (operation == HopperCargoState.CORRECT && !getUpperSensor()) {
				new SaveCargoHigh(this).schedule(false);
			} else if (operation == HopperCargoState.INCORRECT) {
				new EjectCargo(this).schedule(false);
			}
		}

		// System.out.println(queue);

		// SmartDashboard.putBoolean("newCargo", newCargo);
		SmartDashboard.putBoolean("upper sensor", getUpperSensor());
		// SmartDashboard.putBoolean("lower sensor", getLowerSensor());
		// SmartDashboard.putNumber("cargo count", getCargoCount());
		SmartDashboard.putBoolean("hopper enabled", getAutoEnabled());
		SmartDashboard.putBoolean("correct color", getCargoState() == HopperCargoState.CORRECT);
		SmartDashboard.putString("state", getCargoState().toString());
		SmartDashboard.putData(this);
		SmartDashboard.putBoolean("hopper enabled", autoEnabled);

	}

	public IntakeHopper getIntakeCommand() {
		return intakeCommand;
	}

	public HopperCargoState getQueuedOperation() {
		return queue.peek() == null
				? HopperCargoState.EMPTY
				: queue.poll();

	}

	public void setState(HopperSetting state, double speed, double backSpeed) {
		currentState = state;
		this.speed = speed;
		this.backSpeed = backSpeed;
	}

	public HopperCargoState getCargoState() {
		// First check to see if there is a match for blue or red cargo. If neither of
		// these are true, return EMPTY
		if (!((colorSensor.getBlue() > kColorSensorLeniency)
				|| (colorSensor.getRed() > kColorSensorLeniency)))
			return HopperCargoState.EMPTY;

		// Next check for a match between the alliance color and the sensor reading. If
		// a match is found, return CORRECT
		if (colorSensor.getBlue() > kColorSensorLeniency && alliance == Alliance.Blue
				|| ((colorSensor.getRed() > kColorSensorLeniency && alliance == Alliance.Red)))
			return HopperCargoState.CORRECT;

		// If both of these failed, there must be a ball in the hopper of the wrong
		// color, so INCORRECT is returned
		return HopperCargoState.INCORRECT;
	}

	public boolean getUpperSensor() {
		return !upperSensor.get();
	}

	public void setCargoCount(int newValue) {
		currentBallCount = newValue;
	}

	public boolean getLowerSensor() {
		return !lowerSensor.get();
	}

	public void addToCargoCount() {
		if (currentBallCount < 2)
			currentBallCount++;
	}

	public void subtractFromCargoCount() {
		if (currentBallCount > 0)
			currentBallCount -= 1;
	}

	public int getCargoCount() {
		return currentBallCount;
	}

	private void applyState(HopperSetting state) {
		switch (state) {
			// Set both hopper motors to pull the ball upwards
			case LOAD:
				hopperFront.set(ControlMode.PercentOutput, speed);
				hopperBack.set(ControlMode.PercentOutput, backSpeed);
				break;

			// Set the front motor upwards, and the back motor downwards to spin the top
			// ball in place and eject any below it
			case OUTTAKE:
				hopperFront.set(ControlMode.PercentOutput, speed);
				hopperBack.set(ControlMode.PercentOutput, -backSpeed);
				break;

			// Idle state, pause both motors
			case IDLE:
				hopperFront.neutralOutput();
				hopperBack.neutralOutput();
				break;
		}
	}
}
