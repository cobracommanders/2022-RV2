package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.hood.SetHood;
import frc.robot.subsystems.Hood;
import frc.robot.subsystems.Shooter;

// Sets the shooter to a given state
public class InterpolateShooter extends ParallelCommandGroup {
	public InterpolateShooter(Shooter shooter, Hood hood) {
		super(
			new SetShooter(shooter, shooter.getInterpolatedValue()),
			new SetHood(hood, hood.getInterpolatedValue())
		);
	}
}