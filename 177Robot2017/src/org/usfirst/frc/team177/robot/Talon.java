package org.usfirst.frc.team177.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

public class Talon {

	private CANTalon talon;

	//private boolean isClosedLoop;
	//private int canID;
	
	
	private Talon() {
		super();
	}


	public Talon(int canID,boolean isClosedLoop) {
		this();
		//this.isClosedLoop = isClosedLoop;
		//this.canID = canID;
		talon = new CANTalon(canID);
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
		talon.setF(0.1097);
		talon.setP(0.22);
		talon.setI(0); 
		talon.setD(0);
	}
	
	public void setSpeed(double speed) {
		talon.set(speed);
	}
}
