package org.usfirst.frc.team177.lib;

import edu.wpi.first.wpilibj.Timer;

public class StopWatch {
	private Timer timer = new Timer();
	private double endTime = 0L;
	private boolean inSeconds = true;

	public StopWatch() {
		super();
	}
	
	/*
	public StopWatch(double millisecs) {
		endTime = millisecs;
	} */
	
	public void setWatchInSeconds(double secs) {
		endTime = secs;
		timer.reset();
		timer.start();
		inSeconds = true;
	}
	
	public void setWatchInMillis(double millisecs) {
		endTime = millisecs + System.currentTimeMillis();
		inSeconds = false;
		timer.reset();
		timer.start();
	}
	
	public void reset() {
		timer.reset();
		timer.start();
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
