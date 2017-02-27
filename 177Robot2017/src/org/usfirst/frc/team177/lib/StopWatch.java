package org.usfirst.frc.team177.lib;

import edu.wpi.first.wpilibj.Timer;

public class StopWatch {
	private Timer watch = new Timer();
	private double endTime = 0L;

	public StopWatch() {
		super();
	}
	
	public StopWatch(double millisecs) {
		endTime = millisecs;
	}
	
	public void setWatchInSeconds(double secs) {
		setWatchInMillis(secs * 1000L);
	}
	
	public void setWatchInMillis(double millisecs) {
		endTime = millisecs;
		watch.reset();
		watch.start();
	}
	
	public void reset() {
		watch.reset();
		watch.start();
	}
	
	public void stop() {
		watch.stop();
	}
	
	public boolean hasExpired() {
		boolean expire = false;
		if (watch.get() > endTime )
			expire = true;
		return expire;
	}
	
}
