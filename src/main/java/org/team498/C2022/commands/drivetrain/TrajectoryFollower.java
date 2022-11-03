package org.team498.C2022.commands.drivetrain;

import static org.team498.C2022.Constants.kRoborioTrajectoryFilepath;

import org.team498.C2022.subsystems.Drivetrain;
import org.team498.lib.logging.CSVReader;
import org.team498.lib.util.DriveInterpolator;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class TrajectoryFollower extends CommandBase {
	private final Drivetrain drivetrain;
	private CSVReader reader;
	private final Timer timer;
	private final String filename;
	private DriveInterpolator interpolator;
	private double[][] trajectory;

	public TrajectoryFollower(Drivetrain drivetrain, String filename) {
		this.filename = filename;
		this.drivetrain = drivetrain;
		
		timer = new Timer();

		addRequirements(this.drivetrain);
	}

	@Override
	public void initialize() {
		timer.reset();
		timer.start();
		// Time, vx, vy, rotation, gyro
		reader = new CSVReader(kRoborioTrajectoryFilepath, filename);
		trajectory = getArray();
		interpolator = new DriveInterpolator(trajectory);
	}

	@Override
	public void execute() {
		double time = timer.get();

		double[] speeds = interpolator.getInterpolatedValue(time);

		drivetrain.drive(ChassisSpeeds.fromFieldRelativeSpeeds(
				speeds[0],
				speeds[1],
				speeds[2],
				Rotation2d.fromDegrees(drivetrain.getYaw180())));
	}

	@Override
	public void end(boolean interrupted) {
	}

	@Override
	public boolean isFinished() {
		return interpolator.isFinished();
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
