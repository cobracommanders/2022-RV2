package frc.robot.commands.hopper;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Hopper.HopperState;

public class SaveCargo extends CommandBase {
	private Hopper hopper;
	private int cargoCount;

	public SaveCargo(Hopper hopper) {
		this.hopper = hopper;
		cargoCount = this.hopper.getCargoCount();
		addRequirements(this.hopper);
	}

	// Starts by setting the hopper to load if there's one cargo, and outtake if
	// there are two. This is so the top ball will spin in place and not get pushed
	// into the shooter. It will stop outtaking as soon as the cargo passes the
	// lower beam break sensor, around the center of the hopper.
	@Override
	public void initialize() {
		switch (cargoCount) {
			case 0:
				hopper.setState(HopperState.OUTTAKE);
				break;
			case 1:
				hopper.setState(HopperState.LOAD);
				break;

			default:
				end(true);
		}
	}

	// When the correct beam break is triggered, end the command
	@Override
	public boolean isFinished() {
		return cargoCount == 0 ? hopper.getUpperSensor() : hopper.getLowerSensor();
	}

	// When the command ends, stop the motors
	@Override
	public void end(boolean interrupted) {
		hopper.addCargoCount();
		hopper.setState(HopperState.IDLE);
	}

}
