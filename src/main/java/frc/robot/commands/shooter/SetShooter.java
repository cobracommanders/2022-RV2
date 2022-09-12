package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.ShooterState;

public class SetShooter extends CommandBase {
	private final Shooter shooter;
	private final ShooterState state;

	public SetShooter(Shooter shooter, ShooterState state) {
		this.shooter = shooter;
		this.state = state;
		addRequirements(this.shooter);
	}

	@Override
	public void initialize() {
		shooter.setState(state);
	}

	@Override
	public void execute() {
	}

	@Override
	public void end(boolean interrupted) {
		shooter.setState(ShooterState.IDLE);
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}