package frc.robot.commands.hopper;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.shooter.SetShooter;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Hopper.HopperSetting;
import frc.robot.subsystems.Shooter.ShooterSetting;

public class EjectCargo extends SequentialCommandGroup {
	public EjectCargo(Hopper hopper, Shooter shooter) {
		addRequirements(hopper);
		addCommands(
				new SetHopper(hopper, HopperSetting.OUTTAKE),
				new WaitCommand(1),
				new SetShooter(shooter, ShooterSetting.REVERSE.RPM),
				new SetHopper(hopper, HopperSetting.REVERSE),
				new WaitCommand(0.3),
				new SetHopper(hopper, HopperSetting.IDLE),
				new SetShooter(shooter, ShooterSetting.IDLE.RPM));
	}
}