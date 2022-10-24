package org.team498.C2022.commands;

import org.team498.C2022.subsystems.Drivetrain;

import edu.wpi.first.wpilibj2.command.InstantCommand;

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
