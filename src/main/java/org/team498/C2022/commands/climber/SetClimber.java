package org.team498.C2022.commands.climber;

import java.util.function.DoubleSupplier;

import org.team498.C2022.subsystems.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;

// Sets the climber to a given speed
public class SetClimber extends CommandBase {

	private final Climber climber;
	private DoubleSupplier left;

	public SetClimber(Climber climber, DoubleSupplier speed) {
		this.climber = climber;
		this.left = speed;
		addRequirements(this.climber);
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
	}

	private double deadzone(double input, double deadzone) {
		if (Math.abs(input) > deadzone)
			return input;
		return 0;
	}
}
