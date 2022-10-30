package org.team498.C2022.commands.hopper;

import org.team498.C2022.subsystems.Hopper;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;

// Toggles the status of the hopper, in case we need to disable the automatic sorting
public class ToggleAutoHopper extends InstantCommand {
	private Hopper hopper;

	public ToggleAutoHopper(Hopper hopper) {
		this.hopper = hopper;
	}

	@Override
	public void initialize() {
		hopper.getCurrentCommand().cancel();
		hopper.setAutoEnabled(!hopper.getAutoEnabled());
		SmartDashboard.putBoolean("hopper enabled", hopper.getAutoEnabled());
	}
}
