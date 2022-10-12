package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.IntakeState;

public class ToggleIntake extends CommandBase {
	private final Intake intake;
	private final IntakeState state;

	public ToggleIntake(Intake intake, IntakeState state) {
		this.intake = intake;
		this.state = state;
		addRequirements(this.intake);
	}

	@Override
	public void initialize() {
		intake.setState(state);
		intake.getCounter().add();
		Robot.logger.log("Set intake");
	}

	@Override
	public void end(boolean interrupted) {
		intake.setState(IntakeState.IDLE);
	}
}