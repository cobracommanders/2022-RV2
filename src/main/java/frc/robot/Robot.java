package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.Hopper;

public class Robot extends TimedRobot {
	private RobotContainer robotContainer;
	private Command autoCommand;

	@Override
	public void robotInit() {

		robotContainer = new RobotContainer();
		robotContainer.getRobotInitCommand().schedule();
		SmartDashboard.putNumber("Auto Shoot Delay", 4);
		SmartDashboard.putNumber("Auto Drive Delay", 8);

		SmartDashboard.putNumber("T-Shooter RPM", 0);
		SmartDashboard.putNumber("T-Hood angle", 0);
		SmartDashboard.putNumber("T-limelight distance", 0);
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
