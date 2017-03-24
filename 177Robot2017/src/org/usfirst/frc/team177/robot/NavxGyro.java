package org.usfirst.frc.team177.robot;


import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class NavxGyro extends AHRS implements PIDOutput {
	//private RioLoggerThread logger = RioLoggerThread.getInstance();
	
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
	
	public void displayData() {
        /* Display 6-axis Processed Angle Data                                      */
        SmartDashboard.putBoolean(  "IMU_Connected",        isConnected());
        SmartDashboard.putBoolean(  "IMU_IsCalibrating",    isCalibrating());
        SmartDashboard.putNumber(   "IMU_Yaw",              getYaw());
        SmartDashboard.putNumber(   "IMU_Pitch",            getPitch());
        SmartDashboard.putNumber(   "IMU_Roll",             getRoll());
        
        /* Display tilt-corrected, Magnetometer-based heading (requires             */
        /* magnetometer calibration to be useful)                                   */
        
        SmartDashboard.putNumber(   "IMU_CompassHeading",   getCompassHeading());
        
        /* Display 9-axis Heading (requires magnetometer calibration to be useful)  */
        SmartDashboard.putNumber(   "IMU_FusedHeading",     getFusedHeading());

        /* These functions are compatible w/the WPI Gyro Class, providing a simple  */
        /* path for upgrading from the Kit-of-Parts gyro to the navx MXP            */
        
        SmartDashboard.putNumber(   "IMU_TotalYaw",         getAngle());
        SmartDashboard.putNumber(   "IMU_YawRateDPS",       getRate());

        /* Display Processed Acceleration Data (Linear Acceleration, Motion Detect) */
        
        SmartDashboard.putNumber(   "IMU_Accel_X",          getWorldLinearAccelX());
        SmartDashboard.putNumber(   "IMU_Accel_Y",          getWorldLinearAccelY());
        SmartDashboard.putBoolean(  "IMU_IsMoving",         isMoving());
        SmartDashboard.putBoolean(  "IMU_IsRotating",       isRotating());

        /* Display estimates of velocity/displacement.  Note that these values are  */
        /* not expected to be accurate enough for estimating robot position on a    */
        /* FIRST FRC Robotics Field, due to accelerometer noise and the compounding */
        /* of these errors due to single (velocity) integration and especially      */
        /* double (displacement) integration.                                       */
        
        SmartDashboard.putNumber(   "Velocity_X",           getVelocityX());
        SmartDashboard.putNumber(   "Velocity_Y",           getVelocityY());
        SmartDashboard.putNumber(   "Displacement_X",       getDisplacementX());
        SmartDashboard.putNumber(   "Displacement_Y",       getDisplacementY());
        
        /* Display Raw Gyro/Accelerometer/Magnetometer Values                       */
        /* NOTE:  These values are not normally necessary, but are made available   */
        /* for advanced users.  Before using this data, please consider whether     */
        /* the processed data (see above) will suit your needs.                     */
        
        SmartDashboard.putNumber(   "RawGyro_X",            getRawGyroX());
        SmartDashboard.putNumber(   "RawGyro_Y",            getRawGyroY());
        SmartDashboard.putNumber(   "RawGyro_Z",            getRawGyroZ());
        SmartDashboard.putNumber(   "RawAccel_X",           getRawAccelX());
        SmartDashboard.putNumber(   "RawAccel_Y",           getRawAccelY());
        SmartDashboard.putNumber(   "RawAccel_Z",           getRawAccelZ());
        SmartDashboard.putNumber(   "RawMag_X",             getRawMagX());
        SmartDashboard.putNumber(   "RawMag_Y",             getRawMagY());
        SmartDashboard.putNumber(   "RawMag_Z",             getRawMagZ());
        SmartDashboard.putNumber(   "IMU_Temp_C",           getTempC());
        SmartDashboard.putNumber(   "IMU_Timestamp",        getLastSensorTimestamp());
        
        /* Omnimount Yaw Axis Information                                           */
        /* For more info, see http://navx-mxp.kauailabs.com/installation/omnimount  */
        BoardYawAxis yaw_axis = getBoardYawAxis();
        SmartDashboard.putString(   "YawAxisDirection",     yaw_axis.up ? "Up" : "Down" );
        SmartDashboard.putNumber(   "YawAxis",              yaw_axis.board_axis.getValue() );
        
        /* Sensor Board Information                                                 */
        SmartDashboard.putString(   "FirmwareVersion",      getFirmwareVersion());
        
        /* Quaternion Data                                                          */
        /* Quaternions are fascinating, and are the most compact representation of  */
        /* orientation data.  All of the Yaw, Pitch and Roll Values can be derived  */
        /* from the Quaternions.  If interested in motion processing, knowledge of  */
        /* Quaternions is highly recommended.                                       */
        /*
        SmartDashboard.putNumber(   "QuaternionW",          getQuaternionW());
        SmartDashboard.putNumber(   "QuaternionX",          getQuaternionX());
        SmartDashboard.putNumber(   "QuaternionY",          getQuaternionY());
        SmartDashboard.putNumber(   "QuaternionZ",          getQuaternionZ());
        */
        
        /* Connectivity Debugging Support                                           */
        /*
        SmartDashboard.putNumber(   "IMU_Byte_Count",       getByteCount());
        SmartDashboard.putNumber(   "IMU_Update_Count",     getUpdateCount());
        */
	}
	
	public boolean hasStopped() {
		return (!isMoving() && !isRotating());
	}

}
