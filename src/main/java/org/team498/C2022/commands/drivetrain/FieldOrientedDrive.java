package org.team498.C2022.commands.drivetrain;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import org.team498.C2022.subsystems.Drivetrain;

// Controls the robot in fiels oriented mode
public class FieldOrientedDrive extends CommandBase {
	private final Drivetrain drivetrainSubsystem;

	private final DoubleSupplier translationXSupplier;
	private final DoubleSupplier translationYSupplier;
	private final DoubleSupplier rotationSupplier;
	private final double deadzone;
	private final BooleanSupplier slowDrive;

	public FieldOrientedDrive(Drivetrain drivetrainSubsystem,
			DoubleSupplier translationXSupplier,
			DoubleSupplier translationYSupplier,
			DoubleSupplier rotationSupplier,
			double deadzone,
			BooleanSupplier slowDrive) {
		this.drivetrainSubsystem = drivetrainSubsystem;
		this.translationXSupplier = translationXSupplier;
		this.translationYSupplier = translationYSupplier;
		this.rotationSupplier = rotationSupplier;
		this.deadzone = deadzone;
		this.slowDrive = slowDrive;

		addRequirements(this.drivetrainSubsystem);
	}

	@Override
	public void initialize() {
		SmartDashboard.putBoolean("Robot Oriented", false);
	}

	@Override
	public void execute() {
		double driveSpeed = slowDrive.getAsBoolean() ? 1 : 2;
		double xTranslation = translationXSupplier.getAsDouble();
		double yTranslation = translationYSupplier.getAsDouble();
		double rotation = rotationSupplier.getAsDouble();

		drivetrainSubsystem.drive(
				ChassisSpeeds.fromFieldRelativeSpeeds(
						deadzone(((xTranslation * driveSpeed) * (xTranslation * driveSpeed)) * xTranslation, deadzone),
						deadzone(((yTranslation * driveSpeed) * (yTranslation * driveSpeed)) * yTranslation, deadzone),
						deadzone(((rotation * (driveSpeed + (driveSpeed * 0.5))) * (rotation * (driveSpeed + (driveSpeed * 0.5)))) * rotation, deadzone),
						Rotation2d.fromDegrees(drivetrainSubsystem.getYaw180())));
	}

	private double deadzone(double input, double deadzone) {
		if (Math.abs(input) > deadzone)
			return input;
		return 0;
	}

	@Override
	public void end(boolean interrupted) {
		drivetrainSubsystem.drive(new ChassisSpeeds(0.0, 0.0, 0.0));
		SmartDashboard.putBoolean("Robot Oriented", true);
	}
}