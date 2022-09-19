package frc.robot.commands.hopper;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Hopper.HopperSetting;

public class SetHopperManual extends CommandBase {
	private final Hopper hopper;
	private final HopperSetting state;
	private final double speed;

	public SetHopperManual(Hopper hopper, HopperSetting state, double speed) {
		this.hopper = hopper;
		this.state = state;
		this.speed = speed;
		addRequirements(this.hopper);
	}

	@Override
	public void initialize() {
		hopper.setState(state, speed, speed);
	}

	@Override
	public void execute() {
	}

	@Override
	public void end(boolean interrupted) {
		hopper.setState(HopperSetting.IDLE, 0, 0);
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}