package frc.robot;

import static frc.robot.Constants.OIConstants.kControllerRumbleRange;
import static frc.robot.Constants.OIConstants.kDriverControllerID;
import static frc.robot.Constants.OIConstants.kOperatorControllerID;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Axis;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SelectCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.CalibrateGyro;
import frc.robot.commands.ResetGyro;
import frc.robot.commands.RumbleController;
import frc.robot.commands.auto.SimpleTaxi;
import frc.robot.commands.centerer.SetCenterer;
import frc.robot.commands.climber.RaiseClimber;
import frc.robot.commands.climber.SetClimber;
import frc.robot.commands.climber.ZeroClimber;
import frc.robot.commands.drivetrain.FieldOrientedDrive;
import frc.robot.commands.drivetrain.RobotOrientedDrive;
import frc.robot.commands.hood.CalibrateHood;
import frc.robot.commands.hood.SetHood;
import frc.robot.commands.hopper.IntakeHopper;
import frc.robot.commands.hopper.SetHopper;
import frc.robot.commands.hopper.ToggleAutoHopper;
import frc.robot.commands.hopper.ToggleHopper;
import frc.robot.commands.intake.SetIntake;
import frc.robot.commands.shooter.SetShooter;
import frc.robot.commands.wrist.SetWrist;
import frc.robot.subsystems.Centerer;
import frc.robot.subsystems.Centerer.CentererState;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Hood;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Hopper.HopperSetting;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.IntakeState;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.ShooterSetting;
import frc.robot.subsystems.Wrist;
import frc.robot.subsystems.Wrist.WristState;

public class RobotContainer {
	private final Centerer centerer = new Centerer();
	private final Drivetrain drivetrain = new Drivetrain();
	private final Hopper hopper = new Hopper();
	private final Intake intake = new Intake();
	private final Shooter shooter = new Shooter();
	private final Hood hood = new Hood();
	private final Wrist wrist = new Wrist();
	private final Climber climber = new Climber();

	private final XboxController driverController = new XboxController(kDriverControllerID);
	private final XboxController operatorController = new XboxController(kOperatorControllerID);

	private final Trigger intakeButton = new Trigger(() -> {
		return driverController.getRawAxis(Axis.kRightTrigger.value) > 0.3;
	});

	private final Trigger outtakeFrontButton = new Trigger(() -> {
		return driverController.getRawAxis(Axis.kLeftTrigger.value) > 0.3;
	});

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

		climber.setDefaultCommand(
				new SetClimber(
						climber,
						() -> -operatorController.getRawAxis(Axis.kLeftY.value),
						() -> -operatorController.getRawAxis(Axis.kRightY.value),
						climberTuneMode));
	}

	private void configureButtonBindings() {
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

		intakeButton
				// While the button is pressed
				.whileActiveContinuous(
						new ParallelCommandGroup(
								// Start the intake
								new SetIntake(intake, IntakeState.INTAKE),
								// Start the centerers
								new SetCenterer(centerer, CentererState.CENTER),
								// Start the hopper
								new SelectCommand(
										() -> hopper.getAutoEnabled()
												// If automatic sorting is enabled, run the automatic intake command
												? new IntakeHopper(hopper, shooter)
												// If not, just turn on the hopper
												: new SetHopper(hopper, HopperSetting.LOAD))))
				// When the button is first pressed put the wrist out
				.whenActive(new SetWrist(wrist, WristState.OUT))
				// When the button is released put the wrist in
				.whenInactive(new SetWrist(wrist, WristState.IN))
				// If automatic sorting is disabled, set the hopper manually
				.and(hopperEnabled.negate()).whileActiveOnce(new SetHopper(hopper, HopperSetting.LOAD));

		outtakeFrontButton
				// While the button is pressed
				.whileActiveOnce(
						new ParallelCommandGroup(
								// Reverse the intake
								new SetIntake(intake, IntakeState.OUTTAKE),
								// Reverse the centerers
								new SetCenterer(centerer, CentererState.OUTTAKE)))
				// When the button is first pressed put the wrist out
				.whenActive(new SetWrist(wrist, WristState.OUT))
				// When the button is released put the wrist in
				.whenInactive(new SetWrist(wrist, WristState.IN))
				// When automatic sorting is disabled set the hopper to unload cargo as well
				.and(hopperEnabled.negate()).whileActiveOnce(new SetHopper(hopper, HopperSetting.UNLOAD));

		new JoystickButton(driverController, Button.kRightBumper.value)
				.and(flywheelAtSpeed)
				.whileActiveContinuous(new ToggleHopper(hopper, HopperSetting.LOAD));

		new JoystickButton(operatorController, Button.kLeftBumper.value)
				.whenPressed(new ToggleAutoHopper(hopper));

		new JoystickButton(operatorController, Button.kY.value)
				.whenPressed(new SetShooter(shooter, ShooterSetting.LAUNCHPAD.RPM))
				.whenPressed(new SetHood(hood, ShooterSetting.LAUNCHPAD.angle));

		new JoystickButton(operatorController, Button.kB.value)
				.whenPressed(new SetShooter(shooter, ShooterSetting.TARMAC.RPM))
				.whenPressed(new SetHood(hood, ShooterSetting.TARMAC.angle));

		new JoystickButton(operatorController, Button.kA.value)
				.whenPressed(new SetShooter(shooter, ShooterSetting.FENDER.RPM))
				.whenPressed(new SetHood(hood, ShooterSetting.FENDER.angle));

		new JoystickButton(operatorController, Button.kX.value)
				.whenPressed(new SetShooter(shooter, ShooterSetting.IDLE.RPM));

		new JoystickButton(operatorController, Button.kRightBumper.value)
				.whenPressed(new CalibrateHood(hood));

		new JoystickButton(operatorController, Button.kBack.value).whenPressed(new RaiseClimber(climber));
		new JoystickButton(operatorController, Button.kStart.value).whenPressed(new ZeroClimber(climber));

		// new JoystickButton(driverController, Button.kStart.value)
		// .whenPressed(new InstantCommand(() ->
		// shooter.setSpeed(SmartDashboard.getNumber("Shooter RPM", 0))));
		// new JoystickButton(driverController, Button.kBack.value)
		// .whenPressed(new InstantCommand(() ->
		// hood.setAngle(SmartDashboard.getNumber("Hood Angle", 0))));

		flywheelAtSpeed.whileActiveOnce(new RumbleController(driverController, 0.2));
	}

	public Command getAutoCommand() {
		return new SimpleTaxi(drivetrain, hood, shooter, hopper);
	}

	public Command getRobotInitCommand() {
		return new CalibrateGyro(drivetrain);
	}
}