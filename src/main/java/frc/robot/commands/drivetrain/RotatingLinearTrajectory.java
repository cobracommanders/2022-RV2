package frc.robot.commands.drivetrain;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class RotatingLinearTrajectory extends CommandBase {
	private final Drivetrain drivetrain;
	private double seconds;
	private double xScalar;
	private double yScalar;
	private double rps;

	private Timer timer = new Timer();

	private final double kSlowDownPrecentage = 0.5;

	private final double kMaxSpeed = 3;
	private final double kAcceptedRange = 2;
	private final double kP;

	/**
	 * @apiNote t is zero at x = 0 and y = 0
	 * @apiNote all units in meters
	 * @param drivetrain drivetrain
	 * @param xScalar    coefficient of x in parabolic equation
	 * @param yScalar    coefficient of y in parabolic equation
	 * @param rps        rotations per second
	 * @param seconds    initial value of t
	 */
	public RotatingLinearTrajectory(Drivetrain drivetrain, double xScalar, double yScalar, double rps, double seconds) {
		this.drivetrain = drivetrain;
		this.xScalar = xScalar;
		this.yScalar = yScalar;
		this.rps = rps;
		this.seconds = seconds;
		addRequirements(this.drivetrain);
		drivetrain.zeroGyro();

		kP = kMaxSpeed / 180;
	}

	@Override
	public void initialize() {
		timer.reset();
		timer.start();
	}

	@Override
	public void execute() {
		double time = getTimerCountdown();

		double rotationSpeed = (-drivetrain.IMUAngle() - rps) * -0.01666;

		drivetrain.drive(ChassisSpeeds.fromFieldRelativeSpeeds(
				time < Math.abs(xScalar) * kSlowDownPrecentage ? xScalar * time : xScalar,
				time < Math.abs(yScalar) * kSlowDownPrecentage ? yScalar * time : yScalar,
				clampBelow(rotationSpeed + Math.copySign(0.5, rotationSpeed), kMaxSpeed),
				drivetrain.getGyroAngle()));
	}

	@Override
	public void end(boolean interrupted) {
		drivetrain.drive(new ChassisSpeeds());
		timer.stop();
		timer.reset();
	}

	@Override
	public boolean isFinished() {
		return timer.get() > seconds && Math.abs(-drivetrain.IMUAngle() - rps) < kAcceptedRange;
	}

	private double getTimerCountdown() {
		// Gets the countdown of the timer reduced to two decimal places
		return seconds - (((double) Math.round(timer.get() * 100)) / 100);
	}

	private double clampBelow(double input, double max) {
		if (input > max)
			return max;
		return input;
	}
}