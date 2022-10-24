package org.team498.C2022.commands.hopper;

import org.team498.C2022.subsystems.Hopper;
import org.team498.C2022.subsystems.Hopper.HopperSetting;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

// Brings a cargo to the top of the hopper
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