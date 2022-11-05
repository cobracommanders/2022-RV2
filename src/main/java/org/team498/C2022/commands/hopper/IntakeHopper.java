package org.team498.C2022.commands.hopper;

import org.team498.C2022.commands.centerer.ToggleCenterer;
import org.team498.C2022.subsystems.Centerer;
import org.team498.C2022.subsystems.Hopper;
import org.team498.C2022.subsystems.Shooter;
import org.team498.C2022.subsystems.Centerer.CentererState;
import org.team498.C2022.subsystems.Hopper.HopperSetting;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

// This is run while the intake button is held, it sets the hopper motors based on instruction from the hopper class
public class IntakeHopper extends CommandBase {
	private Hopper hopper;
	private Shooter shooter;
	private Centerer centerer;
	private Command intakeCommand;

	public IntakeHopper(Hopper hopper, Centerer centerer) {
		this.hopper = hopper;
		this.centerer = centerer;
		intakeCommand = new ParallelCommandGroup(
				new ToggleHopper(this.hopper, HopperSetting.INTAKE),
				new ToggleCenterer(this.centerer, CentererState.CENTER));
		addRequirements(this.hopper);
	}

	@Override
	public void execute() {
		switch (hopper.getOperation()) {
			case CORRECT:
				new SaveCargoHigh(hopper).schedule(false);
				break;

			case INCORRECT:
				new EjectCargo(hopper).schedule(false);
				break;

			case IDLE:
				intakeCommand.cancel();
				break;

			case EMPTY:
				intakeCommand.schedule(true);
				break;
		}
	}

	@Override
	public void end(boolean interrupted) {
		intakeCommand.cancel();
	}
}