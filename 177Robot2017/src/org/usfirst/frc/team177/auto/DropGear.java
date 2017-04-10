package org.usfirst.frc.team177.auto;

import org.usfirst.frc.team177.lib.SmartDash;
import org.usfirst.frc.team177.lib.SmartPID;
import org.usfirst.frc.team177.lib.StopWatch;
import org.usfirst.frc.team177.robot.Talon;

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

	protected double adjustAngleChange(double angle) {
		double newAngle = angle;
		if (angle > 180) {
			newAngle -= 360;
		} else if (angle < -180) {
			newAngle += 360;
		}
		return newAngle;
	}

	protected void setShootersPIDS(Talon ll,Talon lu,Talon rl,Talon ru,SmartPID pid) {
		double llff = 0.026;
		double luff = 0.0245;
		double rlff = 0.0255;
		double ruff = 0.0245;
		logger.log("shooter pid (P,I,D) " + pid.getP() + ", " + pid.getI() + ", " + pid.getD());
		ll.setPIDParameters(pid);
		lu.setPIDParameters(pid);
		rl.setPIDParameters(pid);
		ru.setPIDParameters(pid);
		logger.log("shooter left lower (FF) " + llff);
		ll.setF(llff);
		logger.log("shooter left upper (FF) " + luff);
		lu.setF(luff);
		logger.log("shooter right lower (FF) " + rlff);
		rl.setF(rlff);
		logger.log("shooter right upper (FF) " + ruff);
		ru.setF(ruff);
	}

	public abstract void autoPeriodic();
}
