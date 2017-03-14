package org.usfirst.frc.team177.auto;

public class DropGearStraight extends DropGear {

	@Override
	public void autoInit() {
		super.autoInit();

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
			if (shouldStop(distance1,driveTime)) {
				driveTrain.stop();
				driveTime.setWatchInSeconds(1.0);
				autoStep++;
			}
		}
		if (autoStep == 1) {
			grabber.setSpeed(-0.5);
			pickup.set(true);
			driveTrain.drive(0.3, 0.3);
			if (driveTime.hasExpired()) {
				driveTime.setWatchInSeconds(1.5);
				autoStep++;
			}
		}
		if (autoStep == 2) {
			driveTrain.stop();
			driveTime.stop();
			grabber.setSpeed(0);
			pickup.set(false);
			autoStep++;
		}
	}

}
