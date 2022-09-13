package frc.robot.commands.hopper;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Hopper.HopperState;

public class EjectCargo extends CommandBase {
	private boolean hasCargoBeenDetected;
	private Hopper hopper;

	public EjectCargo(Hopper hopper) {
		this.hopper = hopper;
		addRequirements(this.hopper);
	}

	// Starts by setting the hopper to eject
	@Override
	public void initialize() {
		hopper.setState(HopperState.OUTTAKE);

		hasCargoBeenDetected = false;
	}

	// When the lower beam break is deactivated, end the command
	@Override
	public boolean isFinished() {	
		return (hasCargoBeenDetected ^ hopper.getLowerSensor()) && (!(hasCargoBeenDetected = hopper.getLowerSensor()));
	}

	// When the command ends, stop the motors
	@Override
	public void end(boolean interrupted) {
		hopper.setState(HopperState.IDLE);
	}
}