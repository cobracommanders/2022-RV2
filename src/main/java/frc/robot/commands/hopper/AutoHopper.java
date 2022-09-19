package frc.robot.commands.hopper;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Hopper.HopperCargoState;

public class AutoHopper extends CommandBase {
	private Hopper hopper;
	private BooleanSupplier intakeing;

	public AutoHopper(Hopper hopper, BooleanSupplier intakeing) {
		this.hopper = hopper;
		this.intakeing = intakeing;
		addRequirements(this.hopper);
	}

	@Override
	public void execute() {
		if (hopper.getCurrentCommand() == hopper.getDefaultCommand())
			run(hopper.getQueuedOperation());
	}

	private void run(HopperCargoState operation) {
		
		if (intakeing.getAsBoolean()
				&& (hopper.getCurrentCommand() == hopper.getDefaultCommand())
				&& !hopper.getUpperSensor()) {

			hopper.getIntakeCommand().schedule(true);
		}

		if (operation == HopperCargoState.EMPTY)
			return;

		if (operation == HopperCargoState.CORRECT && !hopper.getUpperSensor()) {
			new SaveCargoHigh(hopper).schedule(false);
		} else if (operation == HopperCargoState.INCORRECT) {
			new EjectCargo(hopper).schedule(false);
		}
	}
}