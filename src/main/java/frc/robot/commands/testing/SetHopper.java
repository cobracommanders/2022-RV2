package frc.robot.commands.testing;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Hopper.HopperState;

public class SetHopper extends CommandBase {
	private final Hopper hopper;
	private final HopperState state;

	public SetHopper(Hopper hopper, HopperState state) {
		this.hopper = hopper;
		this.state = state;
		addRequirements(this.hopper);
	}

	@Override
	public void initialize() {
		hopper.setState(state);
	}

	@Override
	public void execute() {
	}

	@Override
	public void end(boolean interrupted) {
		hopper.setState(HopperState.IDLE);
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}