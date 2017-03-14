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
	private static RioLoggerThread singleton;

	private List<String> logs = new ArrayList<String>();
	private long totalLogTime = 3600 * 1000L; // Default is 1 hour (milliseconds)
	private long logFrequency = 150 * 1000L;  // Default is 2 minutes 30 seconds
	private long endTime = 0L;
	private boolean isLogging = true;

	//private static RioLogger lg = RioLogger.getInstance();
	//private int nbrEntries = 0;
	
	/* Create private constructor */
	private RioLoggerThread() {
		super();
		createLogDirectory();
		endTime = System.currentTimeMillis() + totalLogTime;
		//lg.log("current time, end time " +System.currentTimeMillis() + ", " + endTime );
		logs.add("RioLoggerThread created");
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
		log("current time, end time " +System.currentTimeMillis() + ", " + endTime );
		//lg.log("current time, end time " +System.currentTimeMillis() + ", " + endTime );
	}

	public void log(String line) {
		logs.add(line);
	}

	public void stopLogging() {
		isLogging = false;
	}
	
	/* This method will interrupt the current thread */
	/* write the log and then resume                 */
	public void writeLog() {
		interrupt();
	}
	
	@Override
	public void run() {
		log("RioLoggerThread started");
		do {
			try {
					Thread.sleep(logFrequency);
			} catch (InterruptedException e) {
				/* The thread can be interrupted by a request to write the logs */
				System.out.println("RioLoggerThread interrupted" + e);
			}
			if (logs.size() > 0) {
				List<String> tempLog = new ArrayList<String>(logs);
				logs.clear();
				writeLog(tempLog);
			} 
			//lg.log("current time, end time " +System.currentTimeMillis() + ", " + endTime );
			//lg.log("isLogging " + isLogging);
		} while (isLogging && (System.currentTimeMillis() < endTime));
		isLogging = false;
		logs.add("RioLoggerThread ending");
		writeLog(logs);
		logs.clear();
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
