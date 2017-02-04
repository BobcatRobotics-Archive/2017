package org.usfirst.frc.team177.robot;

import edu.wpi.first.wpilibj.Victor;

public class DriveChain {
	/** Inverts drive direction **/
	private static final double INVERT_MOTOR = -1.0;
	
	private Victor leftFront;
	private Victor leftMiddle;
	private Victor leftRear;
	
	private Victor rightFront;
	private Victor rightMiddle;
	private Victor rightRear;
	private boolean invertLeft = false;
	
	public DriveChain() {
		super();
	}
	
	public void setLeftMotors(int lf,int lm,int lr) {
		leftFront = new Victor(lf);
		leftMiddle = new Victor(lm);
		leftRear = new Victor(lr);
	}
	
	public void setRightMotors(int rf,int rm,int rr) {
		rightFront = new Victor(rf);
		rightMiddle = new Victor(rm);
		rightRear = new Victor(rr);
	}
	
	public void drive(double leftPower, double rightPower) {
		if (invertLeft )
			leftPower *= INVERT_MOTOR;
		else
			rightPower *= INVERT_MOTOR;
		
		leftFront.set(leftPower);
		leftMiddle.set(leftPower);
		leftRear.set(leftPower);
		
		rightFront.set(rightPower);
		rightMiddle.set(rightPower);
		rightRear.set(rightPower);
	}

	public void stop() {
		leftFront.set(0.0);
		leftMiddle.set(0.0);
		leftRear.set(0.0);
		
		rightFront.set(0.0);
		rightMiddle.set(0.0);
		rightRear.set(0.0);
	}

	public void setLeftMotorsReverse(boolean invert) {
		invertLeft = invert;
	}
}