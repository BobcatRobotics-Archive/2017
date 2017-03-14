package org.usfirst.frc.team177.auto;

/**
 * This class is used in Autonomous Mode to drive straight to the gear drop
 * station 
 * 
 * @author frc177
 *
 */
public class DriveBackwards extends DropGear {
	private boolean automode;

	public DriveBackwards() {
		super();
		automode = true;
	}

	@Override
	public void autoInit() {
		logger.log("Drop gear distance is " + distance1);
		
		driveTrain.setLeftPower(INITIAL_LEFT_POWER_FORWARD);
		driveTrain.setRightPower(INITIAL_RIGHT_POWER_FORWARD);

		// Set Timers - Base Timers (driveTime, watch) set in DropGear

	}

	@Override
	public void autoPeriodic() {
		if (automode) {
			if (watch.hasExpired()) {
				watch.reset();
				adjustDriveStraight();
			}
			driveTrain.drive();
			if (shouldStop(distance1, driveTime)) {
				automode = false;
				driveTrain.stop();
				driveTime.stop();
			}
		}
	}
}
