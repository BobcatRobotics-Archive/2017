package org.usfirst.frc.team177.auto;

import org.usfirst.frc.team177.robot.Talon;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ShootFuel extends Autonomous {
	private final double SHOOTER_RPMS = 1000.0f;


	private Talon shooterLeft1;
	private Talon shooterLeft2;
	private Talon shooterRight1;
	private Talon shooterRight2;
	private double shootTime = 0.0;
	private int isShooting = 0;

	@Override
	public void autoInit() {
		double shoot = SmartDashboard.getDouble("Shooter Time");
		this.shootTime = shoot;
	}

	@Override
	public void autoPeriodic() {
    	long currentTime = System.currentTimeMillis();
    	long currentDuration = currentTime - autoStartTime;	
    	
    	if (isShooting  == 0 ) {
			shooterLeft1.setSpeed(SHOOTER_RPMS);
			shooterLeft2.setSpeed(SHOOTER_RPMS);
			shooterRight1.setSpeed(SHOOTER_RPMS);
			shooterRight2.setSpeed(SHOOTER_RPMS);
			isShooting = 1;
    	} else {
    		if (isShooting == 1 && currentDuration > shootTime) {
      			shooterLeft1.setSpeed(0.0);
    			shooterLeft2.setSpeed(0.0);
    			shooterRight1.setSpeed(0.0);
    			shooterRight2.setSpeed(0.0);
    			isShooting = 2;
    			
    			left.reset();
    			right.reset();
      		}
     	}
    	
    	if (isShooting == 2) {
			double ldist = left.getDistance();
			//double rdist = right.getDistance(); 
			
			if (ldist < 60)
				drive.drive(0.3, 0.3);
			else
				drive.drive(0.0, 0.0);
    	}
	}
	
	public void setShooters(Talon l1,Talon l2,Talon r1,Talon r2) {
		shooterLeft1 = l1;
		shooterLeft2 = l2;
		shooterRight1 = r1;
		shooterRight2 = r2;
	}
}
