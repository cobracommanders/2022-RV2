package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.Hopper;
import frc.util.Counter;
import frc.util.Logger;

public class Robot extends TimedRobot {
	private RobotContainer robotContainer;
	private Command autoCommand;
	public static Logger logger;

	@Override
	public void robotInit() {

		robotContainer = new RobotContainer();
		robotContainer.getRobotInitCommand().schedule();
		SmartDashboard.putNumber("Auto Shoot Delay", 4);
		SmartDashboard.putNumber("Auto Drive Delay", 8);
		// logger = new Logger("general", saveFilePath.replace('/',
		// File.pathSeparatorChar));
		logger = new Logger("general");
		logger.log("Initial battery voltage: " + RobotController.getBatteryVoltage());
	}

	private boolean postedMatchData = false;
	private boolean postedEStop = false;
	private boolean postedJoystickData = false;

	@Override
	public void robotPeriodic() {
		CommandScheduler.getInstance().run();

		/*
		// When the field is connected
		if (!postedMatchData && DriverStation.isFMSAttached()) {
			logger.log(
					"Field connected",
					"--- Match Data ---",
					// Name of the event
					"Event Name: " + DriverStation.getEventName(),
					// Type of the match (Qualification, Elimination, etc.)
					"Match Type: " + DriverStation.getMatchType().toString(),
					// Match number
					"Match Number: " + String.valueOf(DriverStation.getMatchNumber()),
					// Replay number
					"Replay Number: " + String.valueOf(DriverStation.getReplayNumber()),
					// Logs the alliance color and driver station number
					"Driver Station: "
							+ DriverStation.getAlliance().toString() + " "
							+ String.valueOf(DriverStation.getLocation()),
					"------------------");

			postedMatchData = true;
		}

		if (!postedJoystickData && DriverStation.isDSAttached()) {
			logger.log(
					"Driver station connected",
					"--- Joystick Data ---");

			for (int i = 0; DriverStation.kJoystickPorts > i; i++) {
				if (DriverStation.isJoystickConnected(i)) {
					logger.log(
							"Port " + i + ":",
							"    Name: " + DriverStation.getJoystickName(i),
							"    Axis Count: " + DriverStation.getStickAxisCount(i),
							"    Button Count: " + DriverStation.getStickButtonCount(i));
				}
			}

			logger.log("---------------------");

			logger.updateDate();

			postedJoystickData = true;
		}

		if (!postedEStop && DriverStation.isEStopped()) {
			logger.log("Robot E-Stopped");
			postedEStop = true;
		}
		*/
	}

	@Override
	public void disabledInit() {
		logger.log("Entered disabled mode");
	}

	@Override
	public void disabledPeriodic() {
	}

	@Override
	public void disabledExit() {
		logger.log("Exited disabled mode");
	}

	@Override
	public void autonomousInit() {
		autoCommand = robotContainer.getAutoCommand();

		if (autoCommand != null) {
			autoCommand.schedule();
		}

		Hopper.updateAlliance();
		logger.log("Entered autonomous mode");
	}

	@Override
	public void autonomousPeriodic() {
	}

	@Override
	public void autonomousExit() {
		logger.log("Exited autonomous mode");
	}

	@Override
	public void teleopInit() {
		CommandScheduler.getInstance().cancelAll();
		Hopper.updateAlliance();

		logger.log("Teleoperated enabled");
	}

	@Override
	public void teleopPeriodic() {
	}

	@Override
	public void teleopExit() {
		logger.log("Exited teleoperated mode");

		for (Counter counter : Counter.getAll())
			logger.log(counter.getName() + " counter was triggered " + counter.get() + " times");

	}

	@Override
	public void testInit() {
		logger.log("Test enabled");
	}

	@Override
	public void testPeriodic() {
	}

	@Override
	public void testExit() {
		logger.log("Exited test mode");
	}

}
