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
	// XXX
	private RioLogger logger = RioLogger.getInstance();
	
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
		logger.log("RioLoggerThread Constructor()");
		createLogDirectory();
		logger.log("RioLoggerThread System Time " + System.currentTimeMillis());
		endTime = System.currentTimeMillis() + totalLogTime;
		logger.log("RioLoggerThread endTime " + endTime);
		
	
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
		logger.log("RioLoggerThread setLoggingParameters()");
		
		this.totalLogTime = totalLogTime * 1000L;
		this.logFrequency = logFrequency *1000L;
		logger.log("RioLoggerThread setLoggingParameters() " + this.totalLogTime + " " + this.logFrequency);
	}

	public void log(String line) {
		logs.add(line);
	}

	public void stopLogging() {
		isLogging = false;
	}

	@Override
	public void run() {
		logger.log("RioLoggerThread run()");
		do {
			try {
				logger.log("RioLoggerThread run(). Sleeping for " + logFrequency );
				
				Thread.sleep(logFrequency);
			} catch (InterruptedException e) {
				System.out.println("RioLogger run exception" + e);
			}
			logger.log("RioLoggerThread run(). Logging entries. Size is  " + logs.size() );
			
			if (logs.size() > 0) {
				List<String> tempLog = new ArrayList<String>(logs);
				logs.clear();
				writeLog(tempLog);
			} 
			logger.log("RioLoggerThread run() isLogging = " + isLogging);
			logger.log("RioLoggerThread run() timecheck = " + (System.currentTimeMillis() < endTime));
		} while (isLogging && (System.currentTimeMillis() < endTime));
	}

	private void createLogDirectory() {
		logger.log("RioLoggerThread createLogDirectory(). Checking " + path);

		File newDir = new File(path);
		if (!newDir.exists()) {
			logger.log("RioLoggerThread createLogDirectory(). creating path");

			try {
				newDir.mkdir();
			} catch (SecurityException e) {
				logger.log("RioLoggerThread createLogDirectory() Error " + e);
				System.out.println("RioLogger Security exception " + e);
				e.printStackTrace();
			}
		}
	}

	private void writeLog(List<String> log) {
		BufferedWriter outputStream;
		logger.log("RioLoggerThread writeLog() writing entries " +log.size() );
		logger.log("RioLoggerThread writeLog() file " +filename );
		
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
			logger.log("RioLoggerThread createLogDirectory() Error " + e);
			System.out.print("Error writing log " + e);
			e.printStackTrace();
		}
	}

}
