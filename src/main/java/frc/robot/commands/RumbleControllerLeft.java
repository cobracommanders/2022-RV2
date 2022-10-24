package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.CommandBase;

// Sets the rumble on a given xbox controller
public class RumbleControllerLeft extends CommandBase {
	private final XboxController controller;
	private final double power;

	public RumbleControllerLeft(XboxController controller, double power) {
		this.controller = controller;
		this.power = power;
	}

	@Override
	public void initialize() {
		controller.setRumble(RumbleType.kLeftRumble, power);
	}

	@Override
	public void end(boolean interrupted) {
		controller.setRumble(RumbleType.kLeftRumble, 0);
	}
}
