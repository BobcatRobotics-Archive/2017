package org.usfirst.frc.team177.lib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LocalReader {

	private final String fileName = File.separator + "home" + File.separator + "lvuser" + File.separator + "robot.cfg";

	public LocalReader() {
		super();
	}

	public RobotConfiguration readConfigFile() {
		RobotConfiguration config = RobotConfiguration.getInstance();
		try {
			FileReader file = new FileReader(fileName);
			BufferedReader br = new BufferedReader(file);
			config.setAutoMode(split(br.readLine()));
			config.setSampleRate(new Long(split(br.readLine())));
			config.setIncreaseFactor(new Double(split(br.readLine())));
			config.setDecreaseFactor(new Double(split(br.readLine())));
			config.setDeadBandRange(new Double(split(br.readLine())));
			br.close();
		} catch (FileNotFoundException e) {
			/* This exception is ok. A default configuration will be used */
			System.out.println("File not found " + fileName);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error reading configuration file " + e);
		}
		return config;
	}

	private String split(String line) {
		String[] str = line.split(" ");
		return str[0];
	}
}
