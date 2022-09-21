package frc.robot.commands.hopper;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Hopper.HopperSetting;

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