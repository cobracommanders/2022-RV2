package frc.robot;

import static frc.robot.Constants.OIConstants.kControllerRumbleRange;
import static frc.robot.Constants.OIConstants.kDriverControllerID;
import static frc.robot.Constants.OIConstants.kOperatorControllerID;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Axis;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.ResetGyro;
import frc.robot.commands.RumbleController;
import frc.robot.commands.auto.SimpleTaxi;
import frc.robot.commands.centerer.CentererControl;
import frc.robot.commands.climbers.ClimbMid;
import frc.robot.commands.climbers.ClimberControl;
import frc.robot.commands.drivetrain.FieldOrientedDrive;
import frc.robot.commands.drivetrain.RobotOrientedDrive;
import frc.robot.commands.hood.Calibrate;
import frc.robot.commands.hood.SetAngle;
import frc.robot.commands.hopper.IntakeHopper;
import frc.robot.commands.hopper.SetHopperManual;
import frc.robot.commands.hopper.ToggleDisabledHopper;
import frc.robot.commands.intake.IntakeControl;
import frc.robot.commands.shooter.SetShooter;
import frc.robot.commands.wrist.WristControl;
import frc.robot.subsystems.Centerer;
import frc.robot.subsystems.Centerer.CentererState;
import frc.robot.subsystems.Climbers;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Hood;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Hopper.HopperSetting;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.IntakeState;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.ShooterSettings;
import frc.robot.subsystems.Wrist;
import frc.robot.subsystems.Wrist.WristState;

//   --------------------------------------------------------------------- CLASS ---------------------------------------------------------------------    \\

public class RobotContainer {
	private final Centerer centerer = new Centerer();
	private final Drivetrain drivetrain = new Drivetrain();
	private final Hopper hopper = new Hopper();
	private final Intake intake = new Intake();
	private final Shooter shooter = new Shooter();
	private final Hood hood = new Hood();
	private final Wrist wrist = new Wrist();
	private final Climbers climbers = new Climbers();

	private final XboxController driverController = new XboxController(kDriverControllerID);
	private final XboxController operatorController = new XboxController(kOperatorControllerID);

	// --------------------------------------------------------------------- BUTTON
	// SETUP ---------------------------------------------------------------------
	// \\

	private final Trigger intakeButton = new Trigger(() -> {
		return driverController.getRawAxis(Axis.kRightTrigger.value) > 0.3;
	});

	public Trigger getIntakeButton() {
		return intakeButton;
	}

	private final Trigger outtakeFrontButton = new Trigger(() -> {
		return driverController.getRawAxis(Axis.kLeftTrigger.value) > 0.3;
	});

	private final Trigger hopperOuttakeButton = new Trigger(() -> {
		return operatorController.getRawAxis(Axis.kLeftTrigger.value) > 0.3;
	});

	// private final Trigger climberUpLeft = new Trigger(() -> {
	// return operatorController.getRawAxis(Axis.kLeftY.value) < -0.3;
	// });

	// private final Trigger climberDownLeft = new Trigger(() -> {
	// return operatorController.getRawAxis(Axis.kLeftY.value) > 0.3;
	// });

	// private final Trigger climberUpRight = new Trigger(() -> {
	// return operatorController.getRawAxis(Axis.kRightY.value) < -0.3;
	// });

	// private final Trigger climberDownRight = new Trigger(() -> {
	// return operatorController.getRawAxis(Axis.kRightY.value) > 0.3;
	// });

	private final Trigger climberTuneMode = new Trigger(() -> {
		return operatorController.getRawAxis(Axis.kRightTrigger.value) > 0.3;
	});

	private final Trigger flywheelAtSpeed = new Trigger(() -> {
		return shooter.atSetpoint(kControllerRumbleRange);
	});

	private final Trigger hopperEnabled = new Trigger(() -> hopper.getAutoEnabled());

	public RobotContainer() {
		configureButtonBindings();

		drivetrain.setDefaultCommand(
				new FieldOrientedDrive(
						drivetrain,
						// Forwards/backwards translation
						() -> -driverController.getLeftY(),
						// Left/right translation
						() -> -driverController.getLeftX(),
						// Rotation
						() -> -driverController.getRightX(),
						// Deadzone
						0.1,
						// Slow speed
						() -> driverController.getRawButton(Button.kLeftBumper.value)));

		//hopper.setDefaultCommand(new AutoHopper(hopper, intakeButton));
		// hood.setDefaultCommand();

		climbers.setDefaultCommand(
				new ClimberControl(
						climbers,
						() -> -operatorController.getRawAxis(Axis.kLeftY.value),
						() -> -operatorController.getRawAxis(Axis.kRightY.value),
						climberTuneMode));
	}

	private void configureButtonBindings() {
		// TODO: Add gyro offset for auto
		new JoystickButton(driverController, Button.kA.value).whenPressed(new ResetGyro(drivetrain));

		new JoystickButton(driverController, Button.kB.value).toggleWhenPressed(
				new RobotOrientedDrive(
						drivetrain,
						// Forwards/backwards translation
						() -> -driverController.getLeftY(),
						// Left/right translation
						() -> -driverController.getLeftX(),
						// Rotation
						() -> -driverController.getRightX(),
						// Deadzone
						0.1,
						// Slow speed
						() -> driverController.getRawButton(Button.kLeftBumper.value)));

		// ------------------------------------------------------------------- INTAKE
		// ---------------------------------------------------------------------------
		// \\

		intakeButton.whileActiveOnce(
				new IntakeControl(intake, IntakeState.INTAKE)
						.alongWith(new CentererControl(centerer, CentererState.CENTER))
						//.alongWith(new SetHopperManual(hopper, HopperSetting.LOAD, 0.2))
						.alongWith(new IntakeHopper(hopper)));

		intakeButton.whenActive(new WristControl(wrist, WristState.OUT));
		intakeButton.whenInactive(new WristControl(wrist, WristState.IN));

		outtakeFrontButton.whileActiveOnce(
				new IntakeControl(intake, IntakeState.OUTTAKE)
						.alongWith(new CentererControl(centerer, CentererState.OUTTAKE))
						//.alongWith(new SetHopperManual(hopper, HopperSetting.LOAD, -0.25))
						);

		outtakeFrontButton.whenActive(new WristControl(wrist, WristState.OUT));
		// outtakeFrontButton.whenActive(new ToggleDisabledHopper(hopper));
		outtakeFrontButton.whenInactive(new WristControl(wrist, WristState.IN));
		// outtakeFrontButton.whenInactive(new ToggleDisabledHopper(hopper));

		new JoystickButton(driverController, Button.kRightBumper.value)
				.and(flywheelAtSpeed)
				.whileActiveContinuous(new SetHopperManual(hopper, HopperSetting.LOAD, 0.4))
				.whenInactive(() -> hopper.setCargoCount(0));

		hopperOuttakeButton
				.and(hopperEnabled.negate())
				.whileActiveContinuous(new SetHopperManual(hopper, HopperSetting.LOAD, -0.6));

		new JoystickButton(operatorController,
				Button.kLeftBumper.value).whenPressed(new ToggleDisabledHopper(hopper));

		new JoystickButton(operatorController, Button.kY.value)
				.whenPressed(new SetShooter(shooter, ShooterSettings.LAUNCHPAD.RPM))
				.whenPressed(new SetAngle(hood, ShooterSettings.LAUNCHPAD.angle));

		new JoystickButton(operatorController, Button.kB.value)
				.whenPressed(new SetShooter(shooter, ShooterSettings.TARMAC.RPM))
				.whenPressed(new SetAngle(hood, ShooterSettings.TARMAC.angle));

		new JoystickButton(operatorController, Button.kA.value)
				.whenPressed(new SetShooter(shooter, ShooterSettings.FENDER.RPM))
				.whenPressed(new SetAngle(hood, ShooterSettings.FENDER.angle));

		new JoystickButton(operatorController, Button.kX.value)
				.whenPressed(new SetShooter(shooter, ShooterSettings.IDLE.RPM));

		new JoystickButton(operatorController, Button.kRightBumper.value)
				.whenPressed(new Calibrate(hood));

		new JoystickButton(operatorController, Button.kBack.value).whenPressed(new ClimbMid(climbers));
		new JoystickButton(operatorController, Button.kStart.value).whenPressed(() -> climbers.resetEncoder());

		new JoystickButton(driverController, Button.kStart.value).whenPressed(new InstantCommand(() -> shooter.setSpeed(SmartDashboard.getNumber("Shooter RPM", 0))));
		new JoystickButton(driverController, Button.kBack.value).whenPressed(new InstantCommand(() -> hood.setAngle(SmartDashboard.getNumber("Hood Angle", 0))));

		flywheelAtSpeed.whileActiveOnce(new RumbleController(driverController, 0.2));

		/*
		 * climberToggle
		 * .whenActive(
		 * new ToggleClimberTuning(
		 * climbers,
		 * () -> deadzone(operatorController.getRawAxis(Axis.kLeftY.value), 0.2),
		 * () -> deadzone(operatorController.getRawAxis(Axis.kRightY.value), 0.2)));
		 */
	}

	public Command getAutoCommand() {
		return new SimpleTaxi(drivetrain, hood, shooter, hopper);
	}
}