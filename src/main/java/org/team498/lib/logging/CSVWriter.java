package org.team498.lib.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CSVWriter {

	private final File file;

	/**
	 * Constructs a new CSVWriter
	 * 
	 * @param path path to the file, using '/' as a seperator between folders
	 * @param name name of the file
	 */
	public CSVWriter(String path, String name) {
		File filepath = new File(path.replace('/', File.separatorChar));

		filepath.mkdirs();

		file = new File(filepath, name + ".csv");

		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private BufferedWriter writer;

	/**
	 * Opens the file. This must be run before attempting to write to it
	 */
	public void open() {
		FileWriter w;
		try {
			w = new FileWriter(file, true);
			writer = new BufferedWriter(w);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Writes a number of values to the file
	 * 
	 * @param value the values to add to the file
	 */
	public void write(String... values) {
		try {

			for (String s : values) {
				writer.write(s + ',');
			}

			writer.write('\n');

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Closes the file, should run after writing to it. This also saves the changes
	 * to the file on disk
	 */
	public void close() {
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}