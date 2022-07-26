package org.team498.C2022.commands.hopper;

import org.team498.C2022.subsystems.Hopper;
import org.team498.C2022.subsystems.Hopper.HopperSetting;

import edu.wpi.first.wpilibj2.command.CommandBase;

// Sets the hopper to a given state, then sets it to idle when the command ends
public class ToggleHopper extends CommandBase {
	private final Hopper hopper;
	private final HopperSetting state;

	public ToggleHopper(Hopper hopper, HopperSetting state) {
		this.hopper = hopper;
		this.state = state;
		addRequirements(this.hopper);
	}

	@Override
	public void initialize() {
		hopper.setState(state);
	}

	@Override
	public void end(boolean interupted) {
		hopper.setState(HopperSetting.IDLE);
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}