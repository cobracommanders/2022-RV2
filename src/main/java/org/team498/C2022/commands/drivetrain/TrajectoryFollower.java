package org.team498.C2022.commands.drivetrain;

import org.team498.C2022.subsystems.Drivetrain;
import org.team498.lib.logging.CSVReader;
import org.team498.lib.util.LinearInterpolator2;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class TrajectoryFollower extends CommandBase {
	private final Drivetrain drivetrain;
	private final CSVReader reader;
	private final Timer timer;
	private final LinearInterpolator2 vxInterpolator;
	private final LinearInterpolator2 vyInterpolator;
	private final LinearInterpolator2 rotationInterpolator;
	private final double[][] trajectory;

	public TrajectoryFollower(Drivetrain drivetrain) {
		this.drivetrain = drivetrain;
		reader = new CSVReader("", "testlog");
		timer = new Timer();

		trajectory = getArray();
		// Time, vx, vy, rotation, gyro
		vxInterpolator = new LinearInterpolator2(trajectory, 1);
		vyInterpolator = new LinearInterpolator2(trajectory, 2);
		rotationInterpolator = new LinearInterpolator2(trajectory, 3);

		addRequirements(this.drivetrain);
	}

	@Override
	public void initialize() {
		timer.reset();
		timer.start();
	}

	@Override
	public void execute() {
		double time = timer.get();
		double vx = vxInterpolator.getInterpolatedValue(time);
		double vy = vyInterpolator.getInterpolatedValue(time);
		double rotation = rotationInterpolator.getInterpolatedValue(time);

		drivetrain.drive(ChassisSpeeds.fromFieldRelativeSpeeds(
				vx,
				vy,
				rotation,
				Rotation2d.fromDegrees(drivetrain.getYaw180())));
	}

	@Override
	public void end(boolean interrupted) {
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	private double[][] getArray() {
		reader.open();

		int lines = 0;
		while (reader.hasNextLine()) {
			reader.nextLine();
			lines++;
		}
		reader.close();

		double[][] converted = new double[lines][];
		reader.open();
		for (int i = 0; i < converted.length; i++) {
			String[] nextLine = reader.nextLine();
			double[] newLine = new double[nextLine.length];

			for (int k = 0; k < nextLine.length; k++) {
				newLine[k] = Double.valueOf(nextLine[k]);
			}
			converted[i] = newLine;
		}

		return converted;
	}
}
