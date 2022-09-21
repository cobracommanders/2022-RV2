package frc.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Climber;

public class ZeroClimber extends InstantCommand {
	public ZeroClimber(Climber climber) {
		climber.resetEncoder();
	}
}
