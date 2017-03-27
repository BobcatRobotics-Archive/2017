package org.usfirst.frc.team177.robot;

import org.usfirst.frc.team177.auto.Autonomous;
import org.usfirst.frc.team177.auto.DoNothing;
import org.usfirst.frc.team177.auto.DriveBackwards;
import org.usfirst.frc.team177.auto.DropGearLeftRight;
import org.usfirst.frc.team177.auto.DropGearStraight;
import org.usfirst.frc.team177.auto.ShootFuel;
import org.usfirst.frc.team177.lib.DashboardConfiguration;
import org.usfirst.frc.team177.lib.LocalReader;
import org.usfirst.frc.team177.lib.RioLogger;
import org.usfirst.frc.team177.lib.RioLoggerThread;
import org.usfirst.frc.team177.lib.SmartDash;
import org.usfirst.frc.team177.lib.SmartPID;
import org.usfirst.frc.team177.lib.StopWatch;
import org.usfirst.frc.team177.lib.ToggleButton;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;

/**
 * 
 */
public class Robot extends IterativeRobot {
	private SmartDash dashboard = SmartDash.getInstance();
	private RioLogger logFile = RioLogger.getInstance();
	private StopWatch logWatch = new StopWatch();
	private DriverStation ds = DriverStation.getInstance();
	private DashboardConfiguration dashConfig = DashboardConfiguration.getInstance();
	private RioLoggerThread logger = null;

	/* Autonomous mode Class */
	Autonomous autoClass = null;

	/* Drive Chain Motors */
	DriveChain driveTrain = new DriveChain();

	/* Joysticks */
	Joystick leftStick = new Joystick(1);
	Joystick rightStick = new Joystick(0);
	Joystick gamePad = new Joystick(2);
	Joystick switchBoard = new Joystick(3);

	/* Solenoids */
	Solenoid shifter = new Solenoid(0); /* For shifting */
	Solenoid caster = new Solenoid(3); /* For engaging casters */
	Solenoid ballShift = new Solenoid(2);
	Solenoid gearShift = new Solenoid(1);

	/* Talons */
	Talon shooterLeftLower = new Talon(3, true);
	Talon shooterLeftUpper = new Talon(4, true);
	Talon shooterRightLower = new Talon(1, true);
	Talon shooterRightUpper = new Talon(2, true);

	/* Victor */
	Victor climber = new Victor(7);
	Victor ballGrabber = new Victor(8);
	Victor gearGrabber = new Victor(9);
	Victor helix = new Victor(3);

	/* Gyro */
	NavxGyro gyro;
	// boolean isGyroCreated = true;
	double[] shooterRPMS = { 1950.00, 2400.00, 2350.00, 2800.00 };
	double leftTargetRange = 0.0;
	double rightTargetRange = 0.0;

	/* Toggle Buttons */
	ToggleButton gearPickup;
	ToggleButton ballPickup;
	ToggleButton shooter;
	boolean isPickupOrShooting = false;
	boolean isBallPickupToggle = false;
	boolean isGearPickupToggle = false;
	boolean isShooterPickupToggle = false;
	boolean isLoggerEnabled = false;
	boolean setShooterSpeed = true;
	boolean setHelixOn = false;

	/* Emergency Button for Climber */
	boolean isEmergency = false;

	/*
	 * Variables to loop until gyro is calibrated we've tried some number of
	 * times
	 */
	int maxCalibrationPasses = 20;
	int iCalibrationPasses;
	double pauseCalibrationSeconds = 0.1;

	
	public Robot() {
		logFile.log("Robot constructor finished");
	}

	@Override
	public void robotInit() {
		dashboard.init();
		logWatch.setWatchInSeconds(30);
		
		driveTrain.setRightMotors(4, 5, 6);
		driveTrain.setLeftMotors(0, 1, 2);
		driveTrain.setLeftMotorsReverse(false);
		driveTrain.setLeftEncoder(new GrayHill(2, 3, false));
		driveTrain.setRightEncoder(new GrayHill(4, 5));
		logFile.log("robotInit() driveTrain initialized");

		gearPickup = new ToggleButton(gamePad,3);
		ballPickup = new ToggleButton(gamePad,2);
		shooter = new ToggleButton(gamePad,1);
		logFile.log("robotInit() togglebuttons initialized");
		
		/* Navx mxp Gyro */
		try {
			/* Communicate w/navX-MXP via the MXP SPI Bus. */
			gyro = new NavxGyro(SPI.Port.kMXP);
			logFile.log("robotInit() called. navx-mxp initialized");
			for (iCalibrationPasses=0; iCalibrationPasses<maxCalibrationPasses; iCalibrationPasses++) {
				if (!gyro.isCalibrating()) break;
				logFile.log("robotInit() gyro is calibrating, pass " + iCalibrationPasses);
			}
			logFile.log("robotInit() gyro is calibrating " + gyro.isCalibrating());
			if (!gyro.isCalibrating())
				gyro.zeroYaw();
			logFile.log("robotInit() currentYaw " + gyro.getYaw());
			dashboard.displayData(gyro);
		} catch (RuntimeException ex) {
			String err = "Error instantiating navX-MXP:  " + ex.getMessage();
			DriverStation.reportError(err, false);
			logFile.log(err);
		}
	}

	@Override
	public void robotPeriodic() {
	}

	@Override
	public void disabledInit() {
		logFile.log("disabledInit() called");
		//startThreadLogger();
		dashboard.updateDashBoardConfig();
		if (dashConfig.hasChanged()) {
			logFile.log("disabledInit() Log file has changed. Updating.");
			LocalReader lr = new LocalReader();
			lr.writeDashboardFile(dashConfig);
		}
		shooterLeftLower.setSpeed(0.0);
		shooterLeftUpper.setSpeed(0.0);
		shooterRightLower.setSpeed(0.0);
		shooterRightUpper.setSpeed(0.0);
		ballGrabber.setSpeed(0.0);
		climber.set(0.0);
		setShooterSpeed = true;
	}

	@Override
	public void disabledPeriodic() {
		/**
		if (logWatch.hasExpired()) {
			logWatch.reset();
			logFile.log("disabledPeriodic() called");
			logFile.log("disabledPeriodic() gyro is calibrating " + gyro.isCalibrating());
			logFile.log("disabledPeriodic() driver station is attached " + ds.isDSAttached());
			logFile.log("disabledPeriodic() driver station is enabled " + ds.isEnabled());
		}
		*/
	}

	@Override
	public void teleopInit() {
		logFile.log("teleopInit() called");
		dashboard.setMode("teleop init");
		logWatch.setWatchInMillis(100);
		startThreadLogger();

		ballShift.set(true); /* Out (pickup position) */
		gearShift.set(false); /* Up position */
		driveTrain.reset();

		// Read PID Parameters
		SmartPID pid = dashboard.getPID();
		logFile.log("shooter pid (FF, P,I,D) " + pid.getFF() + ", " + pid.getP() + ", " + pid.getI() + ", " + pid.getD());
		logger.log("shooter pid (FF, P,I,D) " + pid.getFF() + ", " + pid.getP() + ", " + pid.getI() + ", " + pid.getD());
		shooterLeftLower.setPIDParameters(pid);
		shooterLeftUpper.setPIDParameters(pid);
		shooterRightLower.setPIDParameters(pid);
		shooterRightUpper.setPIDParameters(pid);
		shooterRPMS = dashboard.getShooterRPMS();
		logFile.log("teleop init() shooter rpms " + shooterRPMS[0] + ", " + shooterRPMS[1] + ", " + shooterRPMS[2] + ", " + shooterRPMS[3] );
		logger.log("teleop init() shooter rpms " + shooterRPMS[0] + ", " + shooterRPMS[1] + ", " + shooterRPMS[2] + ", " + shooterRPMS[3] );
	}

	/*
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		dashboard.setMode("teleop periodic");
		dashboard.setValue(SmartDash.ENCODER_LEFT_DIST,driveTrain.getLeftDistance());
		dashboard.setValue(SmartDash.ENCODER_RIGHT_DIST,driveTrain.getRightDistance());

		// Driving
		double left = leftStick.getRawAxis(Joystick.AxisType.kY.value);
		double right = rightStick.getRawAxis(Joystick.AxisType.kY.value);
		driveTrain.drive(left, right);

		// Shifting
		shifter.set(rightStick.getRawButton(3));

		// Caster
		caster.set(leftStick.getRawButton(3));

		// Ball Pickup - This controls Climber and ball pickup
		if (ballPickup.isOn()) {
			climber.set(-1.0);
			ballShift.set(true);
			//ballGrabber.set(1.0);
			isPickupOrShooting = true;
			isBallPickupToggle = true;
		} else {
			if (isBallPickupToggle) {
				climber.set(0.0);
				ballShift.set(false);
				ballGrabber.set(0.0);
				isPickupOrShooting = false;
				isBallPickupToggle = false;
			}
		}
//		if (!isBallPickupToggle) {
			if (gamePad.getRawButton(5))
				ballGrabber.setSpeed(1.0);
			else if (gamePad.getRawButton(7))
				ballGrabber.setSpeed(-1.0);
			else
				ballGrabber.setSpeed(0.0);
//		}

		// Gear Pickup - This controls Gear Grabber and Gear Pickup
		if (gearPickup.isOn()) {
			gearShift.set(true);
			//gearGrabber.set(.0);
			isPickupOrShooting = true;
			isGearPickupToggle = true;
		} else {
			gearShift.set(false);
			gearGrabber.set(0.0);
			isPickupOrShooting = false;
			isGearPickupToggle = false;
		}
		//if (!isGearPickupToggle) {
			if (gamePad.getRawButton(6))
				gearGrabber.setSpeed(1.0);
			else if (gamePad.getRawButton(8))
				gearGrabber.setSpeed(-0.3);
			else
				gearGrabber.setSpeed(0.0);
		//}

		// Climbing
		if (!isPickupOrShooting) {
			double climbAmt = gamePad.getRawAxis(3); /** 3 - Z Rotate Axis **/
			// logger.log("in teleop. climbAmt = " + climbAmt) ;
			if (climbAmt > 0.0)
				climbAmt = 0.0;
			// logger.log("in teleop. climbAmt post cap = " + climbAmt) ;
			climber.set(climbAmt);
		}

		// Shooting controls Climber, Helix and Shooting
		if (shooter.isOn()) {
			if (setShooterSpeed) {
				shooterLeftLower.setSpeed(shooterRPMS[0]);
				shooterLeftUpper.setSpeed(shooterRPMS[1]);
				shooterRightLower.setSpeed(shooterRPMS[2]);
				shooterRightUpper.setSpeed(shooterRPMS[3]);
				setShooterSpeed = false;
			}
			if (logWatch.hasExpired()) {
				logWatch.reset();
				logger.log("shooter speeds " + format(shooterLeftLower.getSpeed(),shooterLeftUpper.getSpeed(),shooterRightLower.getSpeed(),shooterRightUpper.getSpeed()));
			}
			climber.set(-1.0);
			//if (setHelixOn)
				helix.set(0.7);
			isPickupOrShooting = true;
			isShooterPickupToggle = true;
		} else {
			if (isShooterPickupToggle) {
				shooterLeftLower.stop();
				shooterLeftUpper.stop();
				shooterRightLower.stop();
				shooterRightUpper.stop();

				climber.set(0.0);
				helix.set(0.0);
				isPickupOrShooting = false;
				isShooterPickupToggle = false;
				setShooterSpeed = true;
				setHelixOn = false;
			}
		}

		// Emergency Code if the Climber ratchet shifts
		/**
		if (switchBoard.getRawButton(4)) {
			climber.set(-0.5);
			isEmergency = true;
		}
		if (isEmergency && !switchBoard.getRawButton(4)) {
			climber.set(0.0);
			isEmergency = false;
		}
	 	*/
		// End Emergency Code

	}

	@Override
	public void testInit() {
		logger.log("testInit() called");
	}

	@Override
	public void testPeriodic() {
		/**
		 * This is crazy reverse code just to test that test mode is working
		 * Joysticks work on x axis (left to right)
		 **/
		double left = leftStick.getRawAxis(Joystick.AxisType.kX.value);
		double right = rightStick.getRawAxis(Joystick.AxisType.kX.value);
		driveTrain.drive(left, right);
	}

	@Override
	public void autonomousInit() {
		logFile.log("autonomousInit() called");
		startThreadLogger();

		dashboard.setMode("autonomous init");
		String amode = dashboard.getSelected();
		logger.log("autonomousInit() called. mode is " + amode);

		driveTrain.reset();
		driveTrain.stop();

		if (SmartDash.AUTO_CMD_DRIVE.equals(amode)) {
			autoClass = new DriveBackwards();
			shifter.set(false);
		} else if (SmartDash.AUTO_CMD_SHOOT.equals(amode)) {
			autoClass = new ShootFuel();
		} else if (SmartDash.AUTO_CMD_GEAR_STRAIGHT.equals(amode)) {
			DropGearStraight auto = new DropGearStraight();
			auto.setPicker(gearShift);
			auto.setGrabber(gearGrabber);
			autoClass = auto;
		} else if (SmartDash.AUTO_CMD_GEAR_LEFT.equals(amode) || SmartDash.AUTO_CMD_GEAR_RIGHT.equals(amode)) {
			boolean turnLeft = SmartDash.AUTO_CMD_GEAR_LEFT.equals(amode);
			DropGearLeftRight auto = new DropGearLeftRight(turnLeft);
			auto.setPicker(gearShift);
			auto.setGrabber(gearGrabber);
			autoClass = auto;
		} else {
			autoClass = new DoNothing();
		}
		autoClass.setDrive(driveTrain);
		autoClass.setGyro(gyro);
		autoClass.autoInit();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		/** Smart Dashboard Encoder Values */
		dashboard.setValue(SmartDash.ENCODER_LEFT_DIST,driveTrain.getLeftDistance());
		dashboard.setValue(SmartDash.ENCODER_RIGHT_DIST,driveTrain.getRightDistance());
		dashboard.setValue(SmartDash.AUTO_LEFT_POWER,driveTrain.getLeftPower());
		dashboard.setValue(SmartDash.AUTO_RIGHT_POWER,driveTrain.getRightPower());

		autoClass.autoPeriodic();
	}

	/**
	 * Since when the Driver Station attaches is sets the time on the Roborio,
	 * start the logger afterwards. .
	 */
	private void startThreadLogger() {
		if (ds.isDSAttached() /*&& !isLoggerEnabled*/) {
			logger = RioLoggerThread.getInstance();
			logger.setLoggingParameters(3600,15); /* 1 hour, log every 15 seconds */
			//logger.log("logger started");
			logger.writeLog();
			isLoggerEnabled = true;
		}
	}

	private String format(double ld, double rd, double lp, double rp) {
		return String.format("%9.2f %9.2f %9.2f %9.2f", ld, rd, lp, rp);
	}

	/**
	 * Call this method to reset controls. This addresses the issue when an
	 * operator disables Teleop, or Autonomous and the Driver Station
	 * "remembers" Joystick settings and PWM Seetign
	 */
}
