package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Shooter;

public class SetShooter extends SequentialCommandGroup {
	private final Shooter shooter;
	private final double speed;

	public SetShooter(Shooter shooter, double state) {
		this.shooter = shooter;
		this.speed = state;
		addRequirements(this.shooter);
		addCommands(
				new InstantCommand(() -> shooter.setSpeed(-1000)),
				new WaitCommand(0.5),
				new InstantCommand(() -> shooter.setSpeed(speed)));
	}
}