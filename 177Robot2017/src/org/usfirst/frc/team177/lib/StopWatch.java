package org.usfirst.frc.team177.lib;

public class StopWatch {
	private long endTime = 0L;
	private long msecsIn = 0L;

	public StopWatch() {
		super();
	}

	public void setWatchInSeconds(long secs) {
		msecsIn = secs * 1000L;
		reset();
	}

	public void setWatchInMillis(long millisecs) {
		msecsIn = millisecs;
		reset();
	}

	public void reset() {
		endTime = msecsIn + System.currentTimeMillis();
	}

	public void stop() {
		endTime = 0L;
	}

	public boolean hasExpired() {
		return (System.currentTimeMillis() > endTime);
	}

}
