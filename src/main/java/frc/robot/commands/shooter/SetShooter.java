package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Shooter;

public class SetShooter extends InstantCommand {
	private final Shooter shooter;
	private final double speed;

	public SetShooter(Shooter shooter, double state) {
		this.shooter = shooter;
		this.speed = state;
		addRequirements(this.shooter);
	}

	@Override
	public void initialize() {
		shooter.setSpeed(speed);
	}
}