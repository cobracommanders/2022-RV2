package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Drivetrain;

// Sets the gyro sensor angle to 0
public class ResetGyro extends InstantCommand {
	private Drivetrain drivetrain;

	public ResetGyro(Drivetrain drivetrain) {
		this.drivetrain = drivetrain;
	}

	@Override
	public void initialize() {
		drivetrain.zeroGyro();
	}
}
