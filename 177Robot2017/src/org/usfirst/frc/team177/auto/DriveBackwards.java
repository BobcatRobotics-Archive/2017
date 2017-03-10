package org.usfirst.frc.team177.auto;

import org.usfirst.frc.team177.lib.StopWatch;

/**
 * This class is used in Autonomous Mode to drive straight to the gear drop
 * station 
 * 
 * @author frc177
 *
 */
public class DriveBackwards extends Autonomous {
	private StopWatch driveTime = new StopWatch();
	private double distance;
	private boolean automode;

	public DriveBackwards() {
		super();
		driveTrain.setLeftPower(INITIAL_LEFT_POWER);
		driveTrain.setRightPower(INITIAL_RIGHT_POWER);
		automode = true;
	}

	/**
	 * public void setDistance(double distance) { this.distance = distance; }
	 */
	@Override
	public void autoInit() {
		distance = dashboard.getGearDistance();
		logger.log("Drop gear distance is " + distance);
		
		prevLeftDistance = 0.0;
		prevRightDistance = 0.0;

		// Set Timers
		watch.reset();
		driveTime.setWatchInSeconds(3.0);
	}

	@Override
	public void autoPeriodic() {

		if (automode) {
			if (watch.hasExpired()) {
				watch.reset();
				adjustDriveStraight();
			}
			driveTrain.drive();
			if (shouldStop(distance, driveTime)) {
				automode = false;
				driveTrain.stop();
				driveTime.stop();
			}
		}
	}
}
