package frc.robot.commands.auto;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.ResetGyro;
import frc.robot.commands.drivetrain.SetDrivetrain;
import frc.robot.commands.hood.CalibrateHood;
import frc.robot.commands.hood.SetHood;
import frc.robot.commands.hopper.SetHopper;
import frc.robot.commands.shooter.SetShooter;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Hood;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Hopper.HopperSetting;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.ShooterSetting;

public class SimpleTaxi extends SequentialCommandGroup {
	public SimpleTaxi(Drivetrain drivetrain, Hood hood, Shooter shooter, Hopper hopper) {
		addCommands(
				new ParallelCommandGroup(
						// Setup
						new SequentialCommandGroup(
								new CalibrateHood(hood),
								new ResetGyro(drivetrain),
								new SetShooter(shooter, ShooterSetting.TARMAC.RPM),
								new SetHood(hood, ShooterSetting.TARMAC.angle)),
						// Shoot
						new SequentialCommandGroup(
								new WaitCommand(SmartDashboard.getNumber("Auto Shoot Delay", 4)),
								new SetHopper(hopper, HopperSetting.LOAD)),

						// Drive
						new SequentialCommandGroup(
								new WaitCommand(SmartDashboard.getNumber("Auto Drive Delay", 8)),
								new SetDrivetrain(drivetrain, -2, 0, 0),
								new WaitCommand(1.5),
								new SetDrivetrain(drivetrain, 0, 0, 0))),

				new SetHopper(hopper, HopperSetting.IDLE),
				new SetShooter(shooter, ShooterSetting.IDLE.RPM));
	}
}