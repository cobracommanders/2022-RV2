package frc.robot.commands.hopper;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Hopper.HopperState;

import static frc.robot.Constants.HopperConstants.*;

public class ShootCargo extends CommandBase {
	private Hopper hopper;
	private int cargoCount;
	private boolean hasCargoExitedLow;
	private boolean hasCargoExitedHigh;

	public ShootCargo(Hopper hopper) {
		this.hopper = hopper;
		cargoCount = this.hopper.getCargoCount();
		addRequirements(this.hopper);

		hasCargoExitedLow = false;
		hasCargoExitedHigh = false;
	}

	@Override
	public void initialize() {
		if (hopper.getCargoCount() == 0) {
			end(true);
			return;
		}
		hopper.setState(HopperState.LOAD, kHopperLoadSpeed);
	}

	// When the correct beam break is triggered, end the command
	@Override
	public boolean isFinished() {
		return cargoCount == 2
				? (hasCargoExitedLow ^ hopper.getLowerSensor()) && (!(hasCargoExitedLow = hopper.getLowerSensor()))
				: (hasCargoExitedHigh ^ hopper.getUpperSensor()) && (!(hasCargoExitedHigh = hopper.getUpperSensor()));
	}

	// When the command ends, stop the motors
	@Override
	public void end(boolean interrupted) {
			hopper.removeCargoCount();
		hopper.setState(HopperState.IDLE, 0);
	}

}
