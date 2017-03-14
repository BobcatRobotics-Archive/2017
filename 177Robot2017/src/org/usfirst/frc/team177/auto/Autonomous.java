package org.usfirst.frc.team177.auto;

import org.usfirst.frc.team177.lib.RioLoggerThread;
import org.usfirst.frc.team177.lib.SmartDash;
import org.usfirst.frc.team177.lib.StopWatch;
import org.usfirst.frc.team177.robot.DriveChain;
import org.usfirst.frc.team177.robot.NavxGyro;

public abstract class Autonomous {
	protected static final long SAMPLE_RATE = 25L;	/** 25 milliseconds = 20times / seconds */
	protected static final double INITIAL_LEFT_POWER_FORWARD = 0.60;
	protected static final double INITIAL_RIGHT_POWER_FORWARD = 0.46;
	protected static final double INITIAL_LEFT_POWER_BACKWARD = -0.60;
	protected static final double INITIAL_RIGHT_POWER_BACKWARD = -0.46;
	
	/** Variables for Drive Staight */
	private static final double INCREASE_CORRECTION = 1.05;
	private static final double DECREASE_CORRECTION = 0.95;
	private static double deadBandRange = 0.0;
	protected double prevLeftDistance = 0.0;
	protected double prevRightDistance = 0.0;
	protected StopWatch watch = new StopWatch();

	protected RioLoggerThread logger = RioLoggerThread.getInstance();
	protected SmartDash dashboard = SmartDash.getInstance();
	protected DriveChain driveTrain;
	protected NavxGyro gyro;

	public abstract void autoInit();

	public abstract void autoPeriodic();

	public Autonomous() {
		super();
	}

	public void setDrive(DriveChain drive) {
		this.driveTrain = drive;
	}

	public void setGyro(NavxGyro gyro) {
		this.gyro = gyro;
	}
	
	
	private String format(double ld, double rd, double lp, double rp) {
		return String.format("%5.2f %5.2f %1.5f %1.5f", ld, rd, lp, rp);
	}

	protected void adjustDriveStraight() {
		double ldist = driveTrain.getLeftDistance();
		double rdist = driveTrain.getRightDistance();
		double leftPower = driveTrain.getLeftPower();
		double rightPower = driveTrain.getRightPower();
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
		driveTrain.setLeftPower(leftPower);
		driveTrain.setRightPower(rightPower);
	}
	
	protected boolean shouldStop(double totalDistance,StopWatch timer) {
		boolean stop = false;
		if ((Math.abs(driveTrain.getLeftDistance()) > totalDistance) ||
			(Math.abs(driveTrain.getRightDistance()) > totalDistance))
			stop = true;
		if (timer.hasExpired())
			stop = true;
		return stop;
	}

	protected boolean shouldStopGyro() {
		return gyro.isMoving();
	}

}
