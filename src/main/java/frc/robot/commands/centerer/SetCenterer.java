package frc.robot.commands.centerer;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Centerer;
import frc.robot.subsystems.Centerer.CentererState;

public class SetCenterer extends CommandBase {
	private final Centerer centerer;
	private final CentererState state;

	public SetCenterer(Centerer centerer, CentererState state) {
		this.centerer = centerer;
		this.state = state;
		addRequirements(this.centerer);
	}

	@Override
	public void initialize() {
		centerer.setState(state);
	}

	@Override
	public void end(boolean interrupted) {
		centerer.setState(CentererState.IDLE);
	}
}