package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.ShooterSetting;

public class ToggleShooter extends CommandBase {
	private final Shooter shooter;
	private final double speed;

	public ToggleShooter(Shooter shooter, double state) {
		this.shooter = shooter;
		this.speed = state;
		addRequirements(this.shooter);

	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public void initialize() {
		shooter.setSpeed(speed);
	}

	@Override
	public void end(boolean interrupted) {
		shooter.setSpeed(ShooterSetting.IDLE.RPM);
	}
}