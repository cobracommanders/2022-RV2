package frc.robot.commands.hopper;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Hopper.HopperSetting;

// Sets the hopper to a given state
public class SetHopper extends InstantCommand {
	private final Hopper hopper;
	private final HopperSetting state;

	public SetHopper(Hopper hopper, HopperSetting state) {
		this.hopper = hopper;
		this.state = state;
		addRequirements(this.hopper);
	}

	@Override
	public void initialize() {
		hopper.setState(state);
	}
}