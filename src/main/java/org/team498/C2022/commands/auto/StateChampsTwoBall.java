package org.team498.C2022.commands.auto;

import org.team498.C2022.commands.ResetGyro;
import org.team498.C2022.commands.centerer.SetCenterer;
import org.team498.C2022.commands.drivetrain.LimelightAlign;
import org.team498.C2022.commands.drivetrain.RotatingLinearTrajectory;
import org.team498.C2022.commands.hood.CalibrateHood;
import org.team498.C2022.commands.hopper.IntakeHopper;
import org.team498.C2022.commands.hopper.SetHopper;
import org.team498.C2022.commands.intake.SetIntake;
import org.team498.C2022.commands.shooter.InterpolateShooter;
import org.team498.C2022.commands.wrist.SetWrist;
import org.team498.C2022.subsystems.Centerer;
import org.team498.C2022.subsystems.Drivetrain;
import org.team498.C2022.subsystems.Hood;
import org.team498.C2022.subsystems.Hopper;
import org.team498.C2022.subsystems.Intake;
import org.team498.C2022.subsystems.Shooter;
import org.team498.C2022.subsystems.Wrist;
import org.team498.C2022.subsystems.Centerer.CentererState;
import org.team498.C2022.subsystems.Hopper.HopperSetting;
import org.team498.C2022.subsystems.Intake.IntakeState;
import org.team498.C2022.subsystems.Wrist.WristState;
import org.team498.C2022.util.Limelight;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

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
