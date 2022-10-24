package org.team498.C2022.commands.hopper;

import java.util.function.Supplier;

import org.team498.C2022.subsystems.Hopper;
import org.team498.C2022.subsystems.Hopper.HopperSetting;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SelectCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
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
							return new SaveCargoHigh(hopper);
							// );
						}
						return new SetHopper(hopper, HopperSetting.IDLE);
					}
				})
				);
		;
	}
}
