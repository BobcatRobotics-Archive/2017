package org.usfirst.frc.team177.lib;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RioLogger {
	private String path =  File.separator + "home" + File.separator + "lvuser" + File.separator + "logs";
	private String filename =path + File.separator + new SimpleDateFormat("yyyy-MM-dd_hh.mm.ss'.txt'").format(new Date());
	private static RioLogger singleton;

	/* Create private constructor */
	private RioLogger() {
		super();
		createLogDirectory();
	}

	/* Create a static method to get instance. */
	public static RioLogger getInstance() {
		if (singleton == null) {
			singleton = new RioLogger();
		}
		return singleton;
	}

	public void log(String line) {
		BufferedWriter outputStream;

		try {
			// Open the file
			outputStream = new BufferedWriter(new FileWriter(filename, true));
			outputStream.write(line);
			outputStream.newLine();

			// Close the file
			outputStream.close();
		} catch (IOException e) {
			System.out.print("Error writing log " + e);
			e.printStackTrace();
		}
	}

	private void createLogDirectory() {
		File newDir = new File(path);
		if (!newDir.exists()) {
			try {
				newDir.mkdir();
			} catch (SecurityException e) {
				System.out.println("RioLogger Security exception " + e);
			}
		}
	}
}
