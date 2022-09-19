package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Hood;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Hopper.HopperSetting;
import frc.robot.subsystems.Shooter.ShooterSettings;
import frc.robot.commands.ResetGyro;
import frc.robot.commands.drivetrain.SetSwerve;
import frc.robot.commands.hood.Calibrate;
import frc.robot.commands.hood.SetAngle;
import frc.robot.commands.hopper.SetHopperManual;
import frc.robot.commands.shooter.SetShooter;

public class SimpleTaxi extends SequentialCommandGroup {
	public SimpleTaxi(Drivetrain drivetrain, Hood hood, Shooter shooter, Hopper hopper) {
		addCommands(
				new ResetGyro(drivetrain),
				new Calibrate(hood),
				new InstantCommand(() -> new SetShooter(shooter, ShooterSettings.TARMAC.RPM).schedule(false)),
				new InstantCommand(() -> new SetAngle(hood, ShooterSettings.TARMAC.angle).schedule(false)),
				new WaitCommand(5),
				new InstantCommand(() -> new SetHopperManual(hopper, HopperSetting.LOAD, 0.3).schedule(false)),
				new WaitCommand(3),
				new SetSwerve(drivetrain, -2, 0, 0),
				new WaitCommand(1.5),
				new SetSwerve(drivetrain, 0, 0, 0),
				new InstantCommand(() -> new SetHopperManual(hopper, HopperSetting.IDLE, 0).schedule(false)),
				new InstantCommand(() -> new SetShooter(shooter, ShooterSettings.IDLE.RPM).schedule(false)));
	}
}