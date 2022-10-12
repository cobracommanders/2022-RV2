package frc.robot.commands.hopper;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Hopper.HopperSetting;

public class SaveCargoLow extends SequentialCommandGroup {

	public SaveCargoLow(Hopper hopper) {
		addRequirements(hopper);
		addCommands(
				new SetHopper(hopper, HopperSetting.MOVEFRONT),
				new WaitCommand(0.2),
				new SetHopper(hopper, HopperSetting.IDLE));
	}
}