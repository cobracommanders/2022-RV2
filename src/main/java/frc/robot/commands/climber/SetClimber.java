package frc.robot.commands.climber;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.Climber;

public class SetClimber extends CommandBase {

	private final Climber climber;
	private DoubleSupplier left;

	public SetClimber(Climber climber, DoubleSupplier speed) {
		this.climber = climber;
		this.left = speed;
		addRequirements(this.climber);
	}

	@Override
	public void initialize() {
		Robot.logger.log("Starting climbers");
	}

	@Override
	public void execute() {
		double left = deadzone(this.left.getAsDouble(), 0.3);

		climber.setSpeedLeft(left);
		climber.setSpeedRight(left);
	}

	@Override
	public void end(boolean interrupted) {
		climber.setSpeedLeft(0);
		climber.setSpeedRight(0);
		Robot.logger.log("Stopping climbers");
	}

	private double deadzone(double input, double deadzone) {
		if (Math.abs(input) > deadzone)
			return input;
		return 0;
	}
}
