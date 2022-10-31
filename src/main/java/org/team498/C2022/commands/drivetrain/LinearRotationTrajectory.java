package org.team498.C2022.commands.drivetrain;

import org.team498.C2022.subsystems.Drivetrain;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class LinearRotationTrajectory extends CommandBase {
	private final Drivetrain drivetrain;
	private double dx;
	private double dy;
	private double dr;
	private double xDistance;
	private double yDistance;
	private double degrees;
	private double maxSpeed;
	private double rotError;
	private Timer timer = new Timer();
	private double distance;
	private double totalDistance;
	private double currAngle;
	private double angleOffset;
	private double t;
	private double dt;

	/**
	 * @apiNote all units in meters
	 * @param drivetrain drivetrain
	 * @param xDistance  magnitude of x direction
	 * @param yDistance  magnitude of y direction
	 * @param degrees    relative rotation setpoint in degrees
	 */
	public LinearRotationTrajectory(Drivetrain drivetrain, double xDistance, double yDistance, double degrees,
			double maxSpeed) {
		this.drivetrain = drivetrain;
		this.xDistance = xDistance;
		this.yDistance = yDistance;
		this.degrees = degrees;
		this.maxSpeed = maxSpeed;
		addRequirements(this.drivetrain);
	}

	@Override
	public void initialize() {
		timer.start();
		drivetrain.zeroGyro();
		totalDistance = Math.hypot(xDistance, yDistance);
		distance = 0;
		currAngle = 0;
		angleOffset = drivetrain.getYaw180();
		rotError = 2;
	}

	@Override
	public void execute() {
		t = timer.get();
		// if (distance < 1) {
		// 		dy = Math.copySign(distance, yDistance);
		// 		dx = Math.copySign(distance, xDistance);
		// } else if (distance > totalDistance - 1) {
		// 		dy = Math.copySign(totalDistance - distance, yDistance);
		// 		dx = Math.copySign(totalDistance - distance, xDistance);
		// } else {
		// 	dy = maxSpeed * yDistance / totalDistance;
		// 	dx = maxSpeed * xDistance / totalDistance;
		// }
		dy = maxSpeed * yDistance / totalDistance;
		dx = maxSpeed * xDistance / totalDistance;
		double angleError = degrees - currAngle;
		dr = angleError * 50.0 / 180.0;
		if (Math.abs(angleError) <= 8) {
			if (Math.abs(angleError) <= rotError) {
				dr = 0.0;
			} else {
				dr = Math.copySign(10, angleError);
			}
		}
		// dr = (currAngle >= degrees + rotError) ? -30 : ((currAngle <= degrees -
		// rotError) ? 30 : 0);

		drivetrain.drive(ChassisSpeeds.fromFieldRelativeSpeeds(dx, dy, Math.toRadians(dr), Rotation2d.fromDegrees(drivetrain.getYaw())));

		dt = timer.get() - t;
		distance = Math.hypot(dx / dt, dy / dt);
		currAngle = (drivetrain.getYaw180()) - angleOffset;
	}

	@Override
	public void end(boolean interrupted) {
		timer.stop();
		timer.reset();
		drivetrain.drive(new ChassisSpeeds());
	}

	@Override
	public boolean isFinished() {
		return timer.get() >= totalDistance / maxSpeed;//distance >= totalDistance;// && (currAngle >= degrees + rotError) && (currAngle <= degrees - rotError);
	}
}