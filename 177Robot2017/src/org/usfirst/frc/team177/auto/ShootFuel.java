package org.usfirst.frc.team177.auto;

import org.usfirst.frc.team177.lib.SmartDash;
import org.usfirst.frc.team177.lib.StopWatch;
import org.usfirst.frc.team177.robot.Talon;

public class ShootFuel extends Autonomous {
	private static final long DISTANCE_AFTER_SHOOTING = 60;
	
	private Talon shooterLeft1;
	private Talon shooterLeft2;
	private Talon shooterRight1;
	private Talon shooterRight2;
	private StopWatch shootTimer;
	private StopWatch driveTimer;
	private double [] shooterRPM = { 0.0, 0.0, 0.0, 0.0 };
	private double shootTime = 0.0;
	private int shootingStep = 0;

	public void setShooters(Talon l1,Talon l2,Talon r1,Talon r2) {
		shooterLeft1 = l1;
		shooterLeft2 = l2;
		shooterRight1 = r1;
		shooterRight2 = r2;
	}
	
	@Override
	public void autoInit() {
		shootTime = dashboard.getValue(SmartDash.AUTO_SHOOT_TIME);
		logger.log("Shooter Time  is " + shootTime);
		
		shooterRPM = dashboard.getShooterRPMS();
		logger.log("RPM Left Lower " + shooterRPM[0]);
		logger.log("RPM Left Upper " + shooterRPM[1]);
		logger.log("RPM Right Lower " + shooterRPM[2]);
		logger.log("RPM Right Upper " + shooterRPM[3]);	
		
		// Set Timers
		driveTimer.setWatchInSeconds(5);
		shootTimer.setWatchInSeconds((long)shootTime);
	}

	@Override
	public void autoPeriodic() {
    	if (shootingStep  == 0 ) {
			shooterLeft1.setSpeed(shooterRPM[0]);
			shooterLeft2.setSpeed(shooterRPM[1]);
			shooterRight1.setSpeed(shooterRPM[2]);
			shooterRight2.setSpeed(shooterRPM[3]);
			shootingStep++;
    	} 
		if (shootingStep == 1 && shootTimer.hasExpired()) {
  			shooterLeft1.setSpeed(0.0);
			shooterLeft2.setSpeed(0.0);
			shooterRight1.setSpeed(0.0);
			shooterRight2.setSpeed(0.0);
			driveTrain.setLeftPower(0.3);
			driveTrain.setRightPower(0.3);
			watch.reset();
			shootingStep++;
		}
    	if (shootingStep == 2) {
    		if (watch.hasExpired()) {
				watch.reset();
				adjustDriveStraight();
			}
			driveTrain.drive();
			if (shouldStop(DISTANCE_AFTER_SHOOTING,driveTimer)) {
				shootingStep++;
				driveTrain.stop();
			}
    	}
	}
}
