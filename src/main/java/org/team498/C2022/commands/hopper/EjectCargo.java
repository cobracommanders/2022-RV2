package org.team498.C2022.commands.hopper;

import org.team498.C2022.commands.shooter.SetShooter;
import org.team498.C2022.subsystems.Hopper;
import org.team498.C2022.subsystems.Shooter;
import org.team498.C2022.subsystems.Hopper.HopperSetting;
import org.team498.C2022.subsystems.Shooter.ShooterSetting;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

// Eject a cargo from the front of the hopper
public class EjectCargo extends SequentialCommandGroup {
	public EjectCargo(Hopper hopper) {
		addRequirements(hopper);
		addCommands(
				new SetHopper(hopper, HopperSetting.OUTTAKE),
				new WaitCommand(1),
				//new InstantCommand(() -> new SetShooter(shooter, ShooterSetting.REVERSE.RPM)),
				new SetHopper(hopper, HopperSetting.REVERSE),
				new WaitCommand(0.3),
				new SetHopper(hopper, HopperSetting.IDLE));
				//new InstantCommand(() -> new SetShooter(shooter, ShooterSetting.IDLE.RPM)));
	}
}