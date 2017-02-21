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
	private List<String> logs = new ArrayList<String>();

	private String filename = "/home/lvuser/logs/"
			+ new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss'.txt'").format(new Date());
	private long totalLogTime = 300 * 1000L; // Default is 5:00 minutes (milliseconds)
	private long logFrequency = 150 * 1000L; // Default is 2:30
	private long endTime;

	public RioLoggerThread() {
		super();
		createLogDirectory();
		endTime = System.currentTimeMillis() + totalLogTime;
	}

	public RioLoggerThread(long totalLogTime, long logFrequency) {
		this();
		this.totalLogTime = totalLogTime;
		this.logFrequency = logFrequency;
	}

	public void log(String line) {
		logs.add(line);
	}

	@Override
	public void run() {
		do {
			try {
				Thread.sleep(logFrequency);
			} catch (InterruptedException e) {
				System.out.println("RioLogger run exception" + e);
			}
			List<String> tempLog = new ArrayList<String>(logs);
			logs.clear();
			writeLog(tempLog);

		} //while (System.currentTimeMillis() < endTime);
		while (true);
	}

	private void createLogDirectory() {
		File newDir = new File("/home/lvuser/logs");
		if (!newDir.exists()) {
			try {
				newDir.mkdir();
			} catch (SecurityException e) {
				System.out.println("RioLogger Security exception " + e);
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
