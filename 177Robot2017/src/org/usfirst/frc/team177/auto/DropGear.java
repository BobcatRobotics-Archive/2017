package org.usfirst.frc.team177.auto;

import org.usfirst.frc.team177.lib.StopWatch;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;

/**
 * This autonomous class drives straight for 3 seconds Reverses the Gear Grabber
 * Drives backwards for 1.5 seconds
 * 
 * @author bobcat177
 *
 */
public class DropGear extends Autonomous {
	private StopWatch driveTime = new StopWatch();
	private Solenoid pickup = null;
	private Victor grabber = null;

	private double distance;
	private int autoStep = 0;

	public DropGear() {
		super();
		driveTrain.setLeftPower(INITIAL_LEFT_POWER * -1.0);
		driveTrain.setRightPower(INITIAL_RIGHT_POWER * -1.0);
	}
	
	public void setPicker(Solenoid pickup) {
		this.pickup = pickup;
	}

	public void setGrabber(Victor grabber) {
		this.grabber = grabber;
	}
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
		if (autoStep == 0) {
			if (watch.hasExpired()) {
				watch.reset();
				adjustDriveStraight();
			}
			driveTrain.drive();
			if (shouldStop(distance,driveTime)) {
				autoStep = 1;
				driveTrain.stop();
				driveTime.setWatchInSeconds(1.0);
			}
		}
		if (autoStep == 1) {
			grabber.setSpeed(-0.5);
			pickup.set(true);
			driveTrain.drive(0.3, 0.3);
			if (driveTime.hasExpired()) {
				autoStep = 2;
				driveTime.setWatchInSeconds(1.5);
			}
		}
		if (autoStep == 2) {
			autoStep = 3;
			driveTrain.stop();
			driveTime.stop();
			grabber.setSpeed(0);
			pickup.set(false);
		}
	}


}
