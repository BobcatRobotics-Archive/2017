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
public abstract class DropGear extends Autonomous {
	protected StopWatch driveTime = new StopWatch();
	protected Solenoid gearPickup = null;
	protected Victor gearGrabber = null;

	protected double distance1 = 0.0;
	protected double angleToTurn = 0.0;
	protected double distance2 = 0.0;
	protected int autoStep = 0;

	public DropGear() {
		super();
	}
	
	public void setPicker(Solenoid pickup) {
		this.gearPickup = pickup;
	}

	public void setGrabber(Victor grabber) {
		this.gearGrabber = grabber;
	}

	@Override
	public void autoInit() {
		//distance = dashboard.getGearDistance();
		distance1 = dashboard.getAutoDistance1();
		distance2 = dashboard.getAutoDistance2();
		angleToTurn = dashboard.getTurnAngle();
		logger.writeLog();
		logger.log("Drop gear parameters " + distance1 + ", " + distance2 + ", " + angleToTurn);

		driveTrain.reset();
		//gyro.reset();

		autoStep = 0;
		prevLeftDistance = 0.0;
		prevRightDistance = 0.0;

		// Set Timers
		driveTime.setWatchInSeconds(3.0);
		watch.setWatchInMillis(SAMPLE_RATE);
	}

	public abstract void autoPeriodic();
}
