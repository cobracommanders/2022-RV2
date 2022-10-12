package frc.robot.commands.hopper;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.commands.centerer.ToggleCenterer;
import frc.robot.subsystems.Centerer;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Hopper.HopperCargoState;
import frc.robot.subsystems.Hopper.HopperSetting;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Centerer.CentererState;

public class IntakeHopper extends CommandBase {
	private Hopper hopper;
	private Shooter shooter;
	private Centerer centerer;
	private Command intakeCommand;

	public IntakeHopper(Hopper hopper, Shooter shooter, Centerer centerer) {
		this.hopper = hopper;
		this.shooter = shooter;
		this.centerer = centerer;
		intakeCommand = new ParallelCommandGroup(
				new ToggleHopper(this.hopper, HopperSetting.INTAKE),
				new ToggleCenterer(this.centerer, CentererState.CENTER));
		addRequirements(this.hopper);
	}

	@Override
	public void initialize() {
	}

	@Override
	public void execute() {
			switch (hopper.getQueuedOperation()) {
				case CORRECT:
					if (!hopper.getUpperSensor())
						hopper.getLowerSensorTrigger().cancelWhenActive(
								new SaveCargoHigh(hopper));
					else {
						// intakeCommand.cancel();
						// intakeCommand.end(true);
						// new SaveCargoLow(hopper).schedule(false);
						intakeCommand.cancel();
						intakeCommand.end(true);
					}
					break;

				case INCORRECT:
					new EjectCargo(hopper, shooter).schedule(false);
					break;

				case EMPTY:
					if (!hopper.hopperFull())
						intakeCommand.schedule(true);
					// else
					// intakeCommand.cancel();
					// intakeCommand.end(true);
					break;
		}
		intakeCommand.cancel();
		intakeCommand.end(true);
	}

	@Override
	public void end(boolean interrupted) {
		intakeCommand.cancel();
	}
}