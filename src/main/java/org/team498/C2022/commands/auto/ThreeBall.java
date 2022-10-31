package org.team498.C2022.commands.auto;

import org.team498.C2022.commands.ResetGyro;
import org.team498.C2022.commands.centerer.SetCenterer;
import org.team498.C2022.commands.drivetrain.LimelightAlign;
import org.team498.C2022.commands.drivetrain.LinearRotationTrajectory;
import org.team498.C2022.commands.drivetrain.RotatingLinearTrajectory;
import org.team498.C2022.commands.hood.CalibrateHood;
import org.team498.C2022.commands.hopper.SaveCargoHigh;
import org.team498.C2022.commands.hopper.SetHopper;
import org.team498.C2022.commands.intake.SetIntake;
import org.team498.C2022.commands.shooter.InterpolateShooter;
import org.team498.C2022.commands.wrist.SetWrist;
import org.team498.C2022.subsystems.Centerer;
import org.team498.C2022.subsystems.Centerer.CentererState;
import org.team498.C2022.subsystems.Drivetrain;
import org.team498.C2022.subsystems.Hood;
import org.team498.C2022.subsystems.Hopper;
import org.team498.C2022.subsystems.Hopper.HopperSetting;
import org.team498.C2022.subsystems.Intake;
import org.team498.C2022.subsystems.Intake.IntakeState;
import org.team498.C2022.subsystems.Shooter;
import org.team498.C2022.subsystems.Wrist;
import org.team498.C2022.subsystems.Wrist.WristState;
import org.team498.lib.drivers.Limelight;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class ThreeBall extends SequentialCommandGroup {
	public ThreeBall(Drivetrain drivetrain, Hood hood, Shooter shooter, Hopper hopper, Intake intake,
			Wrist wrist, Centerer centerer, Limelight limelight) {

		addCommands(
				// Shoot ball 1
			new ParallelCommandGroup(
				new CalibrateHood(hood),
				new ResetGyro(drivetrain)),
			new ParallelDeadlineGroup(
				new WaitCommand(1.75), 
				new LimelightAlign(drivetrain, limelight),
				new InterpolateShooter(shooter, hood)),
			new ParallelDeadlineGroup(
				new SequentialCommandGroup(
					new WaitCommand(.3),
					new SetHopper(hopper, HopperSetting.LOAD),
					new WaitCommand(0.4),
					new SetHopper(hopper, HopperSetting.IDLE)
				),
				new InterpolateShooter(shooter, hood)
			),

			new RotatingLinearTrajectory(drivetrain, 0, 0, 170, 0),

			//travel to ball 2
			new ParallelDeadlineGroup(
				new WaitCommand(.1),
				new SetWrist(wrist, WristState.OUT),
				new SetIntake(intake, IntakeState.INTAKE),
				new SetCenterer(centerer, CentererState.CENTER)
				//new IntakeHopper(hopper, shooter, centerer)
			),
			new LinearRotationTrajectory(drivetrain, 1, 0, 0, 1),
			new ParallelDeadlineGroup(
				new WaitCommand(1.5), 
				new SaveCargoHigh(hopper)),
			//new SetCenterer(centerer, CentererState.IDLE),
			new RotatingLinearTrajectory(drivetrain, 0, 0, -95, 0),
			//Travel to ball 3
			new ParallelCommandGroup(
				new SetWrist(wrist, WristState.OUT),
				new SetIntake(intake, IntakeState.INTAKE),
				new SetCenterer(centerer, CentererState.CENTER)
				//new IntakeHopper(hopper, shooter, centerer)
			),
			new LinearRotationTrajectory(drivetrain, 3, -0.75, 0, 1.5),
			//new SetCenterer(centerer, CentererState.IDLE),
			new ParallelCommandGroup(
				new SetWrist(wrist, WristState.IN),
				new SetIntake(intake, IntakeState.IDLE)
			),

			new RotatingLinearTrajectory(drivetrain, 0, 0, -120, 0),
			//shoot balls
			new ParallelDeadlineGroup(
				new WaitCommand(1.5), 
				new LimelightAlign(drivetrain, limelight),
				new InterpolateShooter(shooter, hood)),
			new ParallelDeadlineGroup(
				new WaitCommand(0.2),
				new SetHopper(hopper, HopperSetting.LOAD),
				new InterpolateShooter(shooter, hood)
			),
			new ParallelDeadlineGroup(
				new WaitCommand(0.3),
				new SetHopper(hopper, HopperSetting.IDLE),
				new InterpolateShooter(shooter, hood)
			),
			new ParallelDeadlineGroup(
				new WaitCommand(3),
				new SetHopper(hopper, HopperSetting.LOAD),
				new InterpolateShooter(shooter, hood)
			),

			new SetCenterer(centerer, CentererState.IDLE),
			new SetHopper(hopper, HopperSetting.IDLE),
			new RotatingLinearTrajectory(drivetrain, 0, 0, 120, 0),
			new ParallelCommandGroup(
				new SetWrist(wrist, WristState.OUT),
				new SetIntake(intake, IntakeState.INTAKE),
				new SetCenterer(centerer, CentererState.CENTER)
				//new IntakeHopper(hopper, shooter, centerer)
			),
			new LinearRotationTrajectory(drivetrain, 6, -0.5, 0, 1.75),
			new SaveCargoHigh(hopper),
			new WaitCommand(1),
			new LinearRotationTrajectory(drivetrain, -6, 0.5, 0, 1.75),
			new RotatingLinearTrajectory(drivetrain, 0, 0, -120, 0),

			new ParallelDeadlineGroup(
				new SequentialCommandGroup(
					new ParallelDeadlineGroup(
						new WaitCommand(1.4), 
						new LimelightAlign(drivetrain, limelight)),
					new SetHopper(hopper, HopperSetting.LOAD),
					new WaitCommand(0.2),
					new SetHopper(hopper, HopperSetting.IDLE),
					new WaitCommand(0.3),
					new SetHopper(hopper, HopperSetting.LOAD),
					new WaitCommand(3),
					new SetHopper(hopper, HopperSetting.IDLE)
				),
				new InterpolateShooter(shooter, hood)
			)
		);
	}

}
