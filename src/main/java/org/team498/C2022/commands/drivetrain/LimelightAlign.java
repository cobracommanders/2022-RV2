package org.team498.C2022.commands.drivetrain;

import org.team498.C2022.subsystems.Drivetrain;
import org.team498.C2022.util.Limelight;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class LimelightAlign extends CommandBase {
	private final Drivetrain drivetrain;
	private final Limelight limelight;

	/**
	 * Maximum speed of the robot while aligning in meters per second
	 */
	private final double kMaxSpeed = 3;
	private final double kAcceptedRange = 0.35;
	private final double kP;

	public LimelightAlign(Drivetrain drivetrain, Limelight limelight) {
		this.drivetrain = drivetrain;
		this.limelight = limelight;

		// Essentially means the robot will be at max speed on the edge of the
		// limelight's view of the target. Reducing the 29.8 will cause it to go faster
		// on the edges of the field of view
		kP = kMaxSpeed / 29.8;
	}

	@Override
	public void execute() {
		double rotationalSpeed = clampBelow(limelight.getTx() * kP, kMaxSpeed);
		drivetrain.drive(new ChassisSpeeds(0, 0, -rotationalSpeed + Math.copySign(0.3, -rotationalSpeed)));
	}

	@Override
	public boolean isFinished() {
		return Math.abs(limelight.getTx()) < kAcceptedRange;
	}

	@Override
	public void end(boolean interrupted) {
		drivetrain.drive(new ChassisSpeeds(0, 0, 0));
	}

	private double clampBelow(double input, double max) {
		if (input > max)
			return max;
		return input;
	}
}