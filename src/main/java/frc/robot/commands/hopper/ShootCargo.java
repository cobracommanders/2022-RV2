package frc.robot.commands.hopper;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Hopper.HopperState;

public class ShootCargo extends CommandBase {
	private Hopper hopper;
	private int cargoCount;

	public ShootCargo(Hopper hopper) {
		this.hopper = hopper;
		cargoCount = this.hopper.getCargoCount();
		addRequirements(this.hopper);
	}

	@Override
	public void initialize() {
		if (hopper.getCargoCount() == 0) {
			end(true);
			return;
		}
		hopper.setState(HopperState.LOAD);
	}

	// When the correct beam break is triggered, end the command
	private boolean hasCargoExitedLow = false;
	private boolean hasCargoExitedHigh = false;

	@Override
	public boolean isFinished() {

		return cargoCount == 2
				? (hasCargoExitedHigh ^ hopper.getUpperSensor()) && (!(hasCargoExitedHigh = hopper.getUpperSensor()))
				: (hasCargoExitedLow ^ hopper.getLowerSensor()) && (!(hasCargoExitedLow = hopper.getLowerSensor()));
	}

	// When the command ends, stop the motors
	@Override
	public void end(boolean interrupted) {
		if (hopper.getCargoCount() >= 0)
			hopper.removeCargoCount();
		hopper.setState(HopperState.IDLE);
	}

}
