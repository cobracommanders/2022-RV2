package org.team498.C2022.commands.drivetrain;

import org.team498.C2022.subsystems.Drivetrain;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class XLock extends CommandBase {
	private final Drivetrain drivetrain;
	private final SwerveModuleState[] moduleStates = new SwerveModuleState[] {
			// Front left
			new SwerveModuleState(0, Rotation2d.fromDegrees(45)),
			// Front right
			new SwerveModuleState(0, Rotation2d.fromDegrees(-45)),
			// Back left
			new SwerveModuleState(0, Rotation2d.fromDegrees(-45)),
			// Back right
			new SwerveModuleState(0, Rotation2d.fromDegrees(45))
	};

	public XLock(Drivetrain drivetrain) {
		this.drivetrain = drivetrain;
		addRequirements(this.drivetrain);
	}

	@Override
	public void execute() {
		drivetrain.setModuleStates(moduleStates, true);
	}
}
