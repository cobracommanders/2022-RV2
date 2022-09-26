package frc.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;

public class Logger {
	private FileWriter writer;
	private File file;

	public Logger(String name) {
		file = new File(name);
		try {
			writer = new FileWriter(file + ".txt");
		} catch (IOException e) {
			System.out.println("Log already generated!");
		}
	}

	private void initLog() {
		try {
			writer.append(
					"New log created!" +
							"\n Time: " + Instant.now().toString() +
							"\n Event Name: " + DriverStation.getEventName() +
							"\n Match Type: " + DriverStation.getMatchType().toString() +
							"\n Match Number: " + String.valueOf(DriverStation.getMatchNumber()) +
							// If there is a non-0 number of replays for this match, add another line
							// stating the current number
							(DriverStation.getReplayNumber() == 0
									? ""
									: "\n Replay Number: " + String.valueOf(DriverStation.getReplayNumber()) +
											"\n Driver Station: " + DriverStation.getAlliance().toString() + " "
											+ String.valueOf(DriverStation.getLocation())) + 
							"\n Initial Battery Voltage: " + RobotController.getBatteryVoltage());
		} catch (IOException e) {
			System.out.println("Error initilizing log!");
			e.printStackTrace();
		}
	}

	public boolean log(String event) {
		try {
			writer.append("\n [" + getMatchTime() + "] " + event);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public double getMatchTime() {
		//TODO Make this give the actual time, not the match time
		return DriverStation.getMatchTime();
	}
}