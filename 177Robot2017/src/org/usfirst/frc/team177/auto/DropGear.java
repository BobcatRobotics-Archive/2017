package org.usfirst.frc.team177.auto;

import org.usfirst.frc.team177.lib.StopWatch;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is used in Autonomous Mode to drive straight to the gear drop
 * station and drop the gear
 * 
 * @author frc177
 *
 */
public class DropGear extends Autonomous {
	private StopWatch driveTime = new StopWatch();
	private double distance;
	private boolean automode;

	public DropGear() {
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
		String dg = SmartDashboard.getString("Drop Gear Distance");
		double gearDistance = new Double(dg);
		this.distance = gearDistance;
		logger.log("Drop gear distance is " + gearDistance);
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
			if ((left.getDistance() > distance) || (right.getDistance() > distance) || driveTime.hasExpired()) {
				automode = false;
				drive.stop();
				driveTime.stop();
			}
		}
	}
}
