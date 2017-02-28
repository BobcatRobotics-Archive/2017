package org.usfirst.frc.team177.robot;

import org.usfirst.frc.team177.lib.SmartPID;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

public class Talon {

	private CANTalon talon;

	//private boolean isClosedLoop;
	//private int canID;
	private double feedForward = 0.1097;
	private double pidP = 0.22;
	private double pidI = 0.0;
	private double pidD = 0.0;
	
	private Talon() {
		super();
	}


	public Talon(int canID,boolean isClosedLoop) {
		this();
		//this.isClosedLoop = isClosedLoop;
		//this.canID = canID;
		talon = new CANTalon(canID);
		talon.enableBrakeMode(false); /* coast mode */
		if (isClosedLoop) {
			setClosedLoop();
		}
		else {
			setDirect();
		}
	}
	
	private void setDirect() {
		talon.changeControlMode(TalonControlMode.PercentVbus);
	}

	private void setClosedLoop() {
  		talon.changeControlMode(TalonControlMode.Speed);
  		talon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		talon.reverseSensor(false);
	    //_talon.configEncoderCodesPerRev(XXX), // if using FeedbackDevice.QuadEncoder
	    //_talon.configPotentiometerTurns(XXX), // if using FeedbackDevice.AnalogEncoder or AnalogPot

	    /* set the peak and nominal outputs, 12V means full */
		talon.configNominalOutputVoltage(+0.0f, -0.0f);
		talon.configPeakOutputVoltage(+12.0f, -12.0f);
	    /* set closed loop gains in slot0 */
		talon.setProfile(0);
		talon.setF(feedForward);
		talon.setP(pidP);
		talon.setI(pidI); 
		talon.setD(pidD);
	}
	
	public void setSpeed(double speed) {
		//if (isClosedLoop)
		//	speed /= 600.0;
		talon.set(speed);
	}
	
	public double getSpeed() {
		return talon.getSpeed();
	}
	
	public void stop() {
		talon.set(0.0);
		//talon.stopMotor();
	}
	
	public void setPIDParameters(SmartPID pid)  {
		feedForward = pid.getFF();
		pidP = pid.getP();
		pidI = pid.getI();
		pidD = pid.getD();
		
		talon.setF(feedForward);
		talon.setP(pidP);
		talon.setI(pidI); 
		talon.setD(pidD);
	}
}
