package frc.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Properties;
import java.util.Set;

public class Settings extends Properties {

	private File configFile;
	private File configDir;
	private String name;
	private Settings defaults;
	private String saveFilepath;

	public Settings(String name, String saveFilepath) {
		super();
		this.name = name;
		this.saveFilepath = saveFilepath;
		createFile();
	}

	public Settings(String name, Settings defaultSettings, String saveFilePath) {
		super(defaultSettings);
		this.name = name.toLowerCase();
		defaults = defaultSettings;
		this.saveFilepath = saveFilePath;
		createFile();
	}

	public void set(String key, String newValue) {
		if (defaults != null && !defaults.containsKey(key)) {
			throw new InvalidParameterException();
		}

		setProperty(key, newValue);

		FileWriter writer;
		try {
			writer = new FileWriter(configFile);
			store(writer, name);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to set new settings!");
		}

		updateProperties();
	}

	public String get(String key) {

		if (defaults != null && !defaults.containsKey(key)) {
			throw new InvalidParameterException();
		}

		updateProperties();
		return getProperty(key);
	}

	public Set<String> listAll() {
		updateProperties();
		return stringPropertyNames();
	}

	private void updateProperties() {
		try {
			FileReader reader = new FileReader(configFile);
			load(reader);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to update properties from file");
		}
	}

	private void createFile() {
		System.out.println((saveFilepath + "config/") + name + ".properties");
		configDir = new File((saveFilepath + "config/").replace('/', File.separatorChar));
		configFile = new File(configDir, name + ".properties");
		try {
			configDir.mkdir();
			configFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to create the config file!");
		}
	}
}