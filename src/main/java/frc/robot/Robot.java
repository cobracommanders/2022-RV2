package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/* 
 * unplug intake camera from limelight,
 * turn down resolution
*/

public class Robot extends TimedRobot {
	private RobotContainer robotContainer;
	private Command autoCommand;

	@Override
	public void robotInit() {
		robotContainer = new RobotContainer();
		robotContainer.getRobotInitCommand().schedule();
		SmartDashboard.putNumber("Auto Shoot Delay", 4);
		SmartDashboard.putNumber("Auto Drive Delay", 8);
	}

	@Override
	public void robotPeriodic() {
		CommandScheduler.getInstance().run();
	}

	@Override
	public void disabledInit() {
	}

	@Override
	public void disabledPeriodic() {
	}

	@Override
	public void autonomousInit() {
		autoCommand = robotContainer.getAutoCommand();

		if (autoCommand != null) {
			autoCommand.schedule();
		}
	}

	@Override
	public void autonomousPeriodic() {
	}

	@Override
	public void teleopInit() {
	}

	@Override
	public void teleopPeriodic() {
	}

	@Override
	public void testInit() {
	}

	@Override
	public void testPeriodic() {
	}
}
