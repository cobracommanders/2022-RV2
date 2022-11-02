package org.team498.C2022.commands.auto;

import org.team498.C2022.subsystems.Drivetrain;
import org.team498.lib.util.DriveInterpolator;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class TableFollower extends CommandBase {
	private Drivetrain drivetrain;
	private Timer timer = new Timer();
	private DriveInterpolator interpolator;
	private double time = 0;

	private static double deadzone = 0.1;
	private double driveSpeed = 2;

	public TableFollower(Drivetrain drivetrain, double[][] trajectory) {
		this.drivetrain = drivetrain;
		interpolator = new DriveInterpolator(trajectory);
		addRequirements(this.drivetrain);
	}

	@Override
	public void initialize() {
		timer.reset();
		timer.start();
	}

	@Override
	public void execute() {
		time = timer.get();
		double[] state = interpolator.getInterpolatedValue(time);

		drivetrain.drive(
				ChassisSpeeds.fromFieldRelativeSpeeds(
						deadzone(((state[0] * driveSpeed) * (state[0] * driveSpeed)) * state[0], deadzone),
						deadzone(((state[1] * driveSpeed) * (state[1] * driveSpeed)) * state[1], deadzone),
						deadzone(((state[2] * driveSpeed) * (state[2] * driveSpeed)) * state[2], deadzone),
						drivetrain.getGyroAngle()));
	}

	@Override
	public void end(boolean interrupted) {
		drivetrain.drive(new ChassisSpeeds(0, 0, 0));
		timer.stop();
		timer.reset();
	}

	@Override
	public boolean isFinished() {
		return interpolator.isFinished();
	}

	private double deadzone(double input, double deadzone) {
		if (Math.abs(input) > deadzone)
			return input;
		return 0;
	}
}
