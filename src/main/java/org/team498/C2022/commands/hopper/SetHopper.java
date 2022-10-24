package org.team498.C2022.commands.hopper;

import org.team498.C2022.subsystems.Hopper;
import org.team498.C2022.subsystems.Hopper.HopperSetting;

import edu.wpi.first.wpilibj2.command.InstantCommand;

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