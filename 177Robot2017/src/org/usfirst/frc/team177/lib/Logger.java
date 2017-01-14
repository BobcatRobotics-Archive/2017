package org.usfirst.frc.team177.lib;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//This class creates a thread the periodically retrieves data from other classes 
//and writes them to a file 
public class Logger {

	private List<Logable> logList = new ArrayList<Logable>();	
	LogThread logThread;
	
	public Logger() {
	
	}
	
	//Remove all registered loggers
	public void clear() {
		stop();
		logList.clear();
	}
	
	//Add a new class to the list of classes to log
	//This probably shouldn't be called while logging is in processes 
	public void add(Logable l) {
		logList.add(l);
	}
	
	//Start Logging - This has no effect if logging is already active
	public void start() {
		if(logThread == null) {
			logThread = new LogThread(logList);		
			logThread.start();
		}
	}
	
	//Stop Logging
	public void stop() {
		if(logThread != null) {
			logThread.running = false;			
			logThread = null;
		}		
	}
	
	//Handle the actual logging
	private class LogThread extends Thread{
		
		public boolean running = true;
		List<Logable> logList;
		private BufferedWriter outputStream;
		
		public LogThread(List<Logable> logList) {
			this.logList = logList;
		}
		
		@Override
		public void run() {			
			long startTime;
			
			try {
				//Open the file
				outputStream = new BufferedWriter(new FileWriter("/log/" + new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss'.csv'").format(new Date())));
				
				outputStream.write("timestamp,");
				for (Logable l : logList) {
					outputStream.write(String.format("%s,", l.GetColumNames()));
				}
				outputStream.newLine();
				
				while(running)
				{
					startTime = System.nanoTime();
					outputStream.write(startTime/1000000 + ",");
					//Log Data
					for (Logable l : logList) {
						outputStream.write(String.format("%s,", l.log()));
					}
					outputStream.newLine();						
					try {
						Thread.sleep((System.nanoTime() - startTime)/1000000  + 30000); 
					} catch (InterruptedException e) {
					}
				}
				//Close the file
				outputStream.close();
				
			} catch (IOException e) {
				e.printStackTrace();				
			}
		}		
	}
}
