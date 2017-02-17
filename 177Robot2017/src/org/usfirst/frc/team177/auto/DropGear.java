package org.usfirst.frc.team177.auto;

import org.usfirst.frc.team177.lib.RioLogger;
import org.usfirst.frc.team177.robot.DriveChain;
import org.usfirst.frc.team177.robot.GrayHill;

/** 
 * This class is used in Autonomous Mode to
 * drive straight to the gear drop station and
 * drop the gear
 * 
 * @author frc177
 *
 */
public class DropGear {
	//private final String AUTO_FULL = "afull";
	//private final String AUTO_RAMP = "aramp";

	private static final long SAMPLE_RATE = 50L; /* 50 milliseconds = 20 / seconds */
	private static final double INCREASE_CORRECTION = 1.02;
	private static final double DECREASE_CORRECTION = 0.98;
	private double leftPower;
	private double rightPower;
	private int sample_loop = 1;
	private long autoStartTime;
	private boolean automode;
	//private String autoMode = null;

	RioLogger logger = new RioLogger();
	private GrayHill left;
	private GrayHill right;
	DriveChain drive;
	
	public DropGear() {
		super();
		
		leftPower = 0.5;
		rightPower = 0.5;
		automode = true;
  		autoStartTime = System.currentTimeMillis();
  		 
	}
	
	public void setEncoders (GrayHill left, GrayHill right) {
		this.left = left;
		this.right = right;
	}
	
	public void setDrive (DriveChain drive) {
		this.drive = drive;
	}
	
	
    public double getLeftPower() {
		return leftPower;
	}


	public double getRightPower() {
		return rightPower;
	}


	public void autonomousPeriodic() {
    	long currentTime = System.currentTimeMillis();
    	long currentDuration = currentTime - autoStartTime;	
    	
     	if (automode) {
	    	if (currentDuration > (SAMPLE_RATE * sample_loop)) {
	    		double ld = left.getDistance();
	    		double rd = right.getDistance();
	    		sample_loop++;
	    		logger.log(format(ld,rd,leftPower,rightPower));
	    		if (ld > rd) {
	    			rightPower *= INCREASE_CORRECTION;
	    			leftPower *= DECREASE_CORRECTION;
	    		} else 
		   		if (ld < rd) {
	    			leftPower *= INCREASE_CORRECTION;
	    			rightPower *= DECREASE_CORRECTION;
		   		}
	    	}
    	}
     	if (currentDuration < 5000L ) {
    		drive.drive(leftPower,rightPower);
    	} else {
    		automode = false;
    		drive.drive(-0.1, -0.1);
    		drive.stop();
       }
   	
    }
    
    private String format(double ld,double rd,double lp, double rp) {
    	return String.format("%5.2f %5.2f %1.5f %1.5f", ld,rd,lp,rp);
    }
    
    /***
    public void autoRamp (long currentDuration) {
    	   
     	if (currentDuration < 250L ) {
    		driveTrain.drive(0.2,0.2);
    	}
    	else
    	if (currentDuration < 500L ) {
    		driveTrain.drive(0.3,0.3);
    	}
    	else
    	if (currentDuration < 750L ) {
    		driveTrain.drive(0.4,0.4);
    	}
    	else
    	if (currentDuration < 1000L ) {
    		driveTrain.drive(0.5,0.5);
    	}
    	else
 		if (currentDuration > 3000L ) {
    		driveTrain.stop();
       }
    }
    */

}
