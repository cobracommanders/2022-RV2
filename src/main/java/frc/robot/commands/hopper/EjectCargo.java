package frc.robot.commands.hopper;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Hopper.HopperSetting;

public class EjectCargo extends SequentialCommandGroup {
	private Hopper hopper;

	public EjectCargo(Hopper hopper) {
		this.hopper = hopper;
		addCommands(
			new SetHopperManual(hopper, HopperSetting.OUTTAKE, 0.2),
			new WaitCommand(3),
			new SetHopperManual(hopper, HopperSetting.IDLE, 0)
		);
	}

	@Override
	public void end(boolean interrupted) {
		hopper.setState(HopperSetting.IDLE, 0, 0);
	}
}