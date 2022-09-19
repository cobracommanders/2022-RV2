package frc.robot.commands.hopper;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Hopper.HopperSetting;

public class ToggleDisabledHopper extends CommandBase {
	private Hopper hopper;

	public ToggleDisabledHopper(Hopper hopper) {
		this.hopper = hopper;
		addRequirements(this.hopper);
	}

	@Override
	public void initialize() {
		hopper.setAutoControl(!hopper.getAutoEnabled());
		if (hopper.getAutoEnabled()) {
			hopper.setDefaultCommand(new AutoHopper(hopper, Robot.robotContainer.getIntakeButton()));
		} else {
			hopper.setDefaultCommand(new SetHopperManual(hopper, HopperSetting.IDLE, 0));
		}
	}

	@Override
	public boolean isFinished() {
		return true;
	}
}
