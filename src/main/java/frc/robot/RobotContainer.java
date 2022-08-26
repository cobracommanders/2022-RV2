package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.commands.DefaultDrive;
import frc.robot.subsystems.Drivetrain;
import static frc.robot.Constants.OIConstants.*;

public class RobotContainer {
	//private final Centerer centerer = new Centerer();
	private final Drivetrain drivetrain = new Drivetrain();
	//private final Hopper hopper = new Hopper();
	//private final Intake intake = new Intake();
	//private final Wrist wrist = new Wrist();

	private final XboxController driverController = new XboxController(kDriverControllerID);
	private final XboxController operatorController = new XboxController(kOperatorControllerID);

	public RobotContainer() {
		configureButtonBindings();

		drivetrain.setDefaultCommand(
				// TODO: Replace X/Y axes with forwards/backwards and left/right
				new DefaultDrive(
						drivetrain,
						// X axis translation
						() -> deadzone(driverController.getLeftX(), 0.1),
						// Y axis translation
						() -> deadzone(driverController.getLeftY(), 0.1),
						// Rotation
						() -> deadzone(driverController.getRightX(), 0.1)));
	}

	private void configureButtonBindings() {

	}

	private double deadzone(double input, double deadzone) {
		if (Math.abs(input) > deadzone)
			return input;
		return 0;
	}
}
