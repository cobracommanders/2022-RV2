package frc.robot.commands.wrist;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Wrist;
import frc.robot.subsystems.Wrist.WristState;

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
