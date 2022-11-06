package org.team498.C2022.commands.drivetrain;

import org.team498.C2022.subsystems.Drivetrain;
import org.team498.lib.util.DriveInterpolator;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class TableFollower extends CommandBase {
	private Drivetrain drivetrain;
	private Timer timer = new Timer();
	private DriveInterpolator interpolator;
	private double time = 0;

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
                    state[1],
                    state[2],
                    state[3],
					Rotation2d.fromDegrees(drivetrain.getYaw180())
                )
        );
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
}
