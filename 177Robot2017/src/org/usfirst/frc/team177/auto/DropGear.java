package org.usfirst.frc.team177.auto;

import org.usfirst.frc.team177.lib.RioLogger;
import org.usfirst.frc.team177.robot.DriveChain;
import org.usfirst.frc.team177.robot.GrayHill;

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
	
	public void setDistance(double distance) {
		this.distance = distance;
	}

	public void autoInit() {
		// TODO Auto-generated method stub
		double gearDistance = SmartDashboard.getDouble("Drop Gear Distance");
		this.distance = gearDistance;
	}

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
