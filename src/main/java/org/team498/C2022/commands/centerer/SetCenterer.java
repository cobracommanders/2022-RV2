package org.team498.C2022.commands.centerer;

import org.team498.C2022.subsystems.Centerer;
import org.team498.C2022.subsystems.Centerer.CentererState;

import edu.wpi.first.wpilibj2.command.CommandBase;

// Sets the centerer to a given state, then sets it to idle when the command ends
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
	public boolean isFinished() {
		return true;
	}
}