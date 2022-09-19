package frc.robot.commands.hopper;

import static frc.robot.Constants.HopperConstants.kHopperAlignSpeed;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Hopper.HopperSetting;

public class SaveCargoLow extends SequentialCommandGroup {

	public SaveCargoLow(Hopper hopper) {
		addRequirements(hopper);

		addCommands(
				new InstantCommand(() -> hopper.setState(HopperSetting.OUTTAKE, 0.25, 0.25)),
				new WaitUntilCommand(() -> hopper.getLowerSensor()),
				new InstantCommand(() -> hopper.setState(HopperSetting.OUTTAKE, -kHopperAlignSpeed, -kHopperAlignSpeed)),
				new WaitUntilCommand(() -> !hopper.getLowerSensor()),
				new InstantCommand(() -> hopper.addToCargoCount()),
				new InstantCommand(() -> hopper.setState(HopperSetting.IDLE, 0, 0)));
	}
}
