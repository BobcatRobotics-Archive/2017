package org.usfirst.frc.team177.auto;

import org.usfirst.frc.team177.lib.SmartDash;

public class DropGearStraight extends DropGear {
	private static final double LEFT_RIGHT_DRIVE_FORWARDS = -0.4;
	private double driveDistance = 0.0;

	@Override
	public void autoInit() {
		super.autoInit();
		driveDistance = dashboard.getValue(SmartDash.AUTO_STRAIGHT_DISTANCE);

		driveTrain.setLeftPower(INITIAL_LEFT_POWER_BACKWARD);
		driveTrain.setRightPower(INITIAL_RIGHT_POWER_BACKWARD);

		// Set Timers - Base Timers (driveTime, watch) set in DropGear
	}

	@Override
	public void autoPeriodic() {
		if (autoStep == 0) {
			if (watch.hasExpired()) {
				watch.reset();
				adjustDriveStraight();
			}
			driveTrain.drive();
			if (shouldStop(driveDistance,driveTime)) {
				driveTrain.stop();
				driveTime.setWatchInSeconds(1);
				autoStep++;
			}
		}
		if (autoStep == 1) {
			gearGrabber.setSpeed(-0.5);
			gearPickup.set(true);
			driveTrain.drive(LEFT_RIGHT_DRIVE_FORWARDS, LEFT_RIGHT_DRIVE_FORWARDS);
			if (driveTime.hasExpired()) {
				autoStep++;
			}
		}
		if (autoStep == 2) {
			driveTrain.stop();
			driveTime.stop();
			gearGrabber.setSpeed(0);
			gearPickup.set(false);
			autoStep++;
		}
	}

}
