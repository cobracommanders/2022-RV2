package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.ResetGyro;
import frc.robot.commands.drivetrain.LimelightAlign;
import frc.robot.commands.drivetrain.SetDrivetrain;
import frc.robot.commands.hood.CalibrateHood;
import frc.robot.commands.hopper.IntakeHopper;
import frc.robot.commands.hopper.SetHopper;
import frc.robot.commands.intake.SetIntake;
import frc.robot.commands.shooter.InterpolateShooter;
import frc.robot.commands.wrist.SetWrist;
import frc.robot.subsystems.Centerer;
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
	public StateChampsTwoBall(Drivetrain drivetrain, Hood hood, Shooter shooter, Hopper hopper, Intake intake, Wrist wrist, Centerer centerer, Limelight limelight) {
		InterpolateShooter shootCommand = new InterpolateShooter(shooter, hood);
		IntakeHopper hopperCommand = new IntakeHopper(hopper, shooter, centerer);

		addCommands(
				new ParallelCommandGroup(
						// Setup
						new SequentialCommandGroup(
								new CalibrateHood(hood),
								new ResetGyro(drivetrain)),

						// Drive
						new SequentialCommandGroup(
								new SetWrist(wrist, WristState.OUT),
								new SetIntake(intake, IntakeState.INTAKE),
								new InstantCommand(() -> hopperCommand.schedule()),
								new SetDrivetrain(drivetrain, 2, 0, 0),
								new WaitCommand(0), //TODO Find the right number for this
								new SetDrivetrain(drivetrain, 0, 0, 0),
								new SetIntake(intake, IntakeState.IDLE),
								new SetWrist(wrist, WristState.IN),
								new SetDrivetrain(drivetrain, 0, 0, 2), //TODO adjust speed
								new WaitCommand(0), //TODO Find the right number for this
								new LimelightAlign(drivetrain, limelight),
								new InstantCommand(() -> hopperCommand.cancel()),
								new InstantCommand(() -> shootCommand.schedule()),
								new WaitCommand(0), //TODO Find the right number for this

								new SetHopper(hopper, HopperSetting.LOAD),
								new WaitCommand(0.5),
								new SetHopper(hopper, HopperSetting.IDLE),
								new WaitCommand(1),
								new SetHopper(hopper, HopperSetting.LOAD)								
				)),

				new InstantCommand(() -> shootCommand.cancel()));
	}

}
