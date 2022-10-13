package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.ShooterSetting;

// Sets the shooter to a given state
public class SetShooter extends InstantCommand {
	private final Shooter shooter;
	private final ShooterSetting setting;

	public SetShooter(Shooter shooter, ShooterSetting setting) {
		this.shooter = shooter;
		this.setting = setting;
		addRequirements(this.shooter);
	}

	@Override
	public void initialize() {
		shooter.set(setting);
	}
}