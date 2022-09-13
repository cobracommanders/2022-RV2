package frc.robot.commands.hopper;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SelectCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Hopper.CargoColor;

public class AutoHopper extends SequentialCommandGroup {
	public AutoHopper(Hopper hopper) {
		addRequirements(hopper);
		addCommands(
				new WaitUntilCommand(() -> hopper.getCargoColor() != CargoColor.NONE),
				new SelectCommand(
						new Supplier<Command>() {
							@Override
							public Command get() {
								return hopper.getCargoColor() == CargoColor.CORRECT
										? new SaveCargo(hopper)
										: new EjectCargo(hopper);
							}
						}));
	}
}