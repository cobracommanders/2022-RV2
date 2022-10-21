package frc.robot.subsystems;

import static frc.robot.Constants.HopperConstants.kBackHopperID;
import static frc.robot.Constants.HopperConstants.kColorSensorLeniency;
import static frc.robot.Constants.HopperConstants.kFrontHopperID;
import static frc.robot.Constants.HopperConstants.kLowerSensorDIO;
import static frc.robot.Constants.HopperConstants.kUpperSensorDIO;

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

public class Hopper extends SubsystemBase {
	private final TalonSRX frontMotor = new TalonSRX(kFrontHopperID);
	private final TalonSRX backMotor = new TalonSRX(kBackHopperID);
	private final ColorSensorV3 colorSensor = new ColorSensorV3(Port.kOnboard);
	private final DigitalInput upperSensor = new DigitalInput(kUpperSensorDIO);
	private final DigitalInput lowerSensor = new DigitalInput(kLowerSensorDIO);

	private HopperSetting currentState = HopperSetting.IDLE;
	private static Alliance alliance = Alliance.Invalid;

	private boolean autoEnabled;

	public Hopper() {
		frontMotor.setInverted(true);
		backMotor.setInverted(false);
		frontMotor.setNeutralMode(NeutralMode.Brake);
		backMotor.setNeutralMode(NeutralMode.Brake);

		updateAlliance();

		autoEnabled = true;
	}

	@Override
	public void periodic() {
		frontMotor.set(ControlMode.PercentOutput, currentState.frontSpeed);
		backMotor.set(ControlMode.PercentOutput, currentState.backSpeed);

		// System.out.println(currentState);
		// System.out.println(queue);
		SmartDashboard.putBoolean("upper sensor", getUpperSensor());
		// SmartDashboard.putBoolean("correct color", getCargoState() ==
		// HopperCargoState.CORRECT);
		SmartDashboard.putString("state", getOperation().toString());
		SmartDashboard.putData(this);
		SmartDashboard.putNumber("red", colorSensor.getRed());
		SmartDashboard.putNumber("blue", colorSensor.getBlue());
		// SmartDashboard.putString("alliance", alliance.toString());
		SmartDashboard.putBoolean("hopper enabled", autoEnabled);
		SmartDashboard.putBoolean("lower sensor", getLowerSensor());

	}

	public enum HopperSetting {
		LOAD(0.25, 0.25),
		INTAKE(0.19, -0.19),
		UNLOAD(-0.25, -0.25),
		OUTTAKE(0.4, -0.4),
		REVERSE(-0.2, -0.2),
		OUTTAKETOP(-0.4, -0.4),
		IDLE(0, 0);

		private double frontSpeed;
		private double backSpeed;

		private HopperSetting(double frontSpeed, double backSpeed) {
			this.frontSpeed = frontSpeed;
			this.backSpeed = backSpeed;
		}
	}

	// The state of any the hopper
	public enum HopperState {
		CORRECT,
		INCORRECT,
		IDLE,
		EMPTY
	}

	public HopperState getOperation() {
		HopperState currentCargo = getColorSensor();
		// If there is a cargo in the bottom of the hopper and the color sensor hasn't
		// read it yet, or if there is a cargo in the top of the hopper and a correct
		// cargo is in the botom of the hopper, return IDLE to stop the hopper
		if ((getLowerSensor() && currentCargo == HopperState.EMPTY)
				|| (getUpperSensor() && currentCargo == HopperState.CORRECT))
			return HopperState.IDLE;
		// Else, return the value provided by the color sensor
		return currentCargo;
	}

	public HopperState getColorSensor() {
		// If neither color sensor is detecting a value signifigant enough to singal the
		// presense of a cargo, return EMPTY
		if (!((colorSensor.getBlue() > kColorSensorLeniency)
				|| (colorSensor.getRed() > kColorSensorLeniency)))
			return HopperState.EMPTY;

		// If the alliance is blue and the reading from blue is stronger than red, or if
		// the alliance is red with a stronger reading from red, return CORRECT
		if ((alliance == Alliance.Blue && (colorSensor.getBlue() > colorSensor.getRed()))
				|| (alliance == Alliance.Red && (colorSensor.getRed() > colorSensor.getBlue())))
			return HopperState.CORRECT;
		// Else, return INCORRECT
		else
			return HopperState.INCORRECT;
	}

	public void setState(HopperSetting state) {
		currentState = state;
	}

	public boolean isFull() {
		return getColorSensor() == HopperState.CORRECT && getUpperSensor();
	}

	public boolean getUpperSensor() {
		return !upperSensor.get();
	}

	public boolean getLowerSensor() {
		return !lowerSensor.get();
	}

	public static void updateAlliance() {
		alliance = DriverStation.getAlliance();
	}

	public void setAutoEnabled(boolean enabled) {
		autoEnabled = enabled;
	}

	public boolean getAutoEnabled() {
		return autoEnabled;
	}

}
