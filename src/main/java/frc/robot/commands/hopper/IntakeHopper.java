package frc.robot.commands.hopper;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Hopper.HopperSetting;

public class IntakeHopper extends CommandBase {
	private Hopper hopper;

	public IntakeHopper(Hopper hopper) {
		this.hopper = hopper;
		addRequirements(this.hopper);
	}

	@Override
	public void execute() {
		hopper.setState(HopperSetting.OUTTAKE, 0.19, 0.19);
	}

	@Override
	public void end(boolean interrupted) {
		hopper.setState(HopperSetting.IDLE, 0, 0);
	}
}