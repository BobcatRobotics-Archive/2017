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
	private String filename = path + File.separator + new SimpleDateFormat("yyyy-MM-dd_hh.mm.ss'.thread.txt'").format(new Date());
	private static RioLoggerThread singleton;
	private RioLogger lg = RioLogger.getInstance();

	private List<String> logs = new ArrayList<String>();
	private long totalLogTime = 3600 * 1000L; // Default is 1 hour (milliseconds)
	private long logFrequency = 150 * 1000L;  // Default is 2 minutes 30 seconds
	private long endTime = 0L;
	private boolean isLogging = false;

	/* Create private constructor */
	private RioLoggerThread() {
		super();
		createLogDirectory();
		log("RioLoggerThread() constructor");
	}

	/* Create a static method to get instance. */
	public static RioLoggerThread getInstance() {
		if (singleton == null) {
			singleton = new RioLoggerThread();
		}
		return singleton;
	}
	
	// Set Logging Time (in seconds)
	public void setLoggingParameters(long totLogTime, long totFreq) {
		totalLogTime = totLogTime * 1000L;
		logFrequency = totFreq *1000L;
		endTime = System.currentTimeMillis() + totalLogTime;
		//log("current time, end time " +System.currentTimeMillis() + ", " + endTime );
		lg.log("RioLoggerThread setParms() current time, end time " +System.currentTimeMillis() + ", " + endTime );
		if (!isLogging) 
			start();
	}

	public void log(String line) {
		logs.add(line);
	}

	public void stopLogging() {
		isLogging = false;
	}

	
	public boolean isLogging() {
		return isLogging;
	}

	/* This method will interrupt the current thread */
	/* write the log and then resume                 */
	public void writeLog() {
		interrupt();
	}
	
	@Override
	public void run() {
		log("RioLoggerThread started");
		isLogging = true;
		do {
			try {
					Thread.sleep(logFrequency);
			} catch (InterruptedException e) {
				/* The thread can be interrupted by a request to write the logs */
				lg.log("RioLoggerThread interrupted.");
				e.printStackTrace();
			}
			if (logs.size() > 0) {
				List<String> tempLog = new ArrayList<String>(logs);
				logs.clear();
				writeLog(tempLog);
			} 
			//lg.log("run() current time, end time " +System.currentTimeMillis() + ", " + endTime );
			//lg.log("run() isLogging " + isLogging);
		} while (isLogging && (System.currentTimeMillis() < endTime));
		log("RioLoggerThread ending");
		lg.log("RioLoggerThread ending ending current time, end time " +System.currentTimeMillis() + ", " + endTime );
		writeLog(logs);
		logs.clear();
		isLogging = false;
	}

	private void createLogDirectory() {
		File newDir = new File(path);
		if (!newDir.exists()) {
			try {
				newDir.mkdir();
			} catch (SecurityException e) {
				lg.log("RioLogger Security exception " + e);
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
			lg.log("Error writing log " + e);
			e.printStackTrace();
		}
	}
}
