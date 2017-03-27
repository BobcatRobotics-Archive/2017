package org.usfirst.frc.team177.auto;

import org.usfirst.frc.team177.lib.SmartDash;
import org.usfirst.frc.team177.lib.SmartPID;
import org.usfirst.frc.team177.lib.StopWatch;

public class DropGearLeftRight extends DropGear  {
	private static final double LEFT_RIGHT_DRIVE_FORWARDS = -0.4;
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
		logger.log("autoInit() called. gyro is calibrating  " + gyro.isCalibrating());
		
		double pidP = dashboard.getValue(SmartDash.GYRO_PID_P);
		double pidI = dashboard.getValue(SmartDash.GYRO_PID_I);
		double pidD = dashboard.getValue(SmartDash.GYRO_PID_D);
		double tolerance = dashboard.getValue(SmartDash.GYRO_DEGREE_TOLERANCE);
		gyro.setGyroPID(new SmartPID(0.0,pidP,pidI,pidD));
		gyro.setToleranceDegrees(tolerance);
		gyro.initTurnController();
		
		if (!turnLeft)
			angleToTurn = angleToTurn * -1;
		startingYaw = gyro.getYaw();
		logger.log("autoInit() called. (left, angleToTurn) " + turnLeft + ", " + angleToTurn);
		logger.log("autoInit() called. startingYaw " + startingYaw);
	
		driveTrain.setLeftPower(INITIAL_LEFT_POWER_BACKWARD);
		driveTrain.setRightPower(INITIAL_RIGHT_POWER_BACKWARD);

		// Set Timers - Base Timers (driveTime, watch) set in DropGear
		displayData.setWatchInMillis(250);
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
			if (newAngle > 180) {
				newAngle -= 360;
			} else if (newAngle < -180) {
				newAngle += 360;
			}
			logger.log("step 1. start yaw, current yaw " + startingYaw + ", " + gyro.getYaw() );
			logger.log("step 1. turning to angle " + newAngle);
			dashboard.displayData(gyro);
			gyro.turnToAngle(newAngle);
			driveTime.setWatchInMillis(4000);
			autoStep++;
		}
		if (autoStep == 2) {
			double rate = gyro.getRotateToAngleRate();

			if (displayData.hasExpired()) {
				logger.log("step 2. rate = " + rate + " currentYaw = " + gyro.getYaw());
				dashboard.displayData(gyro);
				displayData.reset();
			}
			
			driveTrain.drive(rate * -1.0, rate);
			
			if (driveTime.hasExpired() /*gyro.hasStopped()*/) {
				logger.log("step 2. encoder distances " + driveTrain.getLeftDistance() + ", " + driveTrain.getRightDistance());
				logger.log("step 2. final yaw is  " + gyro.getYaw());
				dashboard.displayData(gyro);
				gyro.stopTurn();
				driveTrain.reset();
				driveTrain.setLeftPower(INITIAL_LEFT_POWER_BACKWARD);
				driveTrain.setRightPower(INITIAL_RIGHT_POWER_BACKWARD);
				driveTime.setWatchInSeconds(3);
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
				driveTime.setWatchInMillis(500);
				autoStep++;
			}
		}
		if (autoStep == 4) {
			gearGrabber.setSpeed(-0.5);
			gearPickup.set(true);
			if (driveTime.hasExpired()) {
				driveTime.setWatchInSeconds(2);
				autoStep++;
			}
		}
		if (autoStep == 5) {
			driveTrain.drive(LEFT_RIGHT_DRIVE_FORWARDS,LEFT_RIGHT_DRIVE_FORWARDS);
			if (driveTime.hasExpired()) {
				//driveTime.setWatchInSeconds(1.5);
				autoStep++;
			}
		}
		if (autoStep == 6) {
			driveTrain.stop();
			driveTime.stop();
			gearGrabber.setSpeed(0);
			gearPickup.set(false);
			autoStep++;
		}
	}
}
