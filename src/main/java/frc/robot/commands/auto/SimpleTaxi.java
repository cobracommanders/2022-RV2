
package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Drivetrain;
import frc.robot.commands.ResetGyro;
import frc.robot.commands.drivetrain.SetSwerve;

public class SimpleTaxi extends SequentialCommandGroup {
	public SimpleTaxi(Drivetrain drivetrain) {
		addCommands(new ResetGyro(drivetrain), new SetSwerve(drivetrain, -1, 0, 0), new WaitCommand(3), new SetSwerve(drivetrain, 0, 0, 0));
	}
}
