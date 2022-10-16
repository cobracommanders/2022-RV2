package frc.robot.commands.hopper;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.centerer.ToggleCenterer;
import frc.robot.subsystems.Centerer;
import frc.robot.subsystems.Centerer.CentererState;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Hopper.HopperSetting;
import frc.robot.subsystems.Hopper.HopperState;
import frc.robot.subsystems.Shooter;

// This is run while the intake button is held, it sets the hopper motors based on instruction from the hopper class
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
	public void execute() {
		switch (hopper.getOperation()) {
			case CORRECT:
				new SaveCargoHigh(hopper).schedule(false);
				break;

			case INCORRECT:
				new EjectCargo(hopper, shooter).schedule(false);
				break;

			case IDLE:
				intakeCommand.cancel();
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