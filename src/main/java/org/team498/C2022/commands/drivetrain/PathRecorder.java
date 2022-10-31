package org.team498.C2022.commands.drivetrain;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import org.team498.C2022.subsystems.Drivetrain;
import org.team498.lib.logging.CSVWriter;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

// Controls the robot in fiels oriented mode
public class PathRecorder extends CommandBase {
	private final Drivetrain drivetrainSubsystem;

	private final DoubleSupplier translationXSupplier;
	private final DoubleSupplier translationYSupplier;
	private final DoubleSupplier rotationSupplier;
	private final double deadzone;
	private final BooleanSupplier slowDrive;
	private final CSVWriter recorder;
	private final Timer timer;
	private boolean started;

	public PathRecorder(Drivetrain drivetrainSubsystem,
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

		started = false;

		recorder = new CSVWriter("", "testlog"); // TODO name
		timer = new Timer();

		addRequirements(this.drivetrainSubsystem);
	}

	@Override
	public void initialize() {
		recorder.open();

		recorder.write(
				"Time",
				"Target X velocity",
				"Target Y velocity",
				"Target rotation speed",
				"Gyro angle");
	}

	public void start() {
		timer.reset();
		timer.start();
		started = true;
	}

	public void end() {
		timer.stop();
		started = false;
	}

	@Override
	public void execute() {
		if (!started)
			return;

		double driveSpeed = slowDrive.getAsBoolean() ? 1 : 2;
		double xTranslation = translationXSupplier.getAsDouble();
		double yTranslation = translationYSupplier.getAsDouble();
		double rotation = rotationSupplier.getAsDouble();

		double vx = deadzone(((xTranslation * driveSpeed) * (xTranslation * driveSpeed)) * xTranslation, deadzone);
		double vy = deadzone(((yTranslation * driveSpeed) * (yTranslation * driveSpeed)) * yTranslation, deadzone);
		double radiansPerSecond = deadzone(
				((rotation * (driveSpeed + (driveSpeed * 0.5)))
						* (rotation * (driveSpeed + (driveSpeed * 0.5)))) * rotation,
				deadzone);

		drivetrainSubsystem.drive(
				ChassisSpeeds.fromFieldRelativeSpeeds(
						vx,
						vy,
						radiansPerSecond,
						Rotation2d.fromDegrees(drivetrainSubsystem.getYaw180())));

		recorder.write(
				String.valueOf(timer.get()),
				String.valueOf(vx),
				String.valueOf(vy),
				String.valueOf(radiansPerSecond),
				String.valueOf(drivetrainSubsystem.getYaw180()));

	}

	private double deadzone(double input, double deadzone) {
		if (Math.abs(input) > deadzone)
			return input;
		return 0;
	}

	@Override
	public void end(boolean interrupted) {
		recorder.close();
		timer.stop();
	}
}