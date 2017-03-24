package org.usfirst.frc.team177.lib;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SmartDash {

	public static final String AUTO_GEAR_STRAIGHT = "agear";
	public static final String AUTO_GEAR_LEFT = "agearleft";
	public static final String AUTO_GEAR_RIGHT = "agearright";
	public static final String AUTO_SHOOT = "ashoot";
	public static final String AUTO_DRIVE = "adrive";
	public static final String AUTO_NOTHING = "anothing";

	private SendableChooser<String> chooser = new SendableChooser<>();

	private static SmartDash singleton;

	/* Create private constructor */
	private SmartDash() {
		super();
	}

	/* Create a static method to get instance. */
	public static SmartDash getInstance() {
		if (singleton == null) {
			singleton = new SmartDash();
		}
		return singleton;
	}

	public void init() {
		/** Add selections for autonomous mode **/
		chooser.addObject("Auto - Drop Gear Straight", AUTO_GEAR_STRAIGHT);
		chooser.addDefault("Auto - Drop Gear Left Side", AUTO_GEAR_LEFT);
		chooser.addObject("Auto - Drop Gear Right Side", AUTO_GEAR_RIGHT);
		chooser.addObject("Auto - Drive Backwards", AUTO_DRIVE);
		chooser.addObject("Auto - Shoot Fuel", AUTO_SHOOT);
		chooser.addObject("Auto - Do Nothing", AUTO_NOTHING);
		SmartDashboard.putData("Auto modes", chooser);
		SmartDashboard.putNumber("Auto - Distance #1", 60.0);
		SmartDashboard.putNumber("Auto - Turn Angle ", 60.0);
		SmartDashboard.putNumber("Auto - Distance #2", 24.8);
		SmartDashboard.putString("Shooter Time", "5");
		
		SmartDashboard.putNumber("Left Lower RPM", 2700);
		SmartDashboard.putNumber("Left Upper RPM", 2700);
		SmartDashboard.putNumber("Right Lower RPM", 2700);
		SmartDashboard.putNumber("Right Upper RPM", 2700);
		
		
		/** Encoder Values **/
		//SmartDashboard.putNumber("Enc 1 Raw ", 0.0);
		SmartDashboard.putNumber("Enc Left Dist", 0.0);
		//SmartDashboard.putNumber("Enc 1 Rate", 0.0);
		//SmartDashboard.putNumber("Enc 2 Raw ", 0.0);
		SmartDashboard.putNumber("Enc Right Dist", 0.0);
		//SmartDashboard.putNumber("Enc 2 Rate", 0.0);

		SmartDashboard.putNumber("Auto LP", 0.0);
		SmartDashboard.putNumber("Auto RP", 0.0);

		SmartDashboard.putNumber("climber", 0.0);
		SmartDashboard.putString("PID FF", "0.028");
		SmartDashboard.putString("PID P", "0.0015");
		SmartDashboard.putString("PID I", "0.0");
		SmartDashboard.putString("PID D", "0.0");
		
		//SmartDashboard.putNumber("Gyro Rate", 0.0);
		//SmartDashboard.putNumber("Gyro Angle", 0.0);
	
	}

	public void setMode(String mode) {
		SmartDashboard.putString("Mode", mode);
	}
	
	public void setLeftEncoderDistance(double distance) {
		SmartDashboard.putNumber("Enc Left Dist", distance);	
	}
	
	public void setRightEncoderDistance(double distance) {
		SmartDashboard.putNumber("Enc Right Dist", distance);	
	}
	
	/**
	public void setLeftEncoder(GrayHill encoder) {
		SmartDashboard.putNumber("Enc 1 Dist", encoder.getDistance());	
		SmartDashboard.putNumber("Enc 1 Raw ", encoder.getRaw());	
		SmartDashboard.putNumber("Enc 1 Rate", encoder.getRate());	
	}

	public void setRightEncoder(GrayHill encoder) {
		SmartDashboard.putNumber("Enc 2 Dist", encoder.getDistance());	
		SmartDashboard.putNumber("Enc 2 Raw ", encoder.getRaw());	
		SmartDashboard.putNumber("Enc 2 Rate", encoder.getRate());	
	}
	*/
	
	public void setClimber(double climbAmt) {
		SmartDashboard.putNumber("climber", climbAmt);
	}
	
	public void setAutoLP(double power) {
		SmartDashboard.putNumber("Auto LP", power);	
	}

	public void setAutoRP(double power) {
		SmartDashboard.putNumber("Auto RP", power);	
	}
	
	/*
	public void setGyroRate(double power) {
		SmartDashboard.putNumber("Gyro Rate", power);	
	}

	public void setGyroAngle(double power) {
		SmartDashboard.putNumber("Gyro Angle", power);	
	}
	*/
	
	public void setAutoDistance1(double power) {
		SmartDashboard.putNumber("Auto - Distance #1", power);	
	}

	public void setTurnAngle(double power) {
		SmartDashboard.putNumber("Auto - Turn Angle ", power);	
	}

	public void setAutoDistance2(double power) {
		SmartDashboard.putNumber("Auto - Distance #2", power);	
	}

	public double getAutoDistance1() {
		return new Double(SmartDashboard.getDouble("Auto - Distance #1"));	
	}

	public double getTurnAngle() {
		return new Double(SmartDashboard.getDouble("Auto - Turn Angle "));	
	}

	public double getAutoDistance2() {
		return new Double(SmartDashboard.getDouble("Auto - Distance #2"));	
	}

	public SmartPID getPID() {
		SmartPID pid = new SmartPID();
		pid.setFF(new Double(SmartDashboard.getString("PID FF")));
		pid.setP(new Double(SmartDashboard.getString("PID P")));
		pid.setI(new Double(SmartDashboard.getString("PID I")));
		pid.setD(new Double(SmartDashboard.getString("PID D")));
		return pid;
	}

	/**
	public long getShooterRPM() {
		return new Long(SmartDashboard.getString("Shooter RPM"));
	} */
	
	public double[] getShooterRPMS() {
		double [] rpms = new double[4];
		rpms[0] = new Double(SmartDashboard.getDouble("Left Lower RPM"));
		rpms[1] = new Double(SmartDashboard.getDouble("Left Upper RPM"));
		rpms[2] = new Double(SmartDashboard.getDouble("Right Lower RPM"));
		rpms[3] = new Double(SmartDashboard.getDouble("Right Upper RPM"));
		return rpms;
	}

	public void setShooterRPMS(double [] rpms) {
		SmartDashboard.putNumber("Left Lower RPM", rpms[0]);
		SmartDashboard.putNumber("Left Upper RPM", rpms[1]);
		SmartDashboard.putNumber("Right Lower RPM", rpms[2]);
		SmartDashboard.putNumber("Right Upper RPM", rpms[3]);
	}
	
	public String getSelected() {
		String amode = chooser.getSelected();
		SmartDashboard.putString("Auto",amode);
		return amode;
	}
	/*
	public double getGearDistance() {		
		return new Double(SmartDashboard.getString("Drop Gear Distance"));
	}
	*/

	public double getShooterTime() {
		return new Double(SmartDashboard.getString("Shooter Time"));
	}
	/*
	public double getGyroRate() {
		return new Double(SmartDashboard.getNumber("Gyro Rate"));
	}
	
	public double getGyroAngle() {
		return new Double(SmartDashboard.getNumber("Gyro Angle"));
	}
	*/
}
