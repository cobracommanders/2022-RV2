package org.team498.C2022.commands.wrist;

import org.team498.C2022.subsystems.Wrist;
import org.team498.C2022.subsystems.Wrist.WristState;

import edu.wpi.first.wpilibj2.command.CommandBase;

// Sets the wrist to a given state
public class SetWrist extends CommandBase {
	private Wrist wrist;
	private WristState state;

	public SetWrist(Wrist wrist, WristState state) {
		this.wrist = wrist;
		this.state = state;
		addRequirements(this.wrist);
	}

	@Override
	public void initialize() {
		wrist.setState(state);
	}

	@Override
	public boolean isFinished() {
		return true;
	}
}
