package org.team498.C2022.commands.auto;

import org.team498.C2022.commands.ResetGyro;
import org.team498.C2022.commands.centerer.SetCenterer;
import org.team498.C2022.commands.drivetrain.LimelightAlign;
import org.team498.C2022.commands.drivetrain.LinearRotationTrajectory;
import org.team498.C2022.commands.drivetrain.RotatingLinearTrajectory;
import org.team498.C2022.commands.drivetrain.TrajectoryFollower;
import org.team498.C2022.commands.hood.CalibrateHood;
import org.team498.C2022.commands.hopper.EjectCargo;
import org.team498.C2022.commands.hopper.IntakeHopper;
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

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class TwoBallARL extends SequentialCommandGroup {
	public TwoBallARL(Drivetrain drivetrain, Hood hood, Shooter shooter, Hopper hopper, Intake intake,
			Wrist wrist, Centerer centerer, Limelight limelight) {
		addCommands(
			new ParallelDeadlineGroup(
                new SequentialCommandGroup(
                    new InstantCommand(()-> drivetrain.zeroGyro(), drivetrain),
                    new SetWrist(wrist, WristState.OUT),
                    new SetIntake(intake, IntakeState.INTAKE),
                    new SetCenterer(centerer, CentererState.CENTER),
                    new TrajectoryFollower(drivetrain, "auto_1_leg_1"),
                    new WaitCommand(.5),
                    new ParallelCommandGroup(
                        new SetWrist(wrist, WristState.IN),
                        new SetIntake(intake, IntakeState.IDLE)
                    )
                )
                //new IntakeHopper(hopper, centerer)
            ),
            new SaveCargoHigh(hopper),
                new ParallelDeadlineGroup(
                    new SequentialCommandGroup(
                        new ParallelDeadlineGroup(
                            new WaitCommand(1.4),
                            new LimelightAlign(drivetrain, limelight)
                        ),
                        new SaveCargoHigh(hopper),
                        new SetHopper(hopper, HopperSetting.IDLE),
                        new WaitCommand(0.5),
                        new SetHopper(hopper, HopperSetting.LOAD),
                        new WaitCommand(3.5),
                        new SetHopper(hopper, HopperSetting.IDLE)
                    ),
                    new InterpolateShooter(shooter, hood)
                ),
                new ParallelDeadlineGroup (
                    new SequentialCommandGroup(
                        new ParallelCommandGroup(
                            new SetWrist(wrist, WristState.OUT),
                            new SetIntake(intake, IntakeState.INTAKE),
                            new SetCenterer(centerer, CentererState.CENTER)
                        ),
                        new InstantCommand(()-> drivetrain.zeroGyro(), drivetrain),

                        new TrajectoryFollower(drivetrain, "trajectory4"),
                        new WaitCommand(2)
                    ),
                    new EjectCargo(hopper)
                ),
                new EjectCargo(hopper),
                new ParallelCommandGroup(
                    new SetWrist(wrist, WristState.IN),
                    new SetIntake(intake, IntakeState.IDLE),
                    new SetCenterer(centerer, CentererState.IDLE)
                )
        );
	}

}
