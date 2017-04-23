package org.usfirst.frc.team177.lib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import edu.wpi.first.wpilibj.DriverStation;

public class LocalReader {
	private static LocalReader singleton;

	private RioLogger logFile = RioLogger.getInstance();
	private final String robotFileName = File.separator + "home" + File.separator + "lvuser" + File.separator + "robot.cfg";
	private final String dashFileName  = File.separator + "home" + File.separator + "lvuser" + File.separator + "dashboard.cfg";
	private final String autoFileName  = File.separator + "home" + File.separator + "lvuser" + File.separator + "auto.cfg";
	private boolean readFile = false;
	private boolean readRobotFile = false;
	private boolean writeFile = false;

	/* Create private constructor */
	private LocalReader() {
		super();
	}

	/* Create a static method to get instance. */
	public static LocalReader getInstance() {
		if (singleton == null) {
			singleton = new LocalReader();
		}
		return singleton;
	}

	public boolean isReadFile() {
		return readFile;
	}

	public boolean isRobotReadFile() {
		return readRobotFile;
	}
	
	public boolean isWriteFile() {
		return writeFile;
	}

	public RobotConfiguration readConfigFile() {
		readRobotFile = false;
		RobotConfiguration config = RobotConfiguration.getInstance();
		FileReader file = null;
		BufferedReader br = null;
		try {
			file = new FileReader(robotFileName);
			br = new BufferedReader(file);
			config.setAutoMode(split(br.readLine()));
			config.setSampleRate(new Long(split(br.readLine())));
			config.setIncreaseFactor(new Double(split(br.readLine())));
			config.setDecreaseFactor(new Double(split(br.readLine())));
			config.setDeadBandRange(new Double(split(br.readLine())));
			readRobotFile = true;
		} catch (FileNotFoundException e) {
			/* This exception is ok. A default configuration will be used */
			System.out.println("File not found " + robotFileName);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error reading configuration file " + e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Error closing file " + e);
				}
			}
		}
		return config;
	}

	private String split(String line) {
		String[] str = line.split(" ");
		return str[0];
	}

	public DashboardConfiguration readDashboardFile() {
		logFile.log("Reading Dashboard Configuration File");
		readFile = false;
		DashboardConfiguration config = DashboardConfiguration.getInstance();
		FileReader file = null;
		BufferedReader br = null;
		try {
			file = new FileReader(dashFileName);
			br = new BufferedReader(file);
			String configLine;
			while ((configLine = br.readLine()) != null) {
				String[] cLine = configLine.split("=");
				// Ignore Blank Lines or comments
				if (cLine.length > 1)
					config.setValue(cLine[0].trim(), new Double(cLine[1].trim()));
			}
			logFile.log("Read Dashboard Configuration File complete");
			readFile = true;
		} catch (FileNotFoundException e) {
			/* This exception is ok. A default configuration will be used */
			logFile.log("Dashboard Configuration File not found " + dashFileName);
			System.out.println("File not found " + dashFileName);
		} catch (IOException e) {
			String err = "Error reading configuration file " + e;
			DriverStation.reportError(err, false);
			logFile.log(err);
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					String err = "Error closing configuration file " + e;
					DriverStation.reportError(err, false);
					logFile.log(err);
					e.printStackTrace();
				}
			}
		}
		return config;
	}

	public boolean writeDashboardFile(DashboardConfiguration dashConfig) {
		logFile.log("Writing dashboard configuration file");
		FileWriter file = null;
		BufferedWriter br = null;
		try {
			file = new FileWriter(dashFileName);
			br = new BufferedWriter(file);
			String[] configLines = dashConfig.getEntries();
			for (String cfg : configLines) {
				br.write(cfg);
				br.newLine();
			}
			logFile.log("Write Dashboard Configuration File complete");
			writeFile = true;
		} catch (FileNotFoundException e) {
			/* This exception is ok. A default configuration will be used */
			logFile.log("Dashboard Configuration File not found " + dashFileName);
			System.out.println("File not found " + dashFileName);
		} catch (IOException e) {
			String err = "Error writing configuration file " + e;
			DriverStation.reportError(err, false);
			logFile.log(err);
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					String err = "Error closing configuration file " + e;
					DriverStation.reportError(err, false);
					logFile.log(err);
					e.printStackTrace();
				}
			}
		}
		return writeFile;
	}
	
	public String readAutoFile() {
		String automode = "";
		FileReader file = null;
		BufferedReader br = null;
		try {
			file = new FileReader(autoFileName);
			br = new BufferedReader(file);
			automode = br.readLine() ;
		} catch (FileNotFoundException e) {
			/* This exception is ok. A default configuration will be used */
		} catch (IOException e) {
			String err = "Error reading configuration file " + e;
			DriverStation.reportError(err, false);
			logFile.log(err);
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					String err = "Error closing configuration file " + e;
					DriverStation.reportError(err, false);
					logFile.log(err);
					e.printStackTrace();
				}
			}
		}
		return automode;
	}
}