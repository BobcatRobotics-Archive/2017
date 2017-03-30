package org.usfirst.frc.team177.lib;

import org.usfirst.frc.team177.robot.NavxGyro;

import com.kauailabs.navx.frc.AHRS.BoardYawAxis;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

@SuppressWarnings("deprecation")
public class SmartDash {
	/* Autonomous Modes */
	public static final String AUTO_CMD_GEAR_STRAIGHT = "agear";
	public static final String AUTO_CMD_GEAR_LEFT = "agearleft";
	public static final String AUTO_CMD_GEAR_RIGHT = "agearright";
	public static final String AUTO_CMD_SHOOT = "ashoot";
	public static final String AUTO_CMD_DRIVE = "adrive";
	public static final String AUTO_CMD_NOTHING = "anothing";
	/* Autonomous Parameters */
	public static final String AUTO_STRAIGHT_DISTANCE = "Auto - Straight Distance";
	public static final String AUTO_DISTANCE_1 = "Auto - Distance #1";
	public static final String AUTO_DISTANCE_2 = "Auto - Distance #2";
   	public static final String AUTO_DISTANCE_3 = "Auto - Distance #3";
  	public static final String AUTO_DISTANCE_4 = "Auto - Distance #4";
	
	public static final String AUTO_TURN_ANGLE_1 = "Auto - Turn Angle";
	public static final String AUTO_TURN_ANGLE_2 = "Auto - Turn Angle #2";
	public static final String AUTO_SHOOT_TIME = "Auto - Shooter Time";
	public static final String AUTO_LEFT_POWER = "Auto - Left Power";
	public static final String AUTO_RIGHT_POWER = "Auto - Right Power";
	/*  Shooters */
	public static final String SHOOTER_LL_RPM = "Shooter - Left Lower RPM";
	public static final String SHOOTER_LU_RPM = "Shooter - Left Upper RPM";
	public static final String SHOOTER_RL_RPM = "Shooter - Right Lower RPM";
	public static final String SHOOTER_RU_RPM = "Shooter - Right Upper RPM";
	public static final String ENCODER_LEFT_DIST = "Encoder - Left Distance";
	public static final String ENCODER_RIGHT_DIST = "Encoder - Right Distance";
	public static final String SHOOTER_PID_FF = "Shooter - PID FF";
	public static final String SHOOTER_PID_P = "Shooter - PID P";
	public static final String SHOOTER_PID_I = "Shooter - PID I";
	public static final String SHOOTER_PID_D = "Shooter - PID D";
	/* Gyro */
	public static final String GYRO_PID_P = "Gyro - PID P";
	public static final String GYRO_PID_I = "Gyro - PID I";
	public static final String GYRO_PID_D = "Gyro - PID D";
	public static final String GYRO_DEGREE_TOLERANCE = "Gyro - Degree Tolerance";
	/* Others Variables */
	

	Object [][] defaultValues =  {
		{ AUTO_STRAIGHT_DISTANCE, 90.0 },
		{ AUTO_DISTANCE_1, 80.0 },
		{ AUTO_DISTANCE_2, 36.0 },
		{ AUTO_DISTANCE_3, 28.0 },
		{ AUTO_TURN_ANGLE_1, 60.0 },
		{ AUTO_TURN_ANGLE_2, 26.0 },
		{ AUTO_SHOOT_TIME, 5.0 },
		{ AUTO_LEFT_POWER, 0.0 },
		{ AUTO_RIGHT_POWER, 0.0 },
		{ SHOOTER_LL_RPM, 1950.0 },
		{ SHOOTER_LU_RPM, 2400.0 },
		{ SHOOTER_RL_RPM, 2350.0 },
		{ SHOOTER_RU_RPM, 2800.0 },
		{ ENCODER_LEFT_DIST, 0.0 },
		{ ENCODER_RIGHT_DIST, 0.0 },
		{ SHOOTER_PID_FF, 0.028 },
		{ SHOOTER_PID_P, 0.0015 },
		{ SHOOTER_PID_I, 0.0 },
		{ SHOOTER_PID_D, 0.0 },
		{ GYRO_DEGREE_TOLERANCE, 1.0 },
		{ GYRO_PID_P, 0.035 },
		{ GYRO_PID_I, 0.003 },
		{ GYRO_PID_D, 0.0 }
	};
	
	private SendableChooser<String> autoChooser = new SendableChooser<>();
	private static RioLogger logFile = RioLogger.getInstance();
	private static LocalReader lr = new LocalReader();
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
		/* Add selections for autonomous mode */
		autoChooser.addObject("Auto - Drop Gear Straight", AUTO_CMD_GEAR_STRAIGHT);
		autoChooser.addDefault("Auto - Drop Gear Left Side", AUTO_CMD_GEAR_LEFT);
		autoChooser.addObject("Auto - Drop Gear Right Side", AUTO_CMD_GEAR_RIGHT);
		autoChooser.addObject("Auto - Drive Backwards", AUTO_CMD_DRIVE);
		autoChooser.addObject("Auto - Shoot Fuel", AUTO_CMD_SHOOT);
		autoChooser.addObject("Auto - Do Nothing", AUTO_CMD_NOTHING);
		SmartDashboard.putData("Auto modes", autoChooser);

		/* Add Variables that do not have a default value here */
		/* Also variables that are not numeric need to go here because only numbers are handled */

		// Default values have been initialized
		for (Object [] defaults : defaultValues) {
			SmartDashboard.putNumber((String)defaults[0],(double)defaults[1]);
			//logFile.log((String)defaults[0] + ", " + (double)defaults[1]);
		}
		
		// Read the dashboard.cfg and update/display these values
		DashboardConfiguration dashConfig = lr.readDashboardFile();
		dashConfig.finishedInitialRead();
		updateDashboard(dashConfig);
	}

	public void updateDashboard(DashboardConfiguration dashConfig )  {
		String [] dashEntries = dashConfig.getKeys();
		for (String entry : dashEntries) {
			//logFile.log("updateDashboard() " + entry + ", " + dashConfig.getValue(entry));
			SmartDashboard.putNumber(entry, dashConfig.getValue(entry));
		}
	}
	
	public void updateDashboardConfigFile() {
		if (lr.isReadFile()) {
			DashboardConfiguration dashConfig = DashboardConfiguration.getInstance();
			String [] dashEntries = dashConfig.getKeys();
			for (String key : dashEntries) {
				double dashVal = getValue(key);
				//logFile.log("updateDashboardConfigFile() setting value " + key + ", " + dashVal);
				dashConfig.setValue(key, dashVal);
			}
			dashConfig.setChanged(false);
		}		
	}
	
	public void setValue(String key, double value) {
		SmartDashboard.putNumber(key, value);
	}
	
	public double getValue(String key) {
		return SmartDashboard.getDouble(key);
	}
	
	public void setMode(String mode) {
		SmartDashboard.putString("Mode", mode);
	}

	public void setShooterRPMS(double [] rpms) {
		SmartDashboard.putNumber(SHOOTER_LL_RPM, rpms[0]);
		SmartDashboard.putNumber(SHOOTER_LU_RPM, rpms[1]);
		SmartDashboard.putNumber(SHOOTER_RL_RPM, rpms[2]);
		SmartDashboard.putNumber(SHOOTER_RU_RPM, rpms[3]);
	}

	/*
	public void setLeftEncoderDistance(double distance) {
		SmartDashboard.putNumber("Enc Left Dist", distance);	
	}
	
	public void setRightEncoderDistance(double distance) {
		SmartDashboard.putNumber("Enc Right Dist", distance);	
	}
	*/
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
	/**
	public void setClimber(double climbAmt) {
		SmartDashboard.putNumber("climber", climbAmt);
	}
	
	public void setAutoLP(double power) {
		SmartDashboard.putNumber("Auto LP", power);	
	}

	public void setAutoRP(double power) {
		SmartDashboard.putNumber("Auto RP", power);	
	}
	
	public void setAutoStraightDistance(double power) {
		SmartDashboard.putNumber("Auto - Distance #1", power);	
	}
	
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
	*/
	public String getSelected() {
		String amode = autoChooser.getSelected();
		SmartDashboard.putString("Auto",amode);
		return amode;
	}
	
	public SmartPID getPID() {
		SmartPID pid = new SmartPID();
		pid.setFF(SmartDashboard.getDouble(SHOOTER_PID_FF));
		pid.setP(SmartDashboard.getDouble(SHOOTER_PID_P));
		pid.setI(SmartDashboard.getDouble(SHOOTER_PID_I));
		pid.setD(SmartDashboard.getDouble(SHOOTER_PID_D));
		return pid;
	}

	
	public double[] getShooterRPMS() {
		double [] rpms = new double[4];
		rpms[0] = SmartDashboard.getDouble(SHOOTER_LL_RPM);
		rpms[1] = SmartDashboard.getDouble(SHOOTER_LU_RPM);
		rpms[2] = SmartDashboard.getDouble(SHOOTER_RL_RPM);
		rpms[3] = SmartDashboard.getDouble(SHOOTER_RU_RPM);
		return rpms;
	}

	

	/*
	public double getGearDistance() {		
		return new Double(SmartDashboard.getString("Drop Gear Distance"));
	}
	

	public double getShooterTime() {
		return new Double(SmartDashboard.getString("Shooter Time"));
	}
	*/
	
	public void displayData(NavxGyro gyro) {
        /* Display 6-axis Processed Angle Data                                      */
        SmartDashboard.putBoolean(  "IMU_Connected",        gyro.isConnected());
        SmartDashboard.putBoolean(  "IMU_IsCalibrating",    gyro.isCalibrating());
        SmartDashboard.putNumber(   "IMU_Yaw",              gyro.getYaw());
        SmartDashboard.putNumber(   "IMU_Pitch",            gyro.getPitch());
        SmartDashboard.putNumber(   "IMU_Roll",             gyro.getRoll());
        
        /* Display tilt-corrected, Magnetometer-based heading (requires             */
        /* magnetometer calibration to be useful)                                   */
        
        SmartDashboard.putNumber(   "IMU_CompassHeading",   gyro.getCompassHeading());
        
        /* Display 9-axis Heading (requires magnetometer calibration to be useful)  */
        SmartDashboard.putNumber(   "IMU_FusedHeading",     gyro.getFusedHeading());

        /* These functions are compatible w/the WPI Gyro Class, providing a simple  */
        /* path for upgrading from the Kit-of-Parts gyro to the navx MXP            */
        /*
        SmartDashboard.putNumber(   "IMU_TotalYaw",         gyro.getAngle());
        SmartDashboard.putNumber(   "IMU_YawRateDPS",       gyro.getRate());
		*/
        
        /* Display Processed Acceleration Data (Linear Acceleration, Motion Detect) */
        SmartDashboard.putNumber(   "IMU_Accel_X",          gyro.getWorldLinearAccelX());
        SmartDashboard.putNumber(   "IMU_Accel_Y",          gyro.getWorldLinearAccelY());
        SmartDashboard.putBoolean(  "IMU_IsMoving",         gyro.isMoving());
        SmartDashboard.putBoolean(  "IMU_IsRotating",       gyro.isRotating());

        /* Display estimates of velocity/displacement.  Note that these values are  */
        /* not expected to be accurate enough for estimating robot position on a    */
        /* FIRST FRC Robotics Field, due to accelerometer noise and the compounding */
        /* of these errors due to single (velocity) integration and especially      */
        /* double (displacement) integration.                                       */
        
        SmartDashboard.putNumber(   "Velocity_X",           gyro.getVelocityX());
        SmartDashboard.putNumber(   "Velocity_Y",           gyro.getVelocityY());
        SmartDashboard.putNumber(   "Displacement_X",       gyro.getDisplacementX());
        SmartDashboard.putNumber(   "Displacement_Y",       gyro.getDisplacementY());
        
        /* Display Raw Gyro/Accelerometer/Magnetometer Values                       */
        /* NOTE:  These values are not normally necessary, but are made available   */
        /* for advanced users.  Before using this data, please consider whether     */
        /* the processed data (see above) will suit your needs.                     */
        /*
        SmartDashboard.putNumber(   "RawGyro_X",            gyro.getRawGyroX());
        SmartDashboard.putNumber(   "RawGyro_Y",            gyro.getRawGyroY());
        SmartDashboard.putNumber(   "RawGyro_Z",            gyro.getRawGyroZ());
        SmartDashboard.putNumber(   "RawAccel_X",           gyro.getRawAccelX());
        SmartDashboard.putNumber(   "RawAccel_Y",           gyro.getRawAccelY());
        SmartDashboard.putNumber(   "RawAccel_Z",           gyro.getRawAccelZ());
        SmartDashboard.putNumber(   "RawMag_X",             gyro.getRawMagX());
        SmartDashboard.putNumber(   "RawMag_Y",             gyro.getRawMagY());
        SmartDashboard.putNumber(   "RawMag_Z",             gyro.getRawMagZ());
        */
        SmartDashboard.putNumber(   "IMU_Temp_C",           gyro.getTempC());
        SmartDashboard.putNumber(   "IMU_Timestamp",        gyro.getLastSensorTimestamp());
        
        /* Omnimount Yaw Axis Information                                           */
        /* For more info, see http://navx-mxp.kauailabs.com/installation/omnimount  */
        BoardYawAxis yaw_axis = gyro.getBoardYawAxis();
        SmartDashboard.putString(   "YawAxisDirection",     yaw_axis.up ? "Up" : "Down" );
        SmartDashboard.putNumber(   "YawAxis",              yaw_axis.board_axis.getValue() );
        
        /* Sensor Board Information                                                 */
        SmartDashboard.putString(   "FirmwareVersion",      gyro.getFirmwareVersion());
        
        /* Quaternion Data                                                          */
        /* Quaternions are fascinating, and are the most compact representation of  */
        /* orientation data.  All of the Yaw, Pitch and Roll Values can be derived  */
        /* from the Quaternions.  If interested in motion processing, knowledge of  */
        /* Quaternions is highly recommended.                                       */
        /*
        SmartDashboard.putNumber(   "QuaternionW",          gyro.getQuaternionW());
        SmartDashboard.putNumber(   "QuaternionX",          gyro.getQuaternionX());
        SmartDashboard.putNumber(   "QuaternionY",          gyro.getQuaternionY());
        SmartDashboard.putNumber(   "QuaternionZ",          gyro.getQuaternionZ());
        */
        
        /* Connectivity Debugging Support                                           */
        /*
        SmartDashboard.putNumber(   "IMU_Byte_Count",       gyro.getByteCount());
        SmartDashboard.putNumber(   "IMU_Update_Count",     gyro.getUpdateCount());
        */
	}
}
