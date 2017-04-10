package org.usfirst.frc.team177.auto;

import org.usfirst.frc.team177.lib.SmartDash;
import org.usfirst.frc.team177.lib.SmartPID;
import org.usfirst.frc.team177.lib.StopWatch;
import org.usfirst.frc.team177.robot.Talon;

import edu.wpi.first.wpilibj.Victor;

public class ShootLeftRight extends DropGear  {
	//private static final double LEFT_RIGHT_DRIVE_FORWARDS = -0.4;
	StopWatch displayData = new StopWatch();
	private boolean turnLeft = true;
	private double startingYaw = 0.0;
	private double distance_1 = 38.0;
	private double distance_2 = 50.0;
	private double distance_3 = 100.0;
	private double angle_1 = 38.0;
	
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
	
	private ShootLeftRight() {
		super();
	}

	public ShootLeftRight(boolean turnLeft) {
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

		shooterRPM = dashboard.getShooterRPMS();
		logger.log("RPM Left Lower " + shooterRPM[0]);
		logger.log("RPM Left Upper " + shooterRPM[1]);
		logger.log("RPM Right Lower " + shooterRPM[2]);
		logger.log("RPM Right Upper " + shooterRPM[3]);	

		SmartPID pid = dashboard.getPID();
		//setShootersPIDS(shooterLeft1, shooterLeft2, shooterRight1, shooterRight2, pid);
		//logger.log("shooter pid ( P,I,D)" + pid.getP() + ", " + pid.getI() + ", " + pid.getD());
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
		// Spin up shooters
		if (startShooters) {
			logger.log("step pre 0. setting shooters on");
			shooterLeft1.setSpeed(shooterRPM[0]);
			shooterLeft2.setSpeed(shooterRPM[1]);
			shooterRight1.setSpeed(shooterRPM[2]);
			shooterRight2.setSpeed(shooterRPM[3]);
			startShooters = false;
			driveTime.setWatchInMillis(2500);
		}
		// Drive away from wall
		if (autoStep == 0) {
			if (watch.hasExpired()) {
				watch.reset();
				adjustDriveStraight();
			}
			driveTrain.drive();
			if (shouldStop(distance_1, driveTime)) {
				displayData.reset();
				logger.log("step 0. encoder distances " + driveTrain.getLeftDistance() + ", " + driveTrain.getRightDistance());
				autoStep++;
			}
		}
		// Turn to boiler
		if (autoStep == 1) {
			if (!turnLeft)
				angle_1 *= -1.0;
			double angle = startingYaw + angle_1;
			double newAngle = adjustAngleChange(angle);
			logger.log("step 1. start yaw, current yaw " + startingYaw + ", " + gyro.getYaw() );
			logger.log("step 1. turning to angle " + newAngle);
			dashboard.displayData(gyro);
			gyro.turnToAngle(newAngle);
			driveTime.setWatchInMillis(2500);
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
				driveTrain.setLeftPower(INITIAL_LEFT_POWER_FORWARD);
				driveTrain.setRightPower(INITIAL_RIGHT_POWER_FORWARD);
				driveTime.setWatchInMillis(3000);
				watch.reset();
				autoStep++;
			}
		}
		// Drive to boiler
		if (autoStep == 3) {
			if (watch.hasExpired()) {
				watch.reset();
				adjustDriveStraight();
			}
			driveTrain.drive();
			if (shouldStop(distance_2, driveTime)) {
				logger.log("step 3. encoder distances " + driveTrain.getLeftDistance() + ", " + driveTrain.getRightDistance());
				driveTrain.stop();
				//driveTime.setWatchInMillis(2500);
				shootTimer.setWatchInSeconds(5);
				autoStep++;
			}
		}
	
		// Shoot
		if (autoStep == 4) {
			helix.set(0.5);
			ballGrabber.set(1.0);
			
			if (displayData.hasExpired()) {
				displayData.reset();
				logger.log("step 4. ss " + format(shooterLeft1.getSpeed(),shooterLeft2.getSpeed(),shooterRight1.getSpeed(),shooterRight2.getSpeed()));
			}

			if (shootTimer.hasExpired()) {
				autoStep++;
				//driveTime.setWatchInMillis(5000);

			}
		}
		// Drive to peg
		if (autoStep == 5) {
			driveTrain.reset();
			driveTrain.setLeftPower(INITIAL_LEFT_POWER_BACKWARD);
			driveTrain.setRightPower(INITIAL_RIGHT_POWER_BACKWARD);
			
			driveTime.setWatchInMillis(5000);
	
			autoStep++;
		}
		if (autoStep == 6) {
			if (watch.hasExpired()) {
				watch.reset();
				adjustDriveStraight();
			}
			driveTrain.drive();
			if (displayData.hasExpired()) {
				displayData.reset();
				logger.log("step 6. ss " + format(shooterLeft1.getSpeed(),shooterLeft2.getSpeed(),shooterRight1.getSpeed(),shooterRight2.getSpeed()));
			}
			if (shouldStop(distance_3, driveTime)) {
				logger.log("step 6. encoder distances " + driveTrain.getLeftDistance() + ", " + driveTrain.getRightDistance());
				autoStep++;
			}
		}

		if (autoStep == 7) {
			shooterLeft1.stop();
			shooterLeft2.stop();
			shooterRight1.stop();
			shooterRight2.stop();
			driveTrain.stop();
			driveTime.stop();
			ballGrabber.setSpeed(0);
			helix.setSpeed(0.0);
			autoStep++;
		}
	}
	
	private String format(double ld, double rd, double lp, double rp) {
		return String.format("%9.2f %9.2f %9.2f %9.2f", ld, rd, lp, rp);
	}
}
