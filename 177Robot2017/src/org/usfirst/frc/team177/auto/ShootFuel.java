package org.usfirst.frc.team177.auto;

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
	private long shooterRPM = 0L;
	private double shootTime = 0.0;
	private int isShooting = 0;

	public void setShooters(Talon l1,Talon l2,Talon r1,Talon r2) {
		shooterLeft1 = l1;
		shooterLeft2 = l2;
		shooterRight1 = r1;
		shooterRight2 = r2;
	}
	
	@Override
	public void autoInit() {
		shootTime = dashboard.getShooterTime();
		logger.log("Shooter Time  is " + shootTime);
		
		shooterRPM = dashboard.getShooterRPM();
		logger.log("Shooter RPM " + shooterRPM );
		
		// Set Timers
		driveTimer.setWatchInSeconds(5.0);
		shootTimer.setWatchInSeconds(shootTime);
	}

	@Override
	public void autoPeriodic() {
    	
    	if (isShooting  == 0 ) {
			shooterLeft1.setSpeed(shooterRPM);
			shooterLeft2.setSpeed(shooterRPM);
			shooterRight1.setSpeed(shooterRPM);
			shooterRight2.setSpeed(shooterRPM);
			isShooting = 1;
    	} 
		if (isShooting == 1 && shootTimer.hasExpired()) {
  			shooterLeft1.setSpeed(0.0);
			shooterLeft2.setSpeed(0.0);
			shooterRight1.setSpeed(0.0);
			shooterRight2.setSpeed(0.0);
			isShooting = 2;
			driveTrain.setLeftPower(0.3);
			driveTrain.setRightPower(0.3);
			watch.reset();
		}
    	if (isShooting == 2) {
    		if (watch.hasExpired()) {
				watch.reset();
				adjustDriveStraight();
			}
			driveTrain.drive();
			if (shouldStop(DISTANCE_AFTER_SHOOTING,driveTimer)) {
				isShooting = 3;
				driveTrain.stop();
			}
    	}
	}
}
