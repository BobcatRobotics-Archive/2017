package org.usfirst.frc.team177.lib;

import edu.wpi.first.wpilibj.Timer;

public class StopWatch {
	private Timer timer = new Timer();
	private double endTime = 0L;
	private boolean inSeconds = true;
	public double secondsIn = 0.0;
	public double msecsIn = 0.0;

	public StopWatch() {
		super();
	}
	
	/*
	public StopWatch(double millisecs) {
		endTime = millisecs;
	} */
	
	public void setWatchInSeconds(double secs) {
		endTime = secs;
		inSeconds = true;
		secondsIn = secs;
		reset();
	}
	
	public void setWatchInMillis(double millisecs) {
		endTime = millisecs + System.currentTimeMillis();
		inSeconds = false;
		msecsIn = millisecs;
		reset();
	}
	
	public void reset() {
		if (inSeconds) {
			endTime = secondsIn;
			timer.reset();
			timer.start();

		} else {
			endTime = msecsIn + System.currentTimeMillis();
		}
	}
	
	public void stop() {
		timer.stop();
	}
	
	public boolean hasExpired() {
		boolean expire = false;
		if (inSeconds) {
			if (timer.get() > endTime)
				expire = true;
		} else {
			if (System.currentTimeMillis() > endTime)
				expire = true;
		}
		return expire;
	}
	
}
