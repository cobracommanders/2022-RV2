package org.team498.app;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class ControllerApp extends CommandBase {
	// normally, I would make this a separate thing, but we want to run it at the
	// same time as the robot
	// also this is the easiest way to access the controllers lol
	private FileWriter printer;
	private XboxController controller;
	private Timer timer = new Timer();
	private File file;

	public ControllerApp(XboxController controller, String fileName, String filePath) {
		this.controller = controller;

		File filepath = new File(filePath.replace('/', File.separatorChar));
		file = new File(filepath, fileName);
		openFile();
	}

	@Override
	public void initialize() {
		// printer.print("{\n");
		timer.start();
	}

	int printTimer = 0;

	@Override
	public void execute() {
		double timeStamp = timer.get();
		double dx = controller.getLeftY();
		double dy = controller.getLeftX();
		double dr = controller.getRightX();

		if (printTimer++ >= 5) {
			printInfo(timeStamp, dx, dy, dr);
			printTimer = 0;
		}
	}

	@Override
	public void end(boolean interrupted) {
		// printer.print("\n}");
		closeFile();
		timer.stop();
		timer.reset();
	}

	private void printInfo(double timeStamp, double dx, double dy, double dr) {
		try {
			printer.write(String.format("{ %.3f, %.4f, %.4f, %.4f },\n", timeStamp, dx, dy, dr));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void openFile() {
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			printer = new FileWriter(file, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void closeFile() {
		try {
			printer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
