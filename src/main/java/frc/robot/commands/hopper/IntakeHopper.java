package frc.robot.commands.hopper;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Hopper.HopperSetting;
import frc.robot.subsystems.Shooter;

public class IntakeHopper extends CommandBase {
	private Hopper hopper;
	private Shooter shooter;
	private ParallelCommandGroup intakeCommand;

	public IntakeHopper(Hopper hopper, Shooter shooter) {
		this.hopper = hopper;
		this.shooter = shooter;
		intakeCommand = new ParallelCommandGroup(
				new ToggleHopper(this.hopper, HopperSetting.INTAKE));
		addRequirements(this.hopper);
	}

	@Override
	public void initialize() {
		if (!hopper.getAutoEnabled()) {
			cancel();
		}
	}

	@Override
	public void execute() {
		switch (hopper.getQueuedOperation()) {
			case CORRECT:
				if (!hopper.getCargoLoadedHigh())
					new SaveCargoHigh(hopper).schedule(false);
				else
					cancel();
				break;

			case INCORRECT:
				new EjectCargo(hopper, shooter).schedule(false);
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