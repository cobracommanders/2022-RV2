package org.team498.C2022.commands.drivetrain;

import org.team498.C2022.subsystems.Drivetrain;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class RotateToAngle extends CommandBase {
	private final Drivetrain drivetrain;
	private final double angle;
	private final double kP;
	private final double kMaxSpeed = 3;

	private double fakeAngle = 0;

	private double lastAngle = 0;

	public RotateToAngle(Drivetrain drivetrain, double angle) {
		this.drivetrain = drivetrain;
		this.angle = angle;

		kP = kMaxSpeed / 180;

		lastAngle = modifyRange(drivetrain.getYaw180());
	}

	public double modifyRange(double input) {
		if (input < 0) {
			input += 360;
		}
		return input;
	}

	@Override
	public void execute() {
		double currentAngle = modifyRange(drivetrain.getYaw180());
		fakeAngle += (currentAngle - lastAngle);
		lastAngle = currentAngle;

		double sign = Math.signum(fakeAngle);

		fakeAngle = modifyRange((Math.abs(fakeAngle) % 180) * sign);

		double setpoint = (angle - fakeAngle) * kP;
		drivetrain.drive(new ChassisSpeeds(0, 0, setpoint));
	}
}
