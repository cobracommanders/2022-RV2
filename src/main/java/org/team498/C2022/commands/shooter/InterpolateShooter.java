package org.team498.C2022.commands.shooter;

import org.team498.C2022.subsystems.Hood;
import org.team498.C2022.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;

// Sets the shooter to a given state
public class InterpolateShooter extends CommandBase {
	private Hood hood;
	private Shooter shooter;

	public InterpolateShooter(Shooter shooter, Hood hood) {
		this.hood = hood;
		this.shooter = shooter;

	}

	@Override
	public void execute() {
		shooter.set(shooter.getInterpolatedValue());
		hood.setAngle(hood.getInterpolatedValue());
	}

	@Override
	public void end(boolean interrupted) {
		shooter.set(0);
		
	}
}