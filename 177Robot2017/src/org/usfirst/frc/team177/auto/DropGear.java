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

	public DropGear() {
		super();
		
		leftPower = 0.5;
		rightPower = 0.5;
		automode = true;
	}
	/**
	public void setDistance(double distance) {
		this.distance = distance;
	}
	*/
	@Override
	public void autoInit() {
		double gearDistance = SmartDashboard.getDouble("Drop Gear Distance");
		this.distance = gearDistance;
	}
	
	@Override
	public void autoPeriodic() {
    	long currentTime = System.currentTimeMillis();
    	long currentDuration = currentTime - autoStartTime;	
		double ldist = left.getDistance();
		double rdist = right.getDistance();
    	
     	if (automode) {
	    	if (currentDuration > (SAMPLE_RATE * sample_loop)) {
	    		adjustDriveStraight(ldist,rdist);
	    		sample_loop++;
	    	}
    	}
     	if (ldist < distance && rdist < distance ) {
    		drive.drive(leftPower,rightPower);
    	} else {
    		automode = false;
     		drive.stop();
       }
    }

   
  
}
