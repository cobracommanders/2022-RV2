package org.team498.C2022.commands.hopper;

import java.util.function.Supplier;

import org.team498.C2022.subsystems.Hopper;
import org.team498.C2022.subsystems.Hopper.HopperSetting;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SelectCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

public class ShootCargo extends SequentialCommandGroup {
	public ShootCargo(Hopper hopper) {
		addCommands(
				new SetHopper(hopper, HopperSetting.LOAD),
				new WaitUntilCommand(() -> !hopper.getUpperSensor()),
				new SelectCommand(new Supplier<Command>() {
					@Override
					public Command get() {
						if (hopper.getLowerSensor()) {
							// new SequentialCommandGroup(
							// return new SaveCargoHigh(hopper);
							return new SequentialCommandGroup(
									new SetHopper(hopper, HopperSetting.LOAD),
									new WaitCommand(0.35),
									new SetHopper(hopper, HopperSetting.IDLE),
									new WaitCommand(1),
									new SetHopper(hopper, HopperSetting.LOAD),
									new WaitCommand(0.5),
									new SetHopper(hopper, HopperSetting.IDLE));
									// TODO potentially set the hopper to 'reverse' for a few seconds to align the top cargo better
						}
						return new SetHopper(hopper, HopperSetting.IDLE);
					}
				}),
				new SetHopper(hopper, HopperSetting.IDLE)
				);
		;
	}
}
