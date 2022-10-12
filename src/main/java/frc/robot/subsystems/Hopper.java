package frc.robot.subsystems;

import static frc.robot.Constants.HopperConstants.kBackHopperID;
import static frc.robot.Constants.HopperConstants.kColorSensorLeniency;
import static frc.robot.Constants.HopperConstants.kFrontHopperID;
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
import edu.wpi.first.wpilibj2.command.button.Trigger;

/*
While intake button pressed:

	If cargo in front of color sensor:

		Cargo present:
			Stop intakeing hopper command

			If cargo correct color

				Correct color:

					If no ball in the top of the hopper

						No ball in top: Load until the beam break triggers

						Ball in top: Disable intaking

				Incorrect color: Outtake

		No cargo present:

			If operation already running

				Not running: Enable hopper intake mode

				Running: Do nothing	
*/

public class Hopper extends SubsystemBase {
	private final TalonSRX frontMotor = new TalonSRX(kFrontHopperID);
	private final TalonSRX backMotor = new TalonSRX(kBackHopperID);
	private final ColorSensorV3 colorSensor = new ColorSensorV3(Port.kOnboard);
	private final DigitalInput upperSensor = new DigitalInput(kUpperSensorDIO);
	private final DigitalInput lowerSensor = new DigitalInput(8);
	private final Trigger lowerSensorTrigger;

	private HopperSetting currentState = HopperSetting.IDLE;
	private static Alliance alliance = Alliance.Invalid;

	private boolean autoEnabled;

	public enum HopperSetting {
		LOAD(0.25, 0.25),
		INTAKE(0.19, -0.19),
		UNLOAD(-0.25, -0.25),
		HOLD(0.25, -0.25),
		OUTTAKE(0.4, -0.4),
		REVERSE(-0.2, -0.2),
		OUTTAKETOP(-0.4, -0.4),
		MOVEFRONT(-0.19, 0.19),
		IDLE(0, 0),
		TESTONLY(0.4, 0.4);

		private double frontSpeed;
		private double backSpeed;

		private HopperSetting(double frontSpeed, double backSpeed) {
			this.frontSpeed = frontSpeed;
			this.backSpeed = backSpeed;
		}
	}

	// The state of any cargo in the hopper
	public enum HopperCargoState {
		CORRECT,
		INCORRECT,
		EMPTY
	}

	public Hopper() {
		frontMotor.setInverted(true);
		backMotor.setInverted(false);
		frontMotor.setNeutralMode(NeutralMode.Brake);
		backMotor.setNeutralMode(NeutralMode.Brake);
		lowerSensorTrigger = new Trigger(() -> lowerSensor.get());

		alliance = DriverStation.getAlliance();

		autoEnabled = true;
	}

	public Trigger getLowerSensorTrigger() {
		return lowerSensorTrigger;
	}

	public static void updateAlliance() {
		alliance = DriverStation.getAlliance();
	}

	private Queue<HopperCargoState> queue = new LinkedList<>();

	public void setAutoControl(boolean enabled) {
		autoEnabled = enabled;
	}

	public boolean getAutoEnabled() {
		return autoEnabled;
	}

	private boolean newCargo = true;

	public boolean hopperFull() {
		return (getUpperSensor() && getCargoState() == HopperCargoState.CORRECT);
	}

	@Override
	public void periodic() {
		frontMotor.set(ControlMode.PercentOutput, currentState.frontSpeed);
		backMotor.set(ControlMode.PercentOutput, currentState.backSpeed);

		// If it's a new cargo and the hopper recives a cargo
		if ((newCargo && getCargoState() != HopperCargoState.EMPTY) && !hopperFull()) {
			// Mark the cargo as no longer being new
			newCargo = false;
			// Queue the new operation
			queue.add(getCargoState());
		}

		newCargo = getCargoState() == HopperCargoState.EMPTY;
		if (hopperFull())
			queue.clear();

		// System.out.println(currentState);
		// System.out.println(queue);
		SmartDashboard.putBoolean("upper sensor", getUpperSensor());
		// SmartDashboard.putBoolean("correct color", getCargoState() ==
		// HopperCargoState.CORRECT);
		SmartDashboard.putString("state", getCargoState().toString());
		SmartDashboard.putData(this);
		SmartDashboard.putNumber("red", colorSensor.getRed());
		SmartDashboard.putNumber("blue", colorSensor.getBlue());
		// SmartDashboard.putString("alliance", alliance.toString());
		SmartDashboard.putBoolean("hopper full", hopperFull());
		SmartDashboard.putBoolean("hopper enabled", autoEnabled);
		SmartDashboard.putBoolean("lower sensor", getLowerSensor());

	}

	public HopperCargoState getQueuedOperation() {
		// System.out.println(queue.peek());
		return queue.peek() == null
				? HopperCargoState.EMPTY
				: queue.poll();

	}

	public HopperCargoState peekQueuedOperation() {
		return queue.peek() == null
				? HopperCargoState.EMPTY
				: queue.peek();
	}

	public void setState(HopperSetting state) {
		currentState = state;
	}

	public HopperCargoState getCargoState() {
		// First check to see if there is a match for blue or red cargo. If neither of
		// these are true, return EMPTY
		if (!lowerSensor.get())
			return HopperCargoState.EMPTY;

		// Next check for a match between the alliance color and the sensor reading. If
		// a match is found, return CORRECT
		// if (colorSensor.getBlue() > colorSensor.getRed() && alliance == Alliance.Blue
		// || ((colorSensor.getRed() > colorSensor.getBlue() && alliance ==
		// Alliance.Red)))
		// return HopperCargoState.CORRECT;

		if (alliance == Alliance.Blue) {
			if (colorSensor.getBlue() > colorSensor.getRed())
				return HopperCargoState.CORRECT;
			else
				return HopperCargoState.INCORRECT;
		}

		if (alliance == Alliance.Red) {
			if (colorSensor.getRed() > colorSensor.getBlue())
				return HopperCargoState.CORRECT;
			else
				return HopperCargoState.INCORRECT;
		}

		// If both of these failed, there must be a ball in the hopper of the wrong
		// color, so INCORRECT is returned
		return HopperCargoState.INCORRECT;
	}

	public boolean getUpperSensor() {
		return !upperSensor.get();
	}

	public boolean getLowerSensor() {
		return !lowerSensor.get();
	}

}
