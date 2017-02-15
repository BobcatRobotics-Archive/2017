package org.usfirst.frc.team177.lib;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RioLogger {

	private String filename = "/home/lvuser/logs/" + new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss'.txt'").format(new Date());
	public RioLogger() {
		super();
		File newDir = new File("/home/lvuser/logs");
		if (!newDir.exists()) {
			try {
				newDir.mkdir();
			} catch (SecurityException e) {
				System.out.print("Security exception " + e);
			}
			
		}
	}
	
	public void log(String line) {
		BufferedWriter outputStream;

		try {
			//Open the file
			outputStream = new BufferedWriter(new FileWriter(filename,true));
			
			outputStream.write(line);
			outputStream.newLine();
			
			//Close the file
			outputStream.close();
			
		} catch (IOException e) {
			System.out.print("Error writing log " + e);
			e.printStackTrace();				
		}
	}

	
}
