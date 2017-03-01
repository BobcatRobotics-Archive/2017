package org.usfirst.frc.team177.auto;

import org.usfirst.frc.team177.lib.StopWatch;

/**
 * This class is used in Autonomous Mode to drive straight to the gear drop
 * station and drop the gear
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

		leftPower = 0.60;
		rightPower = 0.46;
		automode = true;
	}

	/**
	 * public void setDistance(double distance) { this.distance = distance; }
	 */
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

		if (automode) {
			if (watch.hasExpired()) {
				watch.reset();
				adjustDriveStraight();
			}
			drive.drive(leftPower, rightPower);
			//if ((left.getDistance() > distance) || (right.getDistance() > distance) || driveTime.hasExpired()) {
			if (driveTime.hasExpired()) {
				automode = false;
				drive.stop();
				driveTime.stop();
			}
		}
	}
}
