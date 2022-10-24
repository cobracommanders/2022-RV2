package org.team498.C2022.commands.intake;

import org.team498.C2022.subsystems.Intake;
import org.team498.C2022.subsystems.Intake.IntakeState;

import edu.wpi.first.wpilibj2.command.CommandBase;

// Sets the intake to a given state, then sets it to idle when the command ends
public class SetIntake extends CommandBase {
	private final Intake intake;
	private final IntakeState state;

	public SetIntake(Intake intake, IntakeState state) {
		this.intake = intake;
		this.state = state;
		addRequirements(this.intake);
	}

	@Override
	public void initialize() {
		intake.setState(state);
	}

	@Override
	public boolean isFinished() {
		return true;
	}
}