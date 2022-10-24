package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.ResetGyro;
import frc.robot.commands.centerer.SetCenterer;
import frc.robot.commands.drivetrain.LimelightAlign;
import frc.robot.commands.drivetrain.RotatingLinearTrajectory;
import frc.robot.commands.hood.CalibrateHood;
import frc.robot.commands.hopper.IntakeHopper;
import frc.robot.commands.hopper.SetHopper;
import frc.robot.commands.intake.SetIntake;
import frc.robot.commands.shooter.InterpolateShooter;
import frc.robot.commands.wrist.SetWrist;
import frc.robot.subsystems.Centerer;
import frc.robot.subsystems.Centerer.CentererState;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Hood;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Hopper.HopperSetting;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.IntakeState;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Wrist;
import frc.robot.subsystems.Wrist.WristState;
import frc.util.Limelight;

public class StateChampsTwoBall extends SequentialCommandGroup {
	public StateChampsTwoBall(Drivetrain drivetrain, Hood hood, Shooter shooter, Hopper hopper, Intake intake,
			Wrist wrist, Centerer centerer, Limelight limelight) {
		InterpolateShooter shootCommand = new InterpolateShooter(shooter, hood);
		IntakeHopper hopperCommand = new IntakeHopper(hopper, shooter, centerer);

		addCommands(
				// Setup

				// Drive
				new SequentialCommandGroup(
						new CalibrateHood(hood),
						new ResetGyro(drivetrain)),
				new SetWrist(wrist, WristState.OUT),
				new SetIntake(intake, IntakeState.INTAKE),
				// new ParallelCommandGroup(new InstantCommand(() -> hopperCommand.schedule())),
				new SetCenterer(centerer, CentererState.CENTER),
				new WaitCommand(1),
				new RotatingLinearTrajectory(drivetrain, 1.5, 0, 0, 2),
				new SetCenterer(centerer, CentererState.IDLE),
				new SetIntake(intake, IntakeState.IDLE),
				new SetWrist(wrist, WristState.IN),
				new RotatingLinearTrajectory(drivetrain, -1, 0, 0, 2),
				new RotatingLinearTrajectory(drivetrain, 0, 0, 170, 0),
				new LimelightAlign(drivetrain, limelight),
				new ParallelDeadlineGroup(
						new SequentialCommandGroup(
								new WaitCommand(1),
								new SetHopper(hopper, HopperSetting.LOAD)),
						new InterpolateShooter(shooter, hood)),
				new ParallelDeadlineGroup(
						new SequentialCommandGroup(
								new WaitCommand(0.5),
								new SetHopper(hopper, HopperSetting.IDLE)),
						new InterpolateShooter(shooter, hood)),
				new ParallelDeadlineGroup(
						new SequentialCommandGroup(
								new WaitCommand(1),
								new SetHopper(hopper, HopperSetting.LOAD),
								new WaitCommand(5)),
						new InterpolateShooter(shooter, hood))

		);
	}

}
