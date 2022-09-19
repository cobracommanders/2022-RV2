package frc.robot.commands.climbers;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climbers;

public class ClimberControl extends CommandBase {

	private final Climbers climbers;
	private final BooleanSupplier tuneMode;
	private DoubleSupplier left;
	private DoubleSupplier right;

	public ClimberControl(Climbers climbers, DoubleSupplier left, DoubleSupplier right, BooleanSupplier tuneMode) {
		this.climbers = climbers;
		this.left = left;
		this.right = right;
		this.tuneMode = tuneMode;
		addRequirements(this.climbers);
	}

	@Override
	public void execute() {
		double left = deadzone(this.left.getAsDouble(), 0.2);
		double right = deadzone(this.right.getAsDouble(), 0.2);

		if (tuneMode.getAsBoolean()) {
			climbers.setSpeedLeft(left / 4);
			climbers.setSpeedRight(right / 4);
		} else {
			climbers.setSpeedLeft(left);
			climbers.setSpeedRight(left);
		}
	}

	private double deadzone(double input, double deadzone) {
		if (Math.abs(input) > deadzone)
			return input;
		return 0;
	}
}
