package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.commands.DefaultDrive;
import frc.robot.subsystems.Centerer;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Wrist;
import static frc.robot.Constants.OIConstants.*;

public class RobotContainer {
	private final Centerer centerer = new Centerer();
	private final Drivetrain drivetrain = new Drivetrain();
	private final Hopper hopper = new Hopper();
	private final Intake intake = new Intake();
	private final Wrist wrist = new Wrist();

	private final XboxController driverController = new XboxController(kDriverControllerID);
	private final XboxController operatorController = new XboxController(kOperatorControllerID);

	public RobotContainer() {
		configureButtonBindings();

		drivetrain.setDefaultCommand(
				// TODO: Replace X/Y axes with forwards/backwards and left/right
				new DefaultDrive(
						drivetrain,
						// X axis translation
						() -> driverController.getLeftX(),
						// Y axis translation
						() -> driverController.getLeftY(),
						// Rotation
						() -> driverController.getRightY()));
	}

	private void configureButtonBindings() {

	}
}
