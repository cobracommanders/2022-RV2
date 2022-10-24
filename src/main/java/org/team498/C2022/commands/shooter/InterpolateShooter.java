package org.team498.C2022.commands.shooter;

import org.team498.C2022.commands.hood.SetHood;
import org.team498.C2022.subsystems.Hood;
import org.team498.C2022.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

// Sets the shooter to a given state
public class InterpolateShooter extends ParallelCommandGroup {
	public InterpolateShooter(Shooter shooter, Hood hood) {
		super(
			new SetShooter(shooter, shooter.getInterpolatedValue()),
			new SetHood(hood, hood.getInterpolatedValue())
		);
	}
}