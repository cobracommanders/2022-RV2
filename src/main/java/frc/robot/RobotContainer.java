package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.ResetGyro;
import frc.robot.commands.auto.SimpleTaxi;
import frc.robot.commands.centerer.CentererControl;
import frc.robot.commands.drivetrain.FieldOrientedDrive;
import frc.robot.commands.drivetrain.RobotOrientedDrive;
import frc.robot.commands.hopper.AutoHopper;
import frc.robot.commands.hopper.EjectCargo;
import frc.robot.commands.hopper.SaveCargo;
import frc.robot.commands.hopper.ShootCargo;
import frc.robot.commands.intake.IntakeControl;
import frc.robot.commands.testing.SetHopper;
import frc.robot.commands.testing.SetShooter;
import frc.robot.subsystems.Centerer;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Centerer.CentererState;
import frc.robot.subsystems.Hopper.HopperState;
import frc.robot.subsystems.Intake.IntakeState;
import frc.robot.subsystems.Shooter.ShooterState;

import static frc.robot.Constants.OIConstants.*;

public class RobotContainer {
	private final Centerer centerer = new Centerer();
	private final Drivetrain drivetrain = new Drivetrain();
	private final Hopper hopper = new Hopper();
	private final Intake intake = new Intake();
	private final Shooter shooter = new Shooter();
	// private final Wrist wrist = new Wrist();

	private final XboxController driverController = new XboxController(kDriverControllerID);
	private final XboxController operatorController = new XboxController(kOperatorControllerID);

	public RobotContainer() {
		configureButtonBindings();

		drivetrain.setDefaultCommand(
				new FieldOrientedDrive(
						drivetrain,
						// Forwards/backwards translation
						() -> deadzone(((-driverController.getLeftY() * 2.25) * (-driverController.getLeftY() * 2.25))
								* -driverController.getLeftY(), 0.1),
						// Left/right translation
						() -> deadzone(((-driverController.getLeftX() * 2.25) * (-driverController.getLeftX() * 2.25))
								* -driverController.getLeftX(), 0.1),
						// Rotation
						() -> deadzone(((-driverController.getRightX() * 2.25) * (-driverController.getRightX() * 2.25))
								* -driverController.getRightX(), 0.1)));

		hopper.setDefaultCommand(new AutoHopper(hopper));
	}

	private void configureButtonBindings() {
		// TODO: Add gyro offset for auto
		new JoystickButton(driverController, kGyroResetButton).whenPressed(new ResetGyro(drivetrain));
		new JoystickButton(driverController, kRobotOrientedButton).whileHeld(
				new RobotOrientedDrive(
						drivetrain,
						// Forwards/backwards translation
						() -> deadzone(((-driverController.getLeftY() * 2.25) * (-driverController.getLeftY() * 2.25))
								* -driverController.getLeftY(), 0.1),
						// Left/right translation
						() -> deadzone(((-driverController.getLeftX() * 2.25) * (-driverController.getLeftX() * 2.25))
								* -driverController.getLeftX(), 0.1),
						// Rotation
						() -> deadzone(((-driverController.getRightX() * 2.25) * (-driverController.getRightX() * 2.25))
								* -driverController.getRightX(), 0.1)));

		new JoystickButton(driverController, kSlowSpeedDrive).whileHeld(
				new FieldOrientedDrive(
						drivetrain,
						// Forwards/backwards translation
						() -> deadzone(-driverController.getLeftY() * 1, 0.1),
						// Left/right translation
						() -> deadzone(-driverController.getLeftX() * 1, 0.1),
						// Rotation
						() -> deadzone(-driverController.getRightX() * 1, 0.1)));

		new Trigger(() -> {
			return driverController.getRawAxis(3) > .75;
		}).whileActiveContinuous(new IntakeControl(intake, IntakeState.INTAKE).alongWith(new CentererControl(centerer, CentererState.CENTER)));

		new JoystickButton(driverController, 3).whenPressed(new ShootCargo(hopper));

		new JoystickButton(driverController, 2).whileHeld(new SetShooter(shooter, ShooterState.SHOOT));

		//new JoystickButton(driverController, 3).whenPressed(new EjectCargo(hopper));
		//new JoystickButton(driverController, 4).whenPressed(new SaveCargo(hopper));


	}

	public Command getAutoCommand() {
		return new SimpleTaxi(drivetrain);
	}

	private double deadzone(double input, double deadzone) {
		if (Math.abs(input) > deadzone)
			return input;
		return 0;
	}
}
