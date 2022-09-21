package frc.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

public class RaiseClimber extends CommandBase {
	private Climber climber;

	public RaiseClimber(Climber climber) {
		this.climber = climber;
		addRequirements(this.climber);
	}

	@Override
	public void initialize() {
		climber.setSpeedLeft(1);
		climber.setSpeedRight(1);
	}

	@Override
	public void end(boolean interrupted) {
		climber.setSpeedLeft(0);
		climber.setSpeedRight(0);
	}

	@Override
	public boolean isFinished() {
		return climber.getEncoder() < 0;
	}
}
