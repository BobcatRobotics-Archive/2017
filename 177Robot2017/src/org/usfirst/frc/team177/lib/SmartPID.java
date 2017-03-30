package org.usfirst.frc.team177.lib;

public class SmartPID {
	//private PIDController pid = new PIDController();
	private double ff;
	private double p;
	private double i;
	private double d;
	
	public SmartPID() {
		super();
	}

	public SmartPID(double ff, double p, double i, double d) {
		this();
		this.ff = ff;
		this.p = p;
		this.i = i;
		this.d = d;
	}
	
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
