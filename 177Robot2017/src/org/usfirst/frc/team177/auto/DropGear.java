package org.usfirst.frc.team177.auto;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/** 
 * This class is used in Autonomous Mode to
 * drive straight to the gear drop station and
 * drop the gear
 * 
 * @author frc177
 *
 */
public class DropGear extends Autonomous {
	private double distance;
	private int sample_loop = 1;
	private boolean automode;
	private double drivingTime = 3000.0; /* 3 seconds */

	public DropGear() {
		super();
		
		leftPower = 0.60;
		rightPower = 0.46;
		automode = true;
	}
	/**
	public void setDistance(double distance) {
		this.distance = distance;
	}
	*/
	@Override
	public void autoInit() {
		String dg= SmartDashboard.getString("Drop Gear Distance");
		double gearDistance = new Double(dg);
		this.distance = gearDistance;
		logger.log("Drop gear distance is " + gearDistance);
		left.reset();
		right.reset();
		prevLeftDistance = 0.0;
		prevRightDistance = 0.0;
	}
	
	@Override
	public void autoPeriodic() {
    	long currentTime = System.currentTimeMillis();
    	long currentDuration = currentTime - autoStartTime;	
   	
     	if (automode) {
	    	if (currentDuration > (SAMPLE_RATE * sample_loop)) {
	    		adjustDriveStraight();
	    		sample_loop++;
	    	}
    	}
     	if ((left.getDistance() < distance && right.getDistance() < distance ) &&
     		(currentDuration < drivingTime))	{
    		drive.drive(leftPower,rightPower);
    	} else {
    		automode = false;
     		drive.stop();
       }
    }

}
