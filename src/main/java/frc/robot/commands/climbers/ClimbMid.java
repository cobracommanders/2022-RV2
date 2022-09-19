package frc.robot.commands.climbers;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climbers;

public class ClimbMid extends CommandBase {
	private Climbers climbers;

	public ClimbMid(Climbers climbers) {
		this.climbers = climbers;
		addRequirements(this.climbers);
	}

	@Override
	public void initialize() {
		climbers.setSpeedLeft(1);
		climbers.setSpeedRight(1);
	}

	@Override
	public void end(boolean interrupted) {
		climbers.setSpeedLeft(0);
		climbers.setSpeedRight(0);
	}

	@Override
	public boolean isFinished() {
		return climbers.getEncoder() < 0;
	}
}
