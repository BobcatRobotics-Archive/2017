package org.usfirst.frc.team177.lib;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RioLoggerThread extends Thread {
	private String path =  File.separator + "home" + File.separator + "lvuser" + File.separator + "logs";
	private String filename = path + File.separator + new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss'.thread.txt'").format(new Date());

	private List<String> logs = new ArrayList<String>();
	private long totalLogTime = 1500 * 1000L; // Default is 25:00 minutes (milliseconds)
	private long logFrequency = 150 * 1000L;  // Default is 2:30
	private long endTime;
	private boolean isLogging = true;

	private static RioLoggerThread singleton;

	/* Create private constructor */
	private RioLoggerThread() {
		super();
		createLogDirectory();
		endTime = System.currentTimeMillis() + totalLogTime;
	}

	/* Create a static method to get instance. */
	public static RioLoggerThread getInstance() {
		if (singleton == null) {
			singleton = new RioLoggerThread();
		}
		return singleton;
	}
	
	// Set Logging Time (in seconds)
	public void setLoggingParameters(long totalLogTime, long logFrequency) {
		this.totalLogTime = totalLogTime * 1000L;
		this.logFrequency = logFrequency *1000L;
	}

	public void log(String line) {
		logs.add(line);
	}

	public void stopLogging() {
		isLogging = false;
	}

	@Override
	public void run() {
		do {
			try {
				Thread.sleep(logFrequency);
			} catch (InterruptedException e) {
				System.out.println("RioLogger run exception" + e);
			}
			if (logs.size() > 0) {
				List<String> tempLog = new ArrayList<String>(logs);
				logs.clear();
				writeLog(tempLog);
			} 
		} while (isLogging && (System.currentTimeMillis() < endTime));
	}

	private void createLogDirectory() {
		File newDir = new File(path);
		if (!newDir.exists()) {
			try {
				newDir.mkdir();
			} catch (SecurityException e) {
				System.out.println("RioLogger Security exception " + e);
				e.printStackTrace();
			}
		}
	}

	private void writeLog(List<String> log) {
		BufferedWriter outputStream;
		try {
			// Open the file
			outputStream = new BufferedWriter(new FileWriter(filename, true));
			for (String line : log) {
				outputStream.write(line);
				outputStream.newLine();
			}

			// Close the file
			outputStream.close();
		} catch (IOException e) {
			System.out.print("Error writing log " + e);
			e.printStackTrace();
		}
	}

}
