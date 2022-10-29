package org.team498.C2022;

import static org.team498.C2022.Constants.LimelightConstants.kLimelightLensHeight;
import static org.team498.C2022.Constants.LimelightConstants.kLimelightMountAngle;
import static org.team498.C2022.Constants.LimelightConstants.kVisionTapeHeight;
import static org.team498.C2022.Constants.OIConstants.kControllerRumbleRange;
import static org.team498.C2022.Constants.OIConstants.kDriverControllerID;
import static org.team498.C2022.Constants.OIConstants.kOperatorControllerID;

import org.team498.C2022.commands.CalibrateGyro;
import org.team498.C2022.commands.LimelightTestingSetup;
import org.team498.C2022.commands.ResetGyro;
import org.team498.C2022.commands.RumbleControllerLeft;
import org.team498.C2022.commands.RumbleControllerRight;
import org.team498.C2022.commands.auto.StateChampsTwoBall;
import org.team498.C2022.commands.centerer.ToggleCenterer;
import org.team498.C2022.commands.climber.SetClimber;
import org.team498.C2022.commands.climber.TuneClimber;
import org.team498.C2022.commands.drivetrain.FieldOrientedDrive;
import org.team498.C2022.commands.drivetrain.FieldOrientedDriveRotate;
import org.team498.C2022.commands.drivetrain.LimelightAlign;
import org.team498.C2022.commands.drivetrain.RobotOrientedDrive;
import org.team498.C2022.commands.drivetrain.XLock;
import org.team498.C2022.commands.hood.CalibrateHood;
import org.team498.C2022.commands.hood.SetHood;
import org.team498.C2022.commands.hopper.IntakeHopper;
import org.team498.C2022.commands.hopper.ToggleAutoHopper;
import org.team498.C2022.commands.hopper.ToggleHopper;
import org.team498.C2022.commands.intake.ToggleIntake;
import org.team498.C2022.commands.shooter.InterpolateShooter;
import org.team498.C2022.commands.shooter.SetShooter;
import org.team498.C2022.commands.wrist.SetWrist;
import org.team498.C2022.subsystems.Centerer;
import org.team498.C2022.subsystems.Centerer.CentererState;
import org.team498.C2022.subsystems.Climber;
import org.team498.C2022.subsystems.Drivetrain;
import org.team498.C2022.subsystems.Hood;
import org.team498.C2022.subsystems.Hopper;
import org.team498.C2022.subsystems.Hopper.HopperSetting;
import org.team498.C2022.subsystems.Intake;
import org.team498.C2022.subsystems.Intake.IntakeState;
import org.team498.C2022.subsystems.Shooter;
import org.team498.C2022.subsystems.Shooter.ShooterSetting;
import org.team498.C2022.subsystems.Wrist;
import org.team498.C2022.subsystems.Wrist.WristState;
import org.team498.lib.drivers.Limelight;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Axis;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SelectCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class RobotContainer {
	private final Limelight limelight = new Limelight(kLimelightLensHeight, kLimelightMountAngle, kVisionTapeHeight);
	private final Centerer centerer = new Centerer();
	private final Drivetrain drivetrain = new Drivetrain();
	private final Hopper hopper = new Hopper();
	private final Intake intake = new Intake();
	private final Shooter shooter = new Shooter(limelight);
	private final Hood hood = new Hood(limelight);
	private final Wrist wrist = new Wrist();
	private final Climber climber = new Climber();

	private final XboxController driverController = new XboxController(kDriverControllerID);
	private final XboxController operatorController = new XboxController(kOperatorControllerID);

	public enum ControlSet {
		STANDARD,
		FANCY
	}

	public RobotContainer(ControlSet controls) {
		setControls(controls);
	}

	public void setControls(ControlSet controls) {
		CommandScheduler.getInstance().clearButtons();
		switch (controls) {
			case STANDARD:
				configureButtonBindingsStandard();
				break;

			case FANCY:
				configureButtonBindingsFancy();
				break;

			default:
				configureButtonBindingsStandard();
				break;
		}
	}

	/**
	 * Standard driver controls
	 */
	private void configureButtonBindingsStandard() {
		final Trigger intakeButton = new Trigger(() -> driverController.getRawAxis(Axis.kRightTrigger.value) > 0.3);
		final Trigger outtakeButton = new Trigger(() -> driverController.getRawAxis(Axis.kLeftTrigger.value) > 0.3);
		final Trigger climberUp = new Trigger(() -> operatorController.getRawAxis(Axis.kRightTrigger.value) > 0.3);
		final Trigger climberDown = new Trigger(() -> operatorController.getRawAxis(Axis.kLeftTrigger.value) > 0.3);
		final Trigger hopperEnabled = new Trigger(() -> hopper.getAutoEnabled());
		final Trigger hopperFull = new Trigger(() -> hopper.isFull());
		final Trigger flywheelAtSpeed = new Trigger(() -> shooter.atSetpoint(kControllerRumbleRange));

		/* DRIVER CONTROLLER */
		new JoystickButton(driverController, Button.kY.value)
				.whileActiveOnce(new XLock(drivetrain));

		new JoystickButton(driverController, Button.kA.value)
				.whenPressed(new ResetGyro(drivetrain));

		new JoystickButton(driverController, Button.kStart.value)
				.toggleWhenActive(new LimelightTestingSetup(shooter, hood, limelight));

		new JoystickButton(driverController, Button.kRightBumper.value)
				.and(flywheelAtSpeed)
				.whileActiveContinuous(new ToggleHopper(hopper, HopperSetting.LOAD));
		// .whenActive(new ShootCargo(hopper));

		new JoystickButton(driverController, Button.kB.value).toggleWhenPressed(
				new RobotOrientedDrive(
						drivetrain,
						() -> -driverController.getRightY(), // Forwards/backwards translation
						() -> -driverController.getRightX(), // Left/right translation
						() -> -driverController.getLeftX(), // Rotation
						0.1, // Deadzone
						() -> driverController.getRawButton(Button.kLeftBumper.value) // Slow speed
				));

		intakeButton
				.and(hopperFull.negate()) // Makes the intake retract when we have two cargo
				.whileActiveContinuous( // While the button is pressed
						new ParallelCommandGroup(
								new ToggleIntake(intake, IntakeState.INTAKE), // Start the intake
								new SelectCommand( // Start the hopper
										() -> hopper.getAutoEnabled() // If automatic sorting is enabled
												? new IntakeHopper(hopper, shooter, centerer) // Auto intake
												: new ParallelCommandGroup( // Manually intake
														new ToggleHopper(hopper, HopperSetting.LOAD),
														new ToggleCenterer(centerer, CentererState.CENTER)))))
				.whenActive(new SetWrist(wrist, WristState.OUT)) // When the button is first pressed put the wrist out
				.whenInactive(new SetWrist(wrist, WristState.IN) // When the button is released put the wrist in
				);

		outtakeButton
				.whileActiveOnce( // While the button is pressed
						new ParallelCommandGroup(
								new ToggleIntake(intake, IntakeState.OUTTAKE), // Reverse the intake
								new ToggleCenterer(centerer, CentererState.OUTTAKE))) // Reverse the centerers
				.whenActive(new SetWrist(wrist, WristState.OUT)) // When the button is first pressed put the wrist out
				.whenInactive(new SetWrist(wrist, WristState.IN)) // When the button is released put the wrist in
				.and(hopperEnabled.negate()) // When automatic sorting is disabled
				.whileActiveContinuous(new SetShooter(shooter, ShooterSetting.REVERSE.RPM)) // Reverse shooter
				.whileActiveOnce(new ToggleHopper(hopper, HopperSetting.OUTTAKETOP)) // Reverse hopper
				.whenInactive(new SetShooter(shooter, 0) // Turn off shooter when the command ends
				);

		/* OPERATOR CONTROLLER */
		new JoystickButton(operatorController, Button.kY.value)
				.whileHeld(new LimelightAlign(drivetrain, limelight));

		new JoystickButton(operatorController, Button.kBack.value)
				.whenPressed(new InstantCommand(() -> wrist.resetEncoders()));

		new JoystickButton(operatorController, Button.kLeftBumper.value)
				.whenPressed(new ToggleAutoHopper(hopper));

		new JoystickButton(operatorController, Button.kX.value)
				.toggleWhenActive(new ParallelCommandGroup(
						new InterpolateShooter(shooter, hood),
						new RumbleControllerLeft(operatorController, 1)));

		new JoystickButton(operatorController, Button.kA.value)
				.toggleWhenActive(new ParallelCommandGroup(
						new SetShooter(shooter, 2000),
						new SetHood(hood, 0),
						new RumbleControllerLeft(operatorController, 1)));

		new JoystickButton(operatorController, Button.kB.value)
				.toggleWhenActive(new ParallelCommandGroup(
						new SetShooter(shooter, 1000),
						new SetHood(hood, 1),
						new RumbleControllerLeft(operatorController, 1)));

		new JoystickButton(operatorController, Button.kRightBumper.value)
				.whenPressed(new CalibrateHood(hood));

		new JoystickButton(operatorController, Button.kStart.value)
				.toggleWhenPressed(new TuneClimber(
						climber,
						() -> -operatorController.getRawAxis(Axis.kLeftY.value),
						() -> -operatorController.getRawAxis(Axis.kRightY.value)),
						false);

		climberUp.whileActiveOnce(
				new SetClimber(climber, () -> operatorController.getRawAxis(Axis.kRightTrigger.value)));

		climberDown.whileActiveOnce(
				new SetClimber(climber, () -> -operatorController.getRawAxis(Axis.kLeftTrigger.value)));

		/* IDLE COMMANDS */
		hopperFull.whileActiveContinuous(
				new RumbleControllerRight(driverController, 0.2));

		/* DEFAULT COMMANDS */
		drivetrain.setDefaultCommand(
				new FieldOrientedDrive(
						drivetrain,
						() -> -driverController.getLeftY(), // Forwards/backwards translation
						() -> -driverController.getLeftX(), // Left/right translation
						() -> -driverController.getRightX(), // Rotation
						0.1, // Deadzone
						() -> driverController.getRawButton(Button.kLeftBumper.value)) // Slow speed
		);
	}

	/**
	 * Fancier driver controls which allow changing the center of the rotation of
	 * the robot
	 */
	private void configureButtonBindingsFancy() {
		final Trigger intakeButton = new Trigger(() -> driverController.getRawAxis(Axis.kRightTrigger.value) > 0.3);
		final Trigger outtakeButton = new Trigger(() -> driverController.getRawAxis(Axis.kLeftTrigger.value) > 0.3);
		final Trigger climberUp = new Trigger(() -> operatorController.getRawAxis(Axis.kRightTrigger.value) > 0.3);
		final Trigger climberDown = new Trigger(() -> operatorController.getRawAxis(Axis.kLeftTrigger.value) > 0.3);
		final Trigger hopperEnabled = new Trigger(() -> hopper.getAutoEnabled());
		final Trigger hopperFull = new Trigger(() -> hopper.isFull());
		final Trigger flywheelAtSpeed = new Trigger(() -> shooter.atSetpoint(kControllerRumbleRange));

		/* DRIVER CONTROLLER */
		new JoystickButton(driverController, Button.kA.value)
				.whenPressed(new ResetGyro(drivetrain));

		new JoystickButton(driverController, Button.kY.value)
				.whileActiveOnce(new XLock(drivetrain));

		new JoystickButton(driverController, Button.kStart.value)
				.toggleWhenActive(new LimelightTestingSetup(shooter, hood, limelight));

		new JoystickButton(driverController, Button.kRightBumper.value)
				.and(flywheelAtSpeed)
				.whileActiveContinuous(new ToggleHopper(hopper, HopperSetting.LOAD));
		// .whenActive(new ShootCargo(hopper));

		new JoystickButton(driverController, Button.kB.value).toggleWhenPressed(
				new RobotOrientedDrive(
						drivetrain,
						() -> -driverController.getRightY(), // Forwards/backwards translation
						() -> -driverController.getRightX(), // Left/right translation
						() -> -driverController.getLeftX(), // Rotation
						0.1, // Deadzone
						() -> driverController.getRawButton(Button.kLeftBumper.value) // Slow speed
				));

		intakeButton
				.and(hopperFull.negate()) // Makes the intake retract when we have two cargo
				.whileActiveContinuous( // While the button is pressed
						new ParallelCommandGroup(
								new ToggleIntake(intake, IntakeState.INTAKE), // Start the intake
								new SelectCommand( // Start the hopper
										() -> hopper.getAutoEnabled() // If automatic sorting is enabled
												? new IntakeHopper(hopper, shooter, centerer) // Auto intake
												: new ParallelCommandGroup( // Manually intake
														new ToggleHopper(hopper, HopperSetting.LOAD),
														new ToggleCenterer(centerer, CentererState.CENTER)))))
				.whenActive(new SetWrist(wrist, WristState.OUT)) // When the button is first pressed put the wrist out
				.whenInactive(new SetWrist(wrist, WristState.IN) // When the button is released put the wrist in
				);

		outtakeButton
				.whileActiveOnce(// While the button is pressed
						new ParallelCommandGroup(
								new ToggleIntake(intake, IntakeState.OUTTAKE), // Reverse the intake
								new ToggleCenterer(centerer, CentererState.OUTTAKE))) // Reverse the centerers
				.whenActive(new SetWrist(wrist, WristState.OUT)) // When the button is first pressed put the wrist out
				.whenInactive(new SetWrist(wrist, WristState.IN)) // When the button is released put the wrist in
				.and(hopperEnabled.negate()) // When automatic sorting is disabled
				.whileActiveContinuous(new SetShooter(shooter, ShooterSetting.REVERSE.RPM)) // Reverse shooter
				.whileActiveOnce(new ToggleHopper(hopper, HopperSetting.OUTTAKETOP)) // Reverse hopper
				.whenInactive(new SetShooter(shooter, 0) // Turn off shooter when the command ends
				);

		/* OPERATOR CONTROLLER */
		new JoystickButton(operatorController, Button.kY.value)
				.whileHeld(new LimelightAlign(drivetrain, limelight));

		new JoystickButton(operatorController, Button.kBack.value)
				.whenPressed(new InstantCommand(() -> wrist.resetEncoders()));

		new JoystickButton(operatorController, Button.kLeftBumper.value)
				.whenPressed(new ToggleAutoHopper(hopper));

		new JoystickButton(operatorController, Button.kX.value)
				.toggleWhenActive(new ParallelCommandGroup(
						new InterpolateShooter(shooter, hood),
						new RumbleControllerLeft(operatorController, 1)));

		new JoystickButton(operatorController, Button.kA.value)
				.toggleWhenActive(new ParallelCommandGroup(
						new SetShooter(shooter, 2000),
						new SetHood(hood, 0),
						new RumbleControllerLeft(operatorController, 1)));

		new JoystickButton(operatorController, Button.kB.value)
				.toggleWhenActive(new ParallelCommandGroup(
						new SetShooter(shooter, 1000),
						new SetHood(hood, 1),
						new RumbleControllerLeft(operatorController, 1)));

		new JoystickButton(operatorController, Button.kRightBumper.value)
				.whenPressed(new CalibrateHood(hood));

		new JoystickButton(operatorController, Button.kStart.value)
				.toggleWhenPressed(new TuneClimber(
						climber,
						() -> -operatorController.getRawAxis(Axis.kLeftY.value),
						() -> -operatorController.getRawAxis(Axis.kRightY.value)),
						false);

		climberUp.whileActiveOnce(
				new SetClimber(climber, () -> operatorController.getRawAxis(Axis.kRightTrigger.value)));

		climberDown.whileActiveOnce(
				new SetClimber(climber, () -> -operatorController.getRawAxis(Axis.kLeftTrigger.value)));

		/* IDLE COMMANDS */
		hopperFull.whileActiveContinuous(
				new RumbleControllerRight(driverController, 0.2));

		/* DEFAULT COMMANDS */
		drivetrain.setDefaultCommand(
				new FieldOrientedDriveRotate(
						drivetrain,
						() -> -driverController.getRightY(), // Forwards/backwards translation
						() -> -driverController.getRightX(), // Left/right translation
						() -> -driverController.getLeftX(), // Rotation
						0.1, // Deadzone
						() -> driverController.getRawButton(Button.kLeftBumper.value), // Slow speed
						() -> driverController.getPOV()) // Center of rotation
		);
	}

	public Command getAutoCommand() {
		return new StateChampsTwoBall(drivetrain, hood, shooter, hopper, intake, wrist, centerer, limelight);
	}

	public Command getRobotInitCommand() {
		return new CalibrateGyro(drivetrain);
	}
}