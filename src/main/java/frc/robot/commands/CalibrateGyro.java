package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Drivetrain;

// Calibrates the gyro sensor
public class CalibrateGyro extends InstantCommand {
	private Drivetrain drivetrain;

	public CalibrateGyro(Drivetrain drivetrain) {
		this.drivetrain = drivetrain;
	}

	@Override
	public void initialize() {
		drivetrain.calibrateGyro();
	}
}
