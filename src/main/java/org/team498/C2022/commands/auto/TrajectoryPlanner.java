package org.team498.C2022.commands.auto;

import static org.team498.C2022.Constants.AutoConstants.kPThetaController;
import static org.team498.C2022.Constants.AutoConstants.kPXController;
import static org.team498.C2022.Constants.AutoConstants.*;

import org.team498.C2022.subsystems.Drivetrain;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;

public class TrajectoryPlanner extends SequentialCommandGroup {

	public static final TrajectoryConfig config = new TrajectoryConfig(
			kMaxSpeedMetersPerSecond,
			kMaxAccelerationMetersPerSecondSquared)
					.setKinematics(Drivetrain.getKinematics());

	// Constraint for the motion profilied robot angle controller
	public TrajectoryPlanner(Drivetrain drivetrain, Trajectory trajectory) {
		final TrapezoidProfile.Constraints kThetaControllerConstraints = new TrapezoidProfile.Constraints(
				kMaxAngularSpeedRadiansPerSecond, kMaxAngularSpeedRadiansPerSecondSquared);

		final ProfiledPIDController thetaController = new ProfiledPIDController(kPThetaController, 0, 0,
				kThetaControllerConstraints);
		thetaController.enableContinuousInput(-Math.PI, Math.PI);

		SwerveControllerCommand swerveControllerCommand = new SwerveControllerCommand(
				trajectory,
				drivetrain::getPose,
				Drivetrain.getKinematics(),
				new PIDController(kPXController, 0, 0),
				new PIDController(kPYController, 0, 0),
				thetaController,
				drivetrain::setModuleStates,
				drivetrain);

		addCommands(
				new InstantCommand(() -> drivetrain.resetOdometry(trajectory.getInitialPose())),
				swerveControllerCommand);
	}
}