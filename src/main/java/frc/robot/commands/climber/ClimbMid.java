package frc.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.hood.CalibrateHood;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Hood;

public class ClimbMid extends SequentialCommandGroup {
	public ClimbMid(Climber climber, Hood hood) {
		addCommands(
				new ParallelCommandGroup(
						// Raise the climbers
						new RaiseClimber(climber),
						// Fully lower the hood so it doesn't collide with the bars
						new CalibrateHood(hood)));
	}
}
