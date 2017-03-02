package org.usfirst.frc.team177.auto;

import org.usfirst.frc.team177.lib.RioLoggerThread;
import org.usfirst.frc.team177.lib.SmartDash;
import org.usfirst.frc.team177.lib.StopWatch;
import org.usfirst.frc.team177.robot.DriveChain;
import org.usfirst.frc.team177.robot.GrayHill;

public abstract class Autonomous {

	protected static final long SAMPLE_RATE = 25L;	/** 25 milliseconds = 20times / seconds */
	private static final double INCREASE_CORRECTION = 1.05;
	private static final double DECREASE_CORRECTION = 0.95;
	private static double deadBandRange = 0.0;
	protected double prevLeftDistance = 0.0;
	protected double prevRightDistance = 0.0;
	protected double leftPower;
	protected double rightPower;
	protected StopWatch watch = new StopWatch();
	//protected long autoStartTime;

	//protected RioLoggerThread logger = RioLoggerThread.getInstance();
	protected RioLoggerThread logger = RioLoggerThread.getInstance();
	protected SmartDash dashboard = SmartDash.getInstance();
	protected GrayHill left;
	protected GrayHill right;
	DriveChain drive;

	public abstract void autoInit();

	public abstract void autoPeriodic();

	public Autonomous() {
		super();
		watch.setWatchInMillis(SAMPLE_RATE);
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

	protected void adjustDriveStraight() {
		double ldist = left.getDistance();
		double rdist = right.getDistance();
 		logger.log(format(ldist,rdist,leftPower,rightPower));
		
		double leftdiff  = ldist - prevLeftDistance;
		prevLeftDistance = ldist;
		double rightdiff = rdist - prevRightDistance;
		prevRightDistance = rdist;
		
		double ldistChk = Math.abs(leftdiff);
		double rdistChk = Math.abs(rightdiff);
		if (Math.abs(ldistChk - rdistChk) < deadBandRange) {
			return;
		}
			
		if (ldistChk > rdistChk) {
			rightPower *= INCREASE_CORRECTION;
			leftPower *= DECREASE_CORRECTION;
		} else 
   		if (ldistChk < rdistChk) {
			leftPower *= INCREASE_CORRECTION;
			rightPower *= DECREASE_CORRECTION;
   		}  	
		if (leftPower > 1.0) 
			leftPower = 1.0;
		else if (leftPower < -1.0) 
			leftPower = -1.0;
		if (rightPower > 1.0) 
			rightPower = 1.0;
		else if (rightPower < -1.0) 
			rightPower = -1.0;
	}
}
