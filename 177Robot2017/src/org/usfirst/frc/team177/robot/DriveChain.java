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
	
	private GrayHill leftEncoder;
	private GrayHill rightEncoder;
	
	private boolean invertLeft = false;
	
	private double leftPower = 0.0;
	private double rightPower = 0.0;
	
	
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

	public void setLeftMotorsReverse(boolean invert) {
		invertLeft = invert;
	}
	
	public void setLeftEncoder(GrayHill leftEnc) {
		leftEncoder = leftEnc;
	}

	public double getLeftDistance() {
		return leftEncoder.getDistance();
	}
	
	public void setRightEncoder(GrayHill rightEnc) {
		rightEncoder = rightEnc;
	}

	public double getRightDistance() {
		return rightEncoder.getDistance();
	}

	public double getLeftPower() {
		return leftPower;
	}

	public void setLeftPower(double leftPwr) {
		if (leftPwr > 1.0)
			leftPwr = 1.0;
		else
		if (leftPwr < -1.0)
			leftPwr = 1.0;
		this.leftPower = leftPwr;
	}

	public double getRightPower() {
		return rightPower;
	}

	public void setRightPower(double rightPwr) {
		if (rightPwr > 1.0)
			rightPwr = 1.0;
		else
		if (rightPwr < -1.0)
			rightPwr = 1.0;
		this.rightPower = rightPwr;
	}

	public void drive() {
		drive(leftPower,rightPower);
	}
	
	public void drive(double leftPwr, double rightPwr) {
		if (invertLeft )
			leftPwr *= INVERT_MOTOR;
		else
			rightPwr *= INVERT_MOTOR;
		
		leftFront.set(leftPwr);
		leftMiddle.set(leftPwr);
		leftRear.set(leftPwr);
		rightFront.set(rightPwr);
		rightMiddle.set(rightPwr);
		rightRear.set(rightPwr);
	}

	public void stop() {
		leftPower = 0.0;
		rightPower = 0.0;
		
		leftFront.set(0.0);
		leftMiddle.set(0.0);
		leftRear.set(0.0);
		rightFront.set(0.0);
		rightMiddle.set(0.0);
		rightRear.set(0.0);
	}

	public void reset() {
		leftPower = 0.0;
		rightPower = 0.0;
		leftEncoder.reset();
		rightEncoder.reset();
	}
}