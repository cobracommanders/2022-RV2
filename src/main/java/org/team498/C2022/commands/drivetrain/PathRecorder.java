package org.team498.C2022.commands.drivetrain;

import static org.team498.C2022.Constants.kRoborioTrajectoryFilepath;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import org.team498.C2022.subsystems.Drivetrain;
import org.team498.lib.logging.CSVWriter;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

// Controls the robot in fiels oriented mode
public class PathRecorder extends CommandBase {
	private final Drivetrain drivetrainSubsystem;

	private final DoubleSupplier translationXSupplier;
	private final DoubleSupplier translationYSupplier;
	private final DoubleSupplier rotationSupplier;
	private final double deadzone;
	private final BooleanSupplier slowDrive;
	private CSVWriter recorder;
	private final Timer timer;

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

		timer = new Timer();

		addRequirements(this.drivetrainSubsystem);
	}

	@Override
	public void initialize() {
		SmartDashboard.putString("Trajectory name", "trajectory1");
		SmartDashboard.putBoolean("recording auto", timerStarted);
	}

	private boolean timerStarted = false;

	@Override
	public void execute() {

		double driveSpeed = slowDrive.getAsBoolean() ? 1 : 2;
		double xTranslation = translationXSupplier.getAsDouble();
		double yTranslation = translationYSupplier.getAsDouble();
		double rotation = rotationSupplier.getAsDouble();

		double vx = deadzone(((xTranslation * driveSpeed) * (xTranslation * driveSpeed)) * xTranslation, deadzone);
		double vy = deadzone(((yTranslation * driveSpeed) * (yTranslation * driveSpeed)) * yTranslation, deadzone);
		double radiansPerSecond = deadzone(
				((rotation * (driveSpeed + (driveSpeed * 0.5))) * (rotation * (driveSpeed + (driveSpeed * 0.5))))
						* rotation,
				deadzone);

		if (!timerStarted && (vx != 0 || vy != 0 || radiansPerSecond != 0)) {
			timerStarted = true;
			SmartDashboard.putBoolean("recording auto", timerStarted);
			recorder = new CSVWriter(kRoborioTrajectoryFilepath,
					SmartDashboard.getString("Trajectory name", "trajectory1"));
			recorder.open();

			timer.reset();
			timer.start();
		}

		drivetrainSubsystem.drive(
				ChassisSpeeds.fromFieldRelativeSpeeds(
						vx,
						vy,
						radiansPerSecond,
						Rotation2d.fromDegrees(drivetrainSubsystem.getYaw180())));

		if (timerStarted) {
			recorder.write(
					String.valueOf(timer.get()),
					String.valueOf(vx),
					String.valueOf(vy),
					String.valueOf(radiansPerSecond));
		}
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
		timerStarted = false;
		SmartDashboard.putBoolean("recording auto", timerStarted);
	}
}