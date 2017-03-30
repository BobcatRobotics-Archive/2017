package org.usfirst.frc.team177.auto;

import org.usfirst.frc.team177.lib.SmartDash;
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
	protected double distance2 = 0.0;
	protected double distance3 = 0.0;
	protected double distance4 = 0.0;
	protected double angleToTurn = 0.0;
	protected double angleToTurn2 = 0.0;
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
		distance1 = dashboard.getValue(SmartDash.AUTO_DISTANCE_1);
		distance2 = dashboard.getValue(SmartDash.AUTO_DISTANCE_2);
		distance3 = dashboard.getValue(SmartDash.AUTO_DISTANCE_3);
		distance4 = dashboard.getValue(SmartDash.AUTO_DISTANCE_4);
		angleToTurn = dashboard.getValue(SmartDash.AUTO_TURN_ANGLE_1);
		angleToTurn2 = dashboard.getValue(SmartDash.AUTO_TURN_ANGLE_2);
		//logger.writeLog();
		logger.log("DropGear autoInit() " + distance1 + ", " + distance2 + ", " + distance3 + ", " + distance4);
		logger.log("DropGear autoInit() " + angleToTurn + ", " + angleToTurn2);

		driveTrain.reset();

		autoStep = 0;
		prevLeftDistance = 0.0;
		prevRightDistance = 0.0;

		// Set Timers
		driveTime.setWatchInSeconds(3);
		watch.setWatchInMillis(SAMPLE_RATE);
	}

	public abstract void autoPeriodic();
}
