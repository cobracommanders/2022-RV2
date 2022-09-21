package frc.robot.commands.hopper;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Hopper;

public class ToggleAutoHopper extends InstantCommand {
	private Hopper hopper;

	public ToggleAutoHopper(Hopper hopper) {
		this.hopper = hopper;
	}

	@Override
	public void initialize() {
		hopper.setAutoControl(!hopper.getAutoEnabled());
		SmartDashboard.putBoolean("hopper enabled", hopper.getAutoEnabled());
	}
}
