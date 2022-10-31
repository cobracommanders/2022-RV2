package org.team498.C2022;

import static org.team498.C2022.Constants.LimelightConstants.kLimelightLensHeight;
import static org.team498.C2022.Constants.LimelightConstants.kLimelightMountAngle;
import static org.team498.C2022.Constants.LimelightConstants.kVisionTapeHeight;
import static org.team498.C2022.Constants.OIConstants.kDriverControllerID;
import static org.team498.C2022.Constants.OIConstants.kOperatorControllerID;

import org.team498.C2022.Tables.DriveTables;
import org.team498.C2022.commands.CalibrateGyro;
import org.team498.C2022.commands.LimelightTestingSetup;
import org.team498.C2022.commands.ResetGyro;
import org.team498.C2022.commands.RumbleControllerLeft;
import org.team498.C2022.commands.RumbleControllerRight;
import org.team498.C2022.commands.auto.StateChampsTwoBall;
import org.team498.C2022.commands.auto.TableFollower;
import org.team498.C2022.commands.centerer.ToggleCenterer;
import org.team498.C2022.commands.climber.SetClimber;
import org.team498.C2022.commands.climber.TuneClimber;
import org.team498.C2022.commands.drivetrain.FieldOrientedDrive;
import org.team498.C2022.commands.drivetrain.LimelightAlign;
import org.team498.C2022.commands.drivetrain.RobotOrientedDrive;
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
import org.team498.app.ControllerApp;
import org.team498.lib.drivers.Limelight;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Axis;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SelectCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
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

	private final Trigger intakeButton = new Trigger(() -> {
		return driverController.getRawAxis(Axis.kRightTrigger.value) > 0.3;
	});

	private final Trigger outtakeFrontButton = new Trigger(() -> {
		return driverController.getRawAxis(Axis.kLeftTrigger.value) > 0.3;
	});

	private final Trigger climberUp = new Trigger(() -> {
		return operatorController.getRawAxis(Axis.kRightTrigger.value) > 0.3;
	});

	private final Trigger climberDown = new Trigger(() -> {
		return operatorController.getRawAxis(Axis.kLeftTrigger.value) > 0.3;
	});


	private final Trigger hopperEnabled = new Trigger(() -> hopper.getAutoEnabled());
	private final Trigger hopperFull = new Trigger(() -> hopper.isFull());

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
	}

	private void configureButtonBindings() {
		new JoystickButton(driverController, Button.kA.value).whenPressed(new ResetGyro(drivetrain));

		new JoystickButton(operatorController, Button.kY.value).whileHeld(new LimelightAlign(drivetrain, limelight));

		new JoystickButton(driverController, Button.kBack.value).whenPressed(new SequentialCommandGroup(
				// new ParabolicTrajectory(drivetrain, 1, 1, 0, 0, 1);
				// new LinearTrajectory(drivetrain, 2, 2, 0, 1),
				// new LinearTrajectory(drivetrain, 2, -2, 0, 2),
				// new LinearTrajectory(drivetrain, 0, 2, 0, 2)

				new StateChampsTwoBall(drivetrain, hood, shooter, hopper, intake, wrist, centerer, limelight)));
		// new RotatingLinearTrajectory(drivetrain, 0, 0, 90, 10)));

		new JoystickButton(driverController, Button.kStart.value).toggleWhenActive(new SequentialCommandGroup(
				// new ParabolicTrajectory(drivetrain, 1, 1, 0, 0, 1);
				// new LinearTrajectory(drivetrain, 2, 2, 0, 1),
				// new LinearTrajectory(drivetrain, 2, -2, 0, 2),
				// new LinearTrajectory(drivetrain, 0, 2, 0, 2)

				new LimelightTestingSetup(shooter, hood, limelight)));

		// new JoystickButton(driverController, Button.kBack.value).whenPressed(
		// new SplineTrajectory(drivetrain, new double[] { 0, 0, 0 }, new double[] { 0,
		// 0, 0 },
		// new double[] { 1, 0, 0 }, new double[] { 0, 0, 0 }, 1));

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
				// Makes the intake retract when we have two cargo
				.and(hopperFull.negate())
				// While the button is pressed
				.whileActiveContinuous(
						new ParallelCommandGroup(
								// Start the intake
								new ToggleIntake(intake, IntakeState.INTAKE),
								// Start the hopper
								new SelectCommand(
										() -> hopper.getAutoEnabled()
												// If automatic sorting is enabled, run the automatic intake command
												? new IntakeHopper(hopper, shooter, centerer)
												// If not, just turn on the hopper and centerer manually
												: new ParallelCommandGroup(
														new ToggleHopper(hopper, HopperSetting.LOAD),
														new ToggleCenterer(centerer, CentererState.CENTER)))))
				// When the button is first pressed put the wrist out
				.whenActive(new SetWrist(wrist, WristState.OUT))
				// When the button is released put the wrist in
				.whenInactive(new SetWrist(wrist, WristState.IN));

		outtakeFrontButton
				// While the button is pressed
				.whileActiveOnce(
						new ParallelCommandGroup(
								// Reverse the intake
								new ToggleIntake(intake, IntakeState.OUTTAKE),
								// Reverse the centerers
								new ToggleCenterer(centerer, CentererState.OUTTAKE)))
				// When the button is first pressed put the wrist out
				.whenActive(new SetWrist(wrist, WristState.OUT))
				// When the button is released put the wrist in
				.whenInactive(new SetWrist(wrist, WristState.IN))
				// When automatic sorting is disabled set the hopper to unload cargo as well
				.and(hopperEnabled.negate())
				.whileActiveContinuous(new SetShooter(shooter, ShooterSetting.REVERSE.RPM))
				.whileActiveOnce(new ToggleHopper(hopper, HopperSetting.OUTTAKETOP))
				.whenInactive(new SetShooter(shooter, 0));

		new JoystickButton(operatorController, Button.kStart.value)
			.toggleWhenActive(new ControllerApp(driverController, "trajectory1.csv"));
			//TODO: Test the input recorder
		new JoystickButton(driverController, Button.kRightBumper.value)
				// .and(flywheelAtSpeed)
				.whileActiveContinuous(new ToggleHopper(hopper, HopperSetting.LOAD));
		// .whenActive(new ShootCargo(hopper));

		new JoystickButton(operatorController, Button.kLeftBumper.value)
				.whenPressed(new ToggleAutoHopper(hopper));

		new JoystickButton(operatorController, Button.kX.value)
				// .and(limelightHasTarget)
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

		new JoystickButton(operatorController, Button.kStart.value).toggleWhenPressed(
				new TuneClimber(
						climber,
						() -> -operatorController.getRawAxis(Axis.kLeftY.value),
						() -> -operatorController.getRawAxis(Axis.kRightY.value)),
				false);

		climberUp.whileActiveOnce(
				new SetClimber(climber, () -> operatorController.getRawAxis(Axis.kRightTrigger.value)));

		climberDown.whileActiveOnce(
				new SetClimber(climber, () -> -operatorController.getRawAxis(Axis.kLeftTrigger.value)));

		// flywheelAtSpeed.whileActiveOnce(new RumbleControllerRight(driverController,
		// 0.2));

		hopperFull.whileActiveContinuous(new RumbleControllerRight(driverController, 0.2));
	}

	public Command getAutoCommand() {
		return 
		new TableFollower(drivetrain, DriveTables.trajectory1);
		//new StateChampsTwoBall(drivetrain, hood, shooter, hopper, intake, wrist, centerer, limelight);
	}

	public Command getRobotInitCommand() {
		return new CalibrateGyro(drivetrain);
	}
}