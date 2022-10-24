package org.team498.C2022.commands.drivetrain;

import org.team498.C2022.subsystems.Drivetrain;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
// import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class LinearTrajectory extends CommandBase {
	private final Drivetrain drivetrain;
	// private double t;
	// private double x;
	// private double y;
	// private double r;
	// private double t2;
	private double dx;
	private double dy;
	private double dr;
	// private double dt;
	private double xScalar;
	private double yScalar;
	private double rps;
	private Timer timer = new Timer();
	private double seconds;
	private double distance;
	private double totalDistance;

	/**
	 * @apiNote all units in meters
	 * @param drivetrain drivetrain
	 * @param xScalar    magnitude of x direction
	 * @param yScalar    magnitude of y direction
	 * @param rps        rotations per second
	 */
	public LinearTrajectory(Drivetrain drivetrain, double xScalar, double yScalar, double rps, double seconds) {
		this.drivetrain = drivetrain;
		this.xScalar = xScalar;
		this.yScalar = yScalar;
		this.rps = rps;
		this.seconds = seconds;
		addRequirements(this.drivetrain);
	}

	@Override
	public void initialize() {
		timer.reset();
		timer.start();
	}

	private double getTimerCountdown() {
		return seconds - (((double) Math.round(timer.get() * 100)) / 100);
	}

	@Override
	public void execute() {
		double time = getTimerCountdown();
		// System.out.println(time * 10);

		drivetrain.drive(new ChassisSpeeds(
				time > Math.abs(xScalar) * 0.5 ? xScalar : xScalar * time,
				time > Math.abs(yScalar) * 0.5 ? yScalar : yScalar * time,
				time > Math.abs(rps) * 0.5 ? rps : rps * time));
	}

	@Override
	public boolean isFinished() {
		
		return timer.get() > seconds;
		
	}

	// @Override
	// public void initialize() {
	// // timer.start();
	// totalDistance = Math.hypot(xScalar, yScalar);
	// distance = 0;
	// }

	// @Override
	// public void execute() {
	// // t = timer.get();
	// // y = t;
	// // x = t;
	// // r = t;

	// if (distance < 1) {
	// 	  if (distance < .3) {
	// 	      dy = Math.copySign(.3, yScalar);
	// 		  dx = Math.copySign(.3, xScalar);
	// 	  } else {
	// 		  dy = Math.copySign(distance, yScalar);
	// 		  dx = Math.copySign(distance, xScalar);
	//    }
	//
	// } else if (distance > totalDistance - 1) {
	// 	   if (distance > totalDistance - .3) {
	// 		  dy = Math.copySign(.3, yScalar);
	// 		  dx = Math.copySign(.3, xScalar);
	// 	   } else {
	// 	 	  dy = Math.copySign(totalDistance - distance, yScalar);
	// 	      dx = Math.copySign(totalDistance - distance, xScalar);
	// 	   }
	//
	// } else {
	// 	  dy = 1.0 * yScalar;
	//    dx = 1.0 * xScalar;
	// }

	// dr = 2.0 * Math.PI * rps;

	// drivetrain.drive(new ChassisSpeeds(dx, dy, dr));
	// distance += Math.hypot(dx, dy);
	// // dt = timer.get() - t;
	// }

	// @Override
	// public void end(boolean interrupted) {
	// drivetrain.drive(new ChassisSpeeds());
	// }

	// @Override
	// public boolean isFinished() {
	// return distance >= totalDistance;
	// }
}