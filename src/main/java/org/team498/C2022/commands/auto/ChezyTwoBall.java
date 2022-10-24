package org.team498.C2022.commands.auto;

import org.team498.C2022.commands.ResetGyro;
import org.team498.C2022.commands.drivetrain.SetDrivetrain;
import org.team498.C2022.commands.hood.CalibrateHood;
import org.team498.C2022.commands.hood.SetHood;
import org.team498.C2022.commands.hopper.SetHopper;
import org.team498.C2022.commands.shooter.SetShooter;
import org.team498.C2022.subsystems.Drivetrain;
import org.team498.C2022.subsystems.Hood;
import org.team498.C2022.subsystems.Hopper;
import org.team498.C2022.subsystems.Shooter;
import org.team498.C2022.subsystems.Hopper.HopperSetting;
import org.team498.C2022.subsystems.Shooter.ShooterSetting;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

// Scores two cargo in the upper hub from the fender (only at chezy champs)
public class ChezyTwoBall extends SequentialCommandGroup {
	public ChezyTwoBall(Drivetrain drivetrain, Hood hood, Shooter shooter, Hopper hopper) {
		addCommands(
				new ParallelCommandGroup(
						// Setup
						new SequentialCommandGroup(
								new CalibrateHood(hood),
								new ResetGyro(drivetrain),
								new SetShooter(shooter, ShooterSetting.FENDER.RPM),
								new SetHood(hood, ShooterSetting.FENDER.angle)),
						// Shoot
						new SequentialCommandGroup(
								new WaitCommand(SmartDashboard.getNumber("Auto Shoot Delay", 4)),
								new SetHopper(hopper, HopperSetting.LOAD),
								new WaitCommand(0.5),
								new SetHopper(hopper, HopperSetting.IDLE),
								new WaitCommand(1),
								new SetHopper(hopper, HopperSetting.LOAD)),

						// Drive
						new SequentialCommandGroup(
								//new WaitCommand(SmartDashboard.getNumber("Auto Drive Delay", 8)),
								new WaitCommand(12),
								new SetDrivetrain(drivetrain, -2, 0, 0),
								new WaitCommand(1.5),
								new SetDrivetrain(drivetrain, 0, 0, 0))),

				new SetHopper(hopper, HopperSetting.IDLE),
				new SetShooter(shooter, ShooterSetting.IDLE.RPM));
	}
}