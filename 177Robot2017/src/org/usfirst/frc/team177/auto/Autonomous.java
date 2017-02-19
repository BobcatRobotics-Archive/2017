package org.usfirst.frc.team177.auto;

import org.usfirst.frc.team177.lib.RioLogger;
import org.usfirst.frc.team177.robot.DriveChain;
import org.usfirst.frc.team177.robot.GrayHill;

public abstract class Autonomous {

	protected static final long SAMPLE_RATE = 50L; /** 50 milliseconds = 20 / seconds */
	private static final double INCREASE_CORRECTION = 1.02;
	private static final double DECREASE_CORRECTION = 0.98;
	protected double leftPower;
	protected double rightPower;
	protected long autoStartTime;
	//private double distance;
	//private int sample_loop = 1;
	//private boolean automode;
	// private String autoMode = null;

	private RioLogger logger = new RioLogger();
	protected GrayHill left;
	protected GrayHill right;
	DriveChain drive;

	public abstract void autoInit();

	public abstract void autoPeriodic();

	public Autonomous() {
		super();
  		autoStartTime = System.currentTimeMillis();
	}

	public void setEncoders(GrayHill left, GrayHill right) {
		this.left = left;
		this.right = right;
	}

	public void setDrive(DriveChain drive) {
		this.drive = drive;
	}

	public double getLeftPower() {
		return leftPower;
	}

	public double getRightPower() {
		return rightPower;
	}

	protected String format(double ld, double rd, double lp, double rp) {
		return String.format("%5.2f %5.2f %1.5f %1.5f", ld, rd, lp, rp);
	}

	protected void adjustDriveStraight(double ldist, double rdist) {
		logger.log(format(ldist,rdist,leftPower,rightPower));
		if (ldist > rdist) {
			rightPower *= INCREASE_CORRECTION;
			leftPower *= DECREASE_CORRECTION;
		} else 
   		if (ldist < rdist) {
			leftPower *= INCREASE_CORRECTION;
			rightPower *= DECREASE_CORRECTION;
   		}  	
	}
}
