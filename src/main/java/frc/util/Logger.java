package frc.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import frc.robot.Robot;

public class Logger {
	private FileWriter writer;
	private File file;
	private File directory;
	private String name;
	private String saveFilePath;
	private Settings settings;

	private File renameFile;

	// General save path for the rio: /home/lvuser/

	// SFTP rio address: roborio-498-frc.local
	// SFTP rio port: 22

	private final String fileExtention = ".rlog";

	public Logger(String name) {
		/*
		this.name = name;

		saveFilePath = Robot.isReal()
				? "U/logs/"
				: "src/main/java/frc/logs/";
		saveFilePath = saveFilePath.replace('/', File.separatorChar);

		// Try to create a file on the USB drive
		try {
			directory = new File(saveFilePath);
			file = new File(directory, "temp" + fileExtention);
			directory.mkdirs();
			file.createNewFile();
			settings = new Settings(name + "Counter", saveFilePath);
		} catch (IOException e) {
			// If that fails, try to create a file on the RIO
			try {
				directory = new File("/home/lvuser/logs/".replace('/', File.pathSeparatorChar));
				file = new File(directory, "temp" + fileExtention);
				directory.mkdirs();
				file.createNewFile();
				settings = new Settings(name + "Counter", "/home/lvuser/logs/".replace('/', File.pathSeparatorChar));
				// If that fails, give up
			} catch (IOException e1) {
				e1.printStackTrace();
				System.out.println("ERROR: Log file not created");
			}

		}

		try {
			writer = new FileWriter(file);
			writer.append("[" + getTime() + "] [" + getClass().getSimpleName() + "] Logging started");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
	}

	// Writes an event to the log with this format:
	// [TIME] [CAUSE] event
	public void log(String... event) {
		/*
		// Create the filewriter
		try {
			writer = new FileWriter(file, true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// For each of the provided events
		for (String string : event) {
			// Add a new line with the time and class name to the given event
			try {
				// Gets the name of the class that added this to the log, and removes the .java
				String className = Thread.currentThread().getStackTrace()[2].getFileName().replaceAll(".java", "");
				writer.append("\n[" + getTime() + "] [" + className + "] " + string);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Save the file
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
	}

	// Returns the time in this format:
	// HH:MM:SS:MMM
	private String getTime() {
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		int millisecond = calendar.get(Calendar.MILLISECOND);

		return addZerosToLength(2, hour)
				+ ":" + addZerosToLength(2, minute)
				+ ":" + addZerosToLength(2, second)
				+ ":" + addZerosToLength(3, millisecond);
	}

	public void updateDate() {
		/*
		log(getDate());

		if (settings.get("count") == null) {
			System.out.println("INITILIZING COUNTER");
			settings.set("count", "0");
		}

		settings.set("count", String.valueOf(Integer.parseInt(settings.get("count")) + 1));

		renameFile = new File(directory, name + " " + getDate() + " #" + settings.get("count") + fileExtention);
		file.renameTo(renameFile);
		*/
	}

	// Returns the date in this format:
	// DD-MM-YYYY
	private String getDate() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DATE);

		return addZerosToLength(2, month)
				+ "-" + addZerosToLength(2, day)
				+ "-" + addZerosToLength(4, year);
	}

	// Takes an input and adds zeros to the end until it reaches a specified number
	// of characters
	private String addZerosToLength(int length, int input) {
		return String.format("%1$" + length + "s", input).replace(' ', '0');
	}
}