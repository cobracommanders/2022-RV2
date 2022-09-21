package frc.robot.commands.climber;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

public class SetClimber extends CommandBase {

	private final Climber climber;
	private final BooleanSupplier tuneMode;
	private DoubleSupplier left;
	private DoubleSupplier right;

	public SetClimber(Climber climber, DoubleSupplier left, DoubleSupplier right, BooleanSupplier tuneMode) {
		this.climber = climber;
		this.left = left;
		this.right = right;
		this.tuneMode = tuneMode;
		addRequirements(this.climber);
	}

	@Override
	public void execute() {
		double left = deadzone(this.left.getAsDouble(), 0.2);
		double right = deadzone(this.right.getAsDouble(), 0.2);

		if (tuneMode.getAsBoolean()) {
			climber.setSpeedLeft(left / 4);
			climber.setSpeedRight(right / 4);
		} else {
			climber.setSpeedLeft(left);
			climber.setSpeedRight(left);
		}
	}

	private double deadzone(double input, double deadzone) {
		if (Math.abs(input) > deadzone)
			return input;
		return 0;
	}
}
