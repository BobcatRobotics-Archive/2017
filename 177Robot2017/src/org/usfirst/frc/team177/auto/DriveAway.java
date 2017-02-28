package org.usfirst.frc.team177.auto;

import org.usfirst.frc.team177.lib.StopWatch;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;

/**
 * This autonomous class drives straight for 3 seconds
 * Reverses the Gear Grabber
 * Drives backwards  for 2.5 seconds
 * 
 * @author bobcat177
 *
 */
public class DriveAway extends Autonomous {
	private StopWatch driveTime = new StopWatch();
	private  Solenoid pickup = null;
	private Victor grabber = null;
	
	private double distance;
	private int notMoved = 0;
	private int autoStep = 0;

	public DriveAway() {
		super();
		leftPower = -0.60;
		rightPower = -0.46;
	}

	@Override
	public void autoInit() {
		distance = dashboard.getGearDistance();
		logger.log("Drop gear distance is " + distance);
		left.reset();
		right.reset();
		prevLeftDistance = 0.0;
		prevRightDistance = 0.0;
		
		// Set Timers
		watch.reset();
		driveTime.setWatchInSeconds(3.0);
	}

	@Override
	public void autoPeriodic() {
     	if (autoStep == 0) {
	    	if (watch.hasExpired()) {
	    		watch.reset();
	    		adjustDriveStraight();
	       	   	// An artificial check for the bot hitting the wall
	    		// if count == 10 (robot has been still for 250 millisecond (25 *10)
	         	if ((Math.abs(left.getDistance() - prevLeftDistance) < 0.5 ))
	    			notMoved++;
	    	}
    		drive.drive(leftPower,rightPower);
         	if ((left.getDistance() > distance) ||
	     		(right.getDistance() > distance) ||
	         	(driveTime.hasExpired()) ||
	         	(notMoved == 10))	{
	        		autoStep = 1;
	         		drive.stop();
	         		driveTime.setWatchInSeconds(1.0);
	           }
    	}
     	if (autoStep == 1)	 {
			grabber.setSpeed(-0.5);
			pickup.set(true);
			drive.drive(0.3,0.3);
			if (driveTime.hasExpired()) {
        		autoStep = 2;	
        		driveTime.setWatchInSeconds(1.5);
			}
     	}
    	if (autoStep == 2)	 {
			drive.drive(0.3,0.3);
			if (driveTime.hasExpired()) {
        		autoStep = 3;
        		drive.stop();
        		driveTime.stop();
        		grabber.setSpeed(0);
        		pickup.set(false);
			}
     	}    	
	}
	
	public void setPicker(Solenoid pickup) {
		this.pickup = pickup;
	}

	public void setGrabber(Victor grabber) {
		this.grabber = grabber;
	}
}
