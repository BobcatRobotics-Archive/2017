package org.usfirst.frc.team177.auto;

import org.usfirst.frc.team177.lib.StopWatch;

public class DropGearLeftRight extends DropGear  {
	StopWatch displayData = new StopWatch();
	private boolean turnLeft = true;
	private double startingYaw = 0.0;

	private DropGearLeftRight() {
		super();
	}

	public DropGearLeftRight(boolean turnLeft) {
		this();
		this.turnLeft = turnLeft;
	}

	@Override
	public void autoInit() {
		super.autoInit();
		if (!turnLeft)
			angleToTurn = 180 - angleToTurn; // Adjust angle away from 180
		startingYaw = gyro.getYaw();
		logger.log("auto init called. (left, angleToTurn)" + turnLeft + ", " + angleToTurn);
		logger.log("auto init called. startingYaw " + startingYaw);
	
		driveTrain.setLeftPower(INITIAL_LEFT_POWER_BACKWARD);
		driveTrain.setRightPower(INITIAL_RIGHT_POWER_BACKWARD);

		// Set Timers - Base Timers (driveTime, watch) set in DropGear
		displayData.setWatchInMillis(500);
	}

	@Override
	public void autoPeriodic() {
		if (autoStep == 0) {
			if (watch.hasExpired()) {
				watch.reset();
				adjustDriveStraight();
			}
			driveTrain.drive();
			if (shouldStop(distance1, driveTime)) {
				displayData.reset();
				logger.log("step 0. encoder distances " + driveTrain.getLeftDistance() + ", " + driveTrain.getRightDistance());
				autoStep++;
			}
		}
		if (autoStep == 1) {
			double newAngle = startingYaw + angleToTurn;
			logger.log("step 1. start yaw, current yaw " + startingYaw + ", " + gyro.getYaw() );
			logger.log("step 1. turning to angle " + newAngle);
			gyro.displayData();
			gyro.turnToAngle(newAngle);
			driveTime.setWatchInMillis(1500);
			autoStep++;
		}
		if (autoStep == 2) {
			double rate = gyro.getRotateToAngleRate();

			if (displayData.hasExpired()) {
				logger.log("step 2. rate is " + rate);
				gyro.displayData();
				displayData.reset();
			}
			if (turnLeft)
				driveTrain.drive(0.0,rate);
			else
				driveTrain.drive(rate,0.0);
			if (driveTime.hasExpired()) {
				logger.log("step 2. encoder distances " + driveTrain.getLeftDistance() + ", " + driveTrain.getRightDistance());
				logger.log("step 2. final yaw is  " + gyro.getYaw());
				gyro.stopTurn();
				driveTrain.reset();
				driveTrain.setLeftPower(INITIAL_LEFT_POWER_BACKWARD);
				driveTrain.setRightPower(INITIAL_RIGHT_POWER_BACKWARD);
				driveTime.setWatchInSeconds(3.0);
				watch.reset();
				autoStep++;
			}
		}
		if (autoStep == 3) {
			if (watch.hasExpired()) {
				watch.reset();
				adjustDriveStraight();
			}
			driveTrain.drive();
			if (shouldStop(distance2, driveTime)) {
				logger.log("step 3. encoder distances " + driveTrain.getLeftDistance() + ", " + driveTrain.getRightDistance());
				driveTime.setWatchInSeconds(3);
				autoStep++;
			}
		}
		if (autoStep == 4) {
			grabber.setSpeed(-0.5);
			pickup.set(true);
			driveTrain.drive(0.4,0.4);
			if (driveTime.hasExpired()) {
				driveTime.setWatchInSeconds(1.5);
				autoStep++;
			}
		}
		if (autoStep == 5) {
			driveTrain.stop();
			driveTime.stop();
			grabber.setSpeed(0);
			pickup.set(false);
			autoStep++;
		}
	}
}
