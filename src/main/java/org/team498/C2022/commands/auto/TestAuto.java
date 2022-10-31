package org.team498.C2022.commands.auto;

import java.util.List;

import org.team498.C2022.subsystems.Drivetrain;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class TestAuto extends SequentialCommandGroup {
	public TestAuto(Drivetrain drivetrain) {
		super(
				new TrajectoryPlanner(drivetrain, testTrajectory));
	}

	// An example trajectory to follow. All units in meters.
	public static final Trajectory testTrajectory = TrajectoryGenerator.generateTrajectory(
			// Start at the origin facing the +X direction
			new Pose2d(0, 0, new Rotation2d(0)),
			// Pass through these two interior waypoints, making an 's' curve path
			List.of(
					new Translation2d(1, 1),
					new Translation2d(2, -1)),
			// End 3 meters straight ahead of where we started, facing forward
			new Pose2d(3, 0, new Rotation2d(0)),
			TrajectoryPlanner.config);
}
