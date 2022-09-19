package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class ResetGyro extends CommandBase {
	private Drivetrain drivetrain;

	public ResetGyro(Drivetrain drivetrain) {
		this.drivetrain = drivetrain;
	}

	@Override
	public void initialize() {
		drivetrain.zeroGyro();
	}

	@Override
	public boolean isFinished() {
		return true;
	}
}
