package org.team498.C2022;

import org.team498.C2022.RobotContainer.ControlSet;
import org.team498.C2022.subsystems.Hopper;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class Robot extends TimedRobot {
	private RobotContainer robotContainer;
	private Command autoCommand;

	@Override
	public void robotInit() {
		robotContainer = new RobotContainer(ControlSet.STANDARD);
		robotContainer.getRobotInitCommand().schedule();
	}

	@Override
	public void robotPeriodic() {
		CommandScheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		autoCommand = robotContainer.getAutoCommand();

		if (autoCommand != null) {
			autoCommand.schedule();
		}

		Hopper.updateAlliance();
	}

	@Override
	public void teleopInit() {
		CommandScheduler.getInstance().cancelAll();
		Hopper.updateAlliance();
	}

}
