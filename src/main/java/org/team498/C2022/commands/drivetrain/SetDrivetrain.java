package org.team498.C2022.commands.drivetrain;

import org.team498.C2022.subsystems.Drivetrain;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.CommandBase;

// Sets the drivetrain to given velocities
public class SetDrivetrain extends CommandBase {
	private final Drivetrain drivetrainSubsystem;

	private double translationX;
	private double translationY;
	private double rotation;

	public SetDrivetrain(Drivetrain drivetrainSubsystem,
			double translationX,
			double translationY,
			double rotation) {
		this.drivetrainSubsystem = drivetrainSubsystem;
		this.translationX = translationX;
		this.translationY = translationY;
		this.rotation = rotation;

		addRequirements(drivetrainSubsystem);
	}

	@Override
	public void execute() {
		drivetrainSubsystem.drive(
				new ChassisSpeeds(translationX, translationY, rotation));
	}

	@Override
	public void end(boolean interrupted) {
	}

	@Override
	public boolean isFinished() {
		return true;
	}
}