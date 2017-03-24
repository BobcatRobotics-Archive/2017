package org.usfirst.frc.team177.robot;


import org.usfirst.frc.team177.lib.RioLoggerThread;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class NavxGyro extends AHRS implements PIDOutput {
	private RioLoggerThread logger = RioLoggerThread.getInstance();
	
	/* The following PID Controller coefficients will need to be tuned */
	/* to match the dynamics of your drive system. Note that the */
	/* SmartDashboard in Test mode has support for helping you tune */
	/* controllers by displaying a form where you can enter new P, I, */
	/* and D constants and test the mechanism. */
	private PIDController turnController;
	private static final double kP = 0.03;
	private static final double kI = 0.02;
	private static final double kD = 0.00;
	private static final double kF = 0.00;
	private static final double kToleranceDegrees = 1.0f;
	
	private double rotateToAngleRate;


	public NavxGyro(Port spi_port_id) throws RuntimeException {
		super(spi_port_id);
		initTurnController();
	}
	
	protected void initTurnController() {
		logger.log("Gyro PID (P,I,D) = " + kP + ", " + kI + ", " + kD);
		logger.log("Gyro Degree Tolerance = " + kToleranceDegrees);
		// Configure PID
		turnController = new PIDController(kP, kI, kD, kF, this, this);
		turnController.setInputRange(-180.0f, 180.0f);
		turnController.setOutputRange(-0.5, 0.5);
		turnController.setAbsoluteTolerance(kToleranceDegrees);
		turnController.setContinuous(true);
		turnController.disable();
	}

	public double getRotateToAngleRate() {
		return rotateToAngleRate;
	}

	public void turnToAngle(double angle) {
		turnController.setSetpoint(angle);
		rotateToAngleRate = 0; // This value will be updated in the pidWrite() method.
		turnController.enable();
	}
	
	public void stopTurn() {
		turnController.disable();
		//zeroYaw();
	}
	
	@Override
	/* This function is invoked periodically by the PID Controller, */
	/* based upon navX MXP yaw angle input and PID Coefficients. */
	public void pidWrite(double output) {
		rotateToAngleRate = output;
	}
	

	
	public boolean hasStopped() {
		return (!isMoving() && !isRotating());
	}

}
