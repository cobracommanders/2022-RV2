package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.ShooterSetting;

public class ToggleShooter extends CommandBase {
	private final Shooter shooter;
	private final ShooterSetting setting;

	public ToggleShooter(Shooter shooter, ShooterSetting setting) {
		this.shooter = shooter;
		this.setting = setting;
		addRequirements(this.shooter);

	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public void initialize() {
		shooter.set(setting);
	}

	@Override
	public void end(boolean interrupted) {
		shooter.set(ShooterSetting.IDLE);
	}
}