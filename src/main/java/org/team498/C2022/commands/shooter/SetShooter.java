package org.team498.C2022.commands.shooter;

import org.team498.C2022.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;

// Sets the shooter to a given state
public class SetShooter extends CommandBase {
	private final Shooter shooter;
	private double speed;

	public SetShooter(Shooter shooter, double speed) {
		this.shooter = shooter;
		this.speed = speed;
		addRequirements(this.shooter);
	}

	@Override
	public void execute() {
		shooter.set(speed);
	}

	@Override
	public void end(boolean interrupted) {
		shooter.set(0);
	}
}