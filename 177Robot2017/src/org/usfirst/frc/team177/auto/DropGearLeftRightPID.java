package org.usfirst.frc.team177.auto;

import org.usfirst.frc.team177.lib.SmartDash;
import org.usfirst.frc.team177.lib.SmartPID;
import org.usfirst.frc.team177.lib.StopWatch;
import org.usfirst.frc.team177.robot.Talon;

import edu.wpi.first.wpilibj.Victor;

public class DropGearLeftRightPID extends DropGear  {
	//private static final double LEFT_RIGHT_DRIVE_FORWARDS = -0.4;
	StopWatch displayData = new StopWatch();
	private boolean turnLeft = true;
	//private double startingYaw = 0.0;
	
	// For Shooting
	private Talon shooterLeft1;
	private Talon shooterLeft2;
	private Talon shooterRight1;
	private Talon shooterRight2;
	private double [] shooterRPM = { 0.0, 0.0, 0.0, 0.0 };
	private StopWatch shootTimer= new StopWatch()  ;
	private boolean startShooters = true;
	private Victor helix;
	private Victor ballGrabber; 

	// For PID  Control
	private static final double DEGREE_TOLERANCE = 2.0;
	private static final double ANGLE_PERCENT_ADJUST = 2.0; // Adjust every 2 degrees
	private static final double CORRECTION = 0.95;
	private double maxPower = 0.5;
	private double currentPower = 0.5;
	//private double correction_factor = 1.00;
	private double startAngle = 0.0;
	private double finalAngle = 0.0;
	private double totalAngleTurn = 0.0;
	private double previousAngle;

	public void setShooters(Talon l1,Talon l2,Talon r1,Talon r2) {
		shooterLeft1 = l1;
		shooterLeft2 = l2;
		shooterRight1 = r1;
		shooterRight2 = r2;
	}

	public void setHelix(Victor helix) {
		this.helix = helix;
	}
	
	public void setBallGrabber(Victor ballGrabber) {
		this.ballGrabber = ballGrabber;
	}
	
	private DropGearLeftRightPID() {
		super();
	}

	public DropGearLeftRightPID(boolean turnLeft) {
		this();
		this.turnLeft = turnLeft;
	}

	private void setPIDAngle(double powerLimit,double currentYaw,double finalYaw) {
		logger.log("setPIDAngle() called. (left, powerLimit, currentYaw, finalYaw) " + turnLeft + ", " + powerLimit + ", " + currentYaw + ", " + finalYaw);
		maxPower = powerLimit;
		currentPower = maxPower;
		startAngle = currentYaw;
		previousAngle = currentYaw;
		finalAngle = finalYaw;
		totalAngleTurn = Math.abs(currentYaw - finalYaw);
		logger.log("setPIDAngle() called. totalAngleTurn " + totalAngleTurn);
	}
	
	private boolean adjustDriveAngle() {
		double currentYaw = gyro.getYaw();
		if (Math.abs(currentYaw - finalAngle) < DEGREE_TOLERANCE) {
			return true;
		}
		// Check overshoot 
		boolean overshoot = false;
		if (turnLeft && currentYaw > finalAngle)
			overshoot = true;
		else
		if (!turnLeft && currentYaw < finalAngle)
			overshoot = true;
		
		double angleTurned = Math.abs(previousAngle - currentYaw);
		previousAngle = currentYaw;
		double factor = Math.round(angleTurned / ANGLE_PERCENT_ADJUST );
		if (factor > 0.0)
			currentPower *= (Math.pow(CORRECTION,factor));
		logger.log("adjustDriveAngle()  angleTurned, currentPower " + angleTurned + ", " + currentPower );
	
		double leftPower = currentPower;
		double rightPower = currentPower;
		// TODO Adjust for overshoot
		if (turnLeft) 
			leftPower *= -1.0;
		else
			rightPower *= -1.0;
		logger.log("adjustDriveAngle() leftPower, rightPower " + leftPower + ", " + rightPower );
		driveTrain.setLeftPower(leftPower);
		driveTrain.setRightPower(rightPower);
		return false;
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
		//startingYaw = gyro.getYaw();
		logger.log("autoInit() called. (left, angleToTurn) " + turnLeft + ", " + angleToTurn);
		//logger.log("autoInit() called. startingYaw " + startingYaw);
	
		driveTrain.setLeftPower(INITIAL_LEFT_POWER_BACKWARD);
		driveTrain.setRightPower(INITIAL_RIGHT_POWER_BACKWARD);

		shooterRPM = dashboard.getShooterRPMS();
		logger.log("RPM Left Lower " + shooterRPM[0]);
		logger.log("RPM Left Upper " + shooterRPM[1]);
		logger.log("RPM Right Lower " + shooterRPM[2]);
		logger.log("RPM Right Upper " + shooterRPM[3]);	

		SmartPID pid = dashboard.getPID();
		logger.log("shooter pid (FF, P,I,D) " + pid.getFF() + ", " + pid.getP() + ", " + pid.getI() + ", " + pid.getD());
		shooterLeft1.setPIDParameters(pid);
		shooterLeft2.setPIDParameters(pid);
		shooterRight1.setPIDParameters(pid);
		shooterRight2.setPIDParameters(pid);
	
		// Set Timers - Base Timers (driveTime, watch) set in DropGear
		displayData.setWatchInMillis(100);
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
			double currentAngle = gyro.getYaw();
			double angle = currentAngle + angleToTurn;
			double newAngle = adjustAngleChange(angle);
			logger.log("step 1. current yaw, turning to angle " + currentAngle + ", " + newAngle );
			setPIDAngle(0.5, currentAngle, newAngle);
			dashboard.displayData(gyro);
			//gyro.turnToAngle(newAngle);
			driveTime.setWatchInMillis(2500);
			autoStep++;
		}
		if (autoStep == 2) {
			//double rate = gyro.getRotateToAngleRate();
			boolean hitAngle = adjustDriveAngle();

			if (displayData.hasExpired()) {
				//logger.log("step 2. rate = " + rate + " currentYaw = " + gyro.getYaw());
				dashboard.displayData(gyro);
				displayData.reset();
			}
			
			driveTrain.drive();
			
			if (driveTime.hasExpired() || hitAngle) {
				logger.log("step 2. encoder distances " + driveTrain.getLeftDistance() + ", " + driveTrain.getRightDistance());
				logger.log("step 2. final yaw is  " + gyro.getYaw());
				dashboard.displayData(gyro);
				gyro.stopTurn();
				driveTrain.reset();
				driveTrain.setLeftPower(INITIAL_LEFT_POWER_BACKWARD);
				driveTrain.setRightPower(INITIAL_RIGHT_POWER_BACKWARD);
				driveTime.setWatchInMillis(1000);
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
			//gearGrabber.setSpeed(-0.5);
			driveTrain.stop();
			gearPickup.set(true);
			if (driveTime.hasExpired()) {
				driveTime.setWatchInSeconds(3);
				driveTrain.reset();
				driveTrain.setLeftPower(INITIAL_LEFT_POWER_FORWARD);
				driveTrain.setRightPower(INITIAL_RIGHT_POWER_FORWARD);
				autoStep++;
			}
		}
		if (autoStep == 5) {
			if (watch.hasExpired()) {
				watch.reset();
				adjustDriveStraight();
			}
			driveTrain.drive();
			if (shouldStop(distance3,driveTime)) {
				//driveTime.setWatchInSeconds(1.5);
				driveTrain.reset();
				autoStep++;
			}
		}
		if (autoStep == 6) {
			double currentAngle = gyro.getYaw();
			double changeAngle = 0.0;
			if (turnLeft) {
				changeAngle = currentAngle  - angleToTurn2;
			} else {
				changeAngle = currentAngle  + angleToTurn2;
			}
			double newAngle = adjustAngleChange(changeAngle);
			logger.log("step 6. current yaw, turning to angle " + currentAngle + ", " + newAngle );
			//logger.log("step 6. turning to angle " + newAngle);
			dashboard.displayData(gyro);
			gyro.turnToAngle(newAngle);
			driveTime.setWatchInMillis(2000);
			autoStep++;
		}
		if (autoStep == 7) {
			double rate = gyro.getRotateToAngleRate();

			if (displayData.hasExpired()) {
				logger.log("step 7. rate = " + rate + " currentYaw = " + gyro.getYaw());
				dashboard.displayData(gyro);
				displayData.reset();
			}
			
			driveTrain.drive(rate * -1.0, rate);
			
			if (driveTime.hasExpired() /*gyro.hasStopped()*/) {
				logger.log("step 7. encoder distances " + driveTrain.getLeftDistance() + ", " + driveTrain.getRightDistance());
				logger.log("step 7. final yaw is  " + gyro.getYaw());
				dashboard.displayData(gyro);
				gyro.stopTurn();
				driveTrain.reset();
				driveTrain.setLeftPower(INITIAL_LEFT_POWER_FORWARD);
				driveTrain.setRightPower(INITIAL_RIGHT_POWER_FORWARD);
				driveTime.setWatchInSeconds(3);
				watch.reset();
				autoStep++;
			}		
		}
		if (autoStep == 8) {
			if (watch.hasExpired()) {
				watch.reset();
				adjustDriveStraight();
			}
			if (startShooters) {
				logger.log("step 8. setting shooters on");
				shooterLeft1.setSpeed(shooterRPM[0]);
				shooterLeft2.setSpeed(shooterRPM[1]);
				shooterRight1.setSpeed(shooterRPM[2]);
				shooterRight2.setSpeed(shooterRPM[3]);
				startShooters = false;
			}
			if (displayData.hasExpired()) {
				displayData.reset();
				logger.log("step 8. speeds " + format(shooterLeft1.getSpeed(),shooterLeft2.getSpeed(),shooterRight1.getSpeed(),shooterRight2.getSpeed()));
			}

			driveTrain.drive();
			if (shouldStop(distance4, driveTime)) {
				logger.log("step 8. encoder distances " + driveTrain.getLeftDistance() + ", " + driveTrain.getRightDistance());
				driveTime.setWatchInMillis(2000);
				shootTimer.setWatchInSeconds(10);
				driveTrain.stop();
				autoStep++;
			}
		}
		if (autoStep == 9) {
			helix.set(0.7);
			ballGrabber.set(1.0);
			if (shootTimer.hasExpired()) {
				autoStep++;
			}
		}
		if (autoStep == 10) {
			shooterLeft1.stop();
			shooterLeft2.stop();
			shooterRight1.stop();
			shooterRight2.stop();
			driveTrain.stop();
			driveTime.stop();
			ballGrabber.setSpeed(0);
			gearGrabber.setSpeed(0);
			gearPickup.set(false);
			autoStep++;
		}
	}
	
	private String format(double ld, double rd, double lp, double rp) {
		return String.format("%9.2f %9.2f %9.2f %9.2f", ld, rd, lp, rp);
	}
	
	private double adjustAngleChange(double angle) {
		double newAngle = angle;
		if (angle > 180) {
			newAngle -= 360;
		} else if (angle < -180) {
			newAngle += 360;
		}
		return newAngle;
	}
}
