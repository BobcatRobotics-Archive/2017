package org.usfirst.frc.team177.lib;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RioLogger {

	private String filename = "/tmp/log/" + new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss'.txt'").format(new Date());
	public RioLogger() {
		super();
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
			e.printStackTrace();				
		}
	}

	
}
