package frc.robot;

import static frc.robot.Constants.LimelightConstants.kLimelightLensHeight;
import static frc.robot.Constants.LimelightConstants.kLimelightMountAngle;
import static frc.robot.Constants.LimelightConstants.kVisionTapeHeight;
import static frc.robot.Constants.OIConstants.kControllerRumbleRange;
import static frc.robot.Constants.OIConstants.kDriverControllerID;
import static frc.robot.Constants.OIConstants.kOperatorControllerID;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Axis;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SelectCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.CalibrateGyro;
import frc.robot.commands.LimelightTestingSetup;
import frc.robot.commands.ResetGyro;
import frc.robot.commands.RumbleController;
import frc.robot.commands.auto.ChezyTwoBall;
import frc.robot.commands.centerer.ToggleCenterer;
import frc.robot.commands.climber.SetClimber;
import frc.robot.commands.climber.TuneClimber;
import frc.robot.commands.drivetrain.RotatingLinearTrajectory;
import frc.robot.commands.drivetrain.FieldOrientedDrive;
import frc.robot.commands.drivetrain.LimelightAlign;
import frc.robot.commands.drivetrain.LinearTrajectory;
import frc.robot.commands.drivetrain.ParabolicTrajectory;
import frc.robot.commands.drivetrain.RobotOrientedDrive;
import frc.robot.commands.drivetrain.SplineTrajectory;
import frc.robot.commands.hood.CalibrateHood;
import frc.robot.commands.hopper.IntakeHopper;
import frc.robot.commands.hopper.ShootCargo;
import frc.robot.commands.hopper.ToggleAutoHopper;
import frc.robot.commands.hopper.ToggleHopper;
import frc.robot.commands.intake.ToggleIntake;
import frc.robot.commands.shooter.InterpolateShooter;
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
import frc.util.Limelight;

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

	private final Trigger flywheelAtSpeed = new Trigger(() -> {
		return shooter.atSetpoint(kControllerRumbleRange);
	});

	private final Trigger hopperEnabled = new Trigger(() -> hopper.getAutoEnabled());
	private final Trigger hopperFull = new Trigger(() -> hopper.isFull());
	private final Trigger limelightHasTarget = new Trigger(() -> limelight.hasTarget());

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

		new JoystickButton(operatorController, Button.kA.value).whileHeld(new LimelightAlign(drivetrain, limelight));

		new JoystickButton(driverController, Button.kBack.value).whenPressed(new SequentialCommandGroup(
			//	new ParabolicTrajectory(drivetrain, 1, 1, 0, 0, 1);
			// new LinearTrajectory(drivetrain, 2, 2, 0, 1),
			// new LinearTrajectory(drivetrain, 2, -2, 0, 2),
			// new LinearTrajectory(drivetrain, 0, 2, 0, 2)

			//new RotatingLinearTrajectory(drivetrain, 0, 0, 90, 10)
		));

		new JoystickButton(driverController, Button.kStart.value).toggleWhenActive(new SequentialCommandGroup(
			//	new ParabolicTrajectory(drivetrain, 1, 1, 0, 0, 1);
			// new LinearTrajectory(drivetrain, 2, 2, 0, 1),
			// new LinearTrajectory(drivetrain, 2, -2, 0, 2),
			// new LinearTrajectory(drivetrain, 0, 2, 0, 2)

			new LimelightTestingSetup(shooter, hood, limelight)
		));


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
				// Hopefully will make the intake retract when we have two cargo TODO test
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

		new JoystickButton(driverController, Button.kRightBumper.value)
				// .and(flywheelAtSpeed)
				.whileActiveContinuous(new ToggleHopper(hopper, HopperSetting.LOAD));
				// .whenActive(new ShootCargo(hopper));

		new JoystickButton(operatorController, Button.kLeftBumper.value)
				.whenPressed(new ToggleAutoHopper(hopper));

		new JoystickButton(operatorController, Button.kX.value)
				// .and(limelightHasTarget)
				.toggleWhenActive(new InterpolateShooter(shooter, hood));

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

		flywheelAtSpeed.whileActiveOnce(new RumbleController(driverController, 0.2));
		hopperFull.whileActiveOnce(new RumbleController(operatorController, 0.2));
	}

	public Command getAutoCommand() {
		return new ChezyTwoBall(drivetrain, hood, shooter, hopper);
	}

	public Command getRobotInitCommand() {
		return new CalibrateGyro(drivetrain);
	}
}