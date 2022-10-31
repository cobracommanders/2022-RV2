package org.team498.lib.logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CSVReader {

	private final File file;

	/**
	 * Constructs a new CSVReader
	 * 
	 * @param path path to the file, using '/' as a seperator between folders
	 * @param name name of the file
	 */
	public CSVReader(String path, String name) {
		File filepath = new File(path.replace('/', File.separatorChar));
		file = new File(filepath, name + ".csv");
	}

	private Scanner scanner;

	/**
	 * Opens the file. This must be run before attempting to read from it
	 */
	public void open() {
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the next set of values from the file
	 * 
	 * @return an array of the values
	 */
	public String[] nextLine() {
		return scanner.nextLine().split(",");
	}

	/**
	 * Gets a boolean indicating if there is another line in the file
	 * 
	 * @return true if there are more lines
	 */
	public boolean hasNextLine() {
		return scanner.hasNextLine();
	}

	/**
	 * Closes the file, should run after reading from it. This also resets the
	 * current line to the begining of the file
	 */
	public void close() {
		scanner.close();
	}

}
