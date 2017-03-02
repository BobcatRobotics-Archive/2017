package org.usfirst.frc.team177.lib;

import edu.wpi.first.wpilibj.PIDController;

public class SmartPID {
	//private PIDController pid = new PIDController();
	private double ff;
	private double p;
	private double i;
	private double d;
	
	public double getFF() {
		return ff;
	}
	public void setFF(double ff) {
		this.ff = ff;
	}
	public double getP() {
		return p;
	}
	public void setP(double p) {
		this.p = p;
	}
	public double getI() {
		return i;
	}
	public void setI(double i) {
		this.i = i;
	}
	public double getD() {
		return d;
	}
	public void setD(double d) {
		this.d = d;
	}

	
}
