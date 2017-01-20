package org.usfirst.frc.team177.robot;

import edu.wpi.first.wpilibj.Victor;

public class DriveChain {
	
	Victor leftFront;
	Victor leftMiddle;
	Victor leftRear;
	
	Victor rightFront;
	Victor rightMiddle;
	Victor rightRear;
	
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
}