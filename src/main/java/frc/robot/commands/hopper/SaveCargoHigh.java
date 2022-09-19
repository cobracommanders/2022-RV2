package frc.robot.commands.hopper;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Hopper.HopperSetting;

public class SaveCargoHigh extends SequentialCommandGroup {

	public SaveCargoHigh(Hopper hopper) {
		addRequirements(hopper);

		addCommands(
				new InstantCommand(() -> hopper.setState(HopperSetting.LOAD, 0.25, 0.23)),
				new WaitUntilCommand(() -> hopper.getUpperSensor()),
				//new InstantCommand(() -> hopper.setState(HopperSetting.LOAD, -0.15, -0.15)),
				//new WaitUntilCommand(() -> !hopper.getUpperSensor()),
				//new WaitCommand(0.2),
				//new InstantCommand(() -> hopper.addToCargoCount()),
				new InstantCommand(() -> hopper.setState(HopperSetting.IDLE, 0, 0)));
	}
}