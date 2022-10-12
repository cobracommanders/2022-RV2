package frc.robot.commands.hopper;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Hopper.HopperSetting;

public class SaveCargoHigh extends SequentialCommandGroup {

	public SaveCargoHigh(Hopper hopper) {
		addRequirements(hopper);
		addCommands(
				new SetHopper(hopper, HopperSetting.LOAD),
				new WaitUntilCommand(() -> hopper.getUpperSensor()),
				new SetHopper(hopper, HopperSetting.REVERSE),
				new WaitCommand(0.3),
				new SetHopper(hopper, HopperSetting.IDLE));
	}
}