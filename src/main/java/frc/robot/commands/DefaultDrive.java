package frc.robot.commands;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

import java.util.function.DoubleSupplier;

public class DefaultDrive extends CommandBase {
	private final Drivetrain drivetrainSubsystem;

	private final DoubleSupplier translationXSupplier;
	private final DoubleSupplier translationYSupplier;
	private final DoubleSupplier rotationSupplier;

	public DefaultDrive(Drivetrain drivetrainSubsystem,
			DoubleSupplier translationXSupplier,
			DoubleSupplier translationYSupplier,
			DoubleSupplier rotationSupplier) {
		this.drivetrainSubsystem = drivetrainSubsystem;
		this.translationXSupplier = translationXSupplier;
		this.translationYSupplier = translationYSupplier;
		this.rotationSupplier = rotationSupplier;

		addRequirements(drivetrainSubsystem);
	}

	@Override
	public void execute() {
		// You can use `new ChassisSpeeds(...)` for robot-oriented movement instead of
		// field-oriented movement
		drivetrainSubsystem.drive(
				ChassisSpeeds.fromFieldRelativeSpeeds(
						translationXSupplier.getAsDouble(),
						translationYSupplier.getAsDouble(),
						rotationSupplier.getAsDouble(),
						drivetrainSubsystem.getGyro()));
	}

	@Override
	public void end(boolean interrupted) {
		drivetrainSubsystem.drive(new ChassisSpeeds(0.0, 0.0, 0.0));
	}
}