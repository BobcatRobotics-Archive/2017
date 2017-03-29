package org.usfirst.frc.team177.robot;

import org.usfirst.frc.team177.lib.SmartPID;

import com.ctre.CANTalon;

public class Talon extends CANTalon {

	public Talon(int canID,boolean isClosedLoop) {
		this(canID,isClosedLoop,false);
	}
	
	public Talon(int canID, boolean isClosedLoop, boolean reverseSensor) {
		super(canID);
		enableBrakeMode(false); /* coast mode */
		if (isClosedLoop) {
			setClosedLoop(reverseSensor);
		}
		else {
			setDirect();
		}
		
	}
	
	private void setDirect() {
		changeControlMode(TalonControlMode.PercentVbus);
	}

	private void setClosedLoop(boolean reverseSensor) {
  		changeControlMode(TalonControlMode.Speed);
  		setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		reverseSensor(reverseSensor);
	    /* set the peak and nominal outputs, 12V means full */
		configNominalOutputVoltage(+0.0f, -0.0f);
		configPeakOutputVoltage(+12.0f, -12.0f);
		
	    /* set closed loop gains in slot0 */
		/* Set Default Values */
		setProfile(0);
		setF( 0.1097);
		setP(0.22);
		setI(0.0); 
		setD(0.0);
	}
	
	public void setSpeed(double speed) {
		//if (isClosedLoop)
		//	speed /= 600.0;
		set(speed);
	}
	
	/**
	public double getSpeed() {
		return super.getSpeed();
	}
	*/
	
	public void stop() {
		set(0.0);
	}
	
	public void setPIDParameters(SmartPID pid)  {
		setProfile(0);
		setF(pid.getFF());
		setP(pid.getP());
		setI(pid.getI()); 
		setD(pid.getD());
	}
}
