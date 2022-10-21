package frc.robot.commands.climber;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

// Sets each climber arm individually to a given speed
public class TuneClimber extends CommandBase {

	private final Climber climber;
	private DoubleSupplier left;
	private DoubleSupplier right;

	public TuneClimber(Climber climber, DoubleSupplier left, DoubleSupplier right) {
		this.climber = climber;
		this.left = left;
		this.right = right;
		addRequirements(this.climber);
	}

	@Override
	public void execute() {
		double left = deadzone(this.left.getAsDouble(), 0.2);
		double right = deadzone(this.right.getAsDouble(), 0.2);

		climber.setSpeedLeft(left / 4);
		climber.setSpeedRight(right / 4);
	}

	private double deadzone(double input, double deadzone) {
		if (Math.abs(input) > deadzone)
			return input;
		return 0;
	}

}
