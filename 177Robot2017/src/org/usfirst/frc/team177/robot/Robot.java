package org.usfirst.frc.team177.robot;

import org.usfirst.frc.team177.auto.Autonomous;
import org.usfirst.frc.team177.auto.DoNothing;
import org.usfirst.frc.team177.auto.DriveBackwards;
import org.usfirst.frc.team177.auto.DropGearLeftRight;
import org.usfirst.frc.team177.auto.DropGearStraight;
import org.usfirst.frc.team177.auto.ShootFuel;
import org.usfirst.frc.team177.lib.RioLoggerThread;
import org.usfirst.frc.team177.lib.SmartDash;
import org.usfirst.frc.team177.lib.SmartPID;
import org.usfirst.frc.team177.lib.StopWatch;
import org.usfirst.frc.team177.lib.ToggleButton;

import com.kauailabs.navx.frc.AHRS;

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
	private RioLoggerThread logger = RioLoggerThread.getInstance();
	private StopWatch logWatch = new StopWatch();

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
	//boolean isGyroCreated = true;
	double [] shooterRPMS = {2700.0,2700.0,2700.0,2700.0};

	/* Toggle Buttons */
	ToggleButton gearPickup;
	ToggleButton ballPickup;
	ToggleButton shooter;
	boolean isPickupOrShooting = false;
	boolean isPickupToggle = false;
	boolean isShootingToggle = false;
	boolean isGearToggle = false;
	
	/* Emergency Button for Climber */
	boolean isEmergency = false;

	public Robot() {
	}

	@Override
	public void robotInit() {
		logger.setLoggingParameters(3600,15); /* 1 hour, log every 15 seconds */
		logger.start();
		logger.log("robotInit() logger started");
		
		dashboard.init();
		logWatch.setWatchInSeconds(5);
		
		driveTrain.setRightMotors(4, 5, 6);
		driveTrain.setLeftMotors(0, 1, 2);
		driveTrain.setLeftMotorsReverse(false);
		driveTrain.setLeftEncoder(new GrayHill(2, 3, false));
		driveTrain.setRightEncoder(new GrayHill(4, 5));
		logger.log("robotInit() driveTrain initialized");

		gearPickup = new ToggleButton(gamePad,3);
		ballPickup = new ToggleButton(gamePad,2);
		shooter = new ToggleButton(gamePad,1);
		logger.log("robotInit() togglebuttons initialized");
		
		/* Navx mxp Gyro */
		try {
			/* Communicate w/navX-MXP via the MXP SPI Bus. */
			gyro = new NavxGyro(SPI.Port.kMXP);
			logger.log("robotInit() called. navx-mxp initialized");
			logger.log("robotInit() gyro is calibrating " + gyro.isCalibrating());

		} catch (RuntimeException ex) {
			String err = "Error instantiating navX-MXP:  " + ex.getMessage();
			DriverStation.reportError(err, false);
			logger.log(err);
			//isGyroCreated = false;
		}
	}

	@Override
	public void disabledInit() {
		logger.log("disabledInit() called");
		//ballShift.set(false);  /* In position */
		//gearShift.set(true);  /* Up position */
	}

	@Override
	public void disabledPeriodic() {
		if (logWatch.hasExpired()) {
			logWatch.reset();
			logger.log("gyro is calibrating " + gyro.isCalibrating());
		}
	}

	
	@Override
	public void teleopInit() {
		logger.log("teleopInit() called");
		logger.writeLog();
		dashboard.setMode("teleop init");
		
		ballShift.set(true);   /* Out (pickup position) */
		gearShift.set(false);  /* Up position */
		driveTrain.reset();
		isPickupOrShooting = false;
		
		// Read PID Parameters
		SmartPID pid = dashboard.getPID();
		shooterLeftLower.setPIDParameters(pid);
		shooterLeftUpper.setPIDParameters(pid);
		shooterRightLower.setPIDParameters(pid);
		shooterRightUpper.setPIDParameters(pid);
		
		shooterRPMS = dashboard.getShooterRPMS();
	}

	/*
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		logger.writeLog();
		logWatch.setWatchInMillis(250);
		//dashboard.setLeftEncoderDistance(driveTrain.getLeftDistance());
		//dashboard.setRightEncoderDistance(driveTrain.getRightDistance());

		// Driving
		double left = leftStick.getRawAxis(Joystick.AxisType.kY.value);
		double right = rightStick.getRawAxis(Joystick.AxisType.kY.value);
		driveTrain.drive(left, right);

		// Shifting
		shifter.set(rightStick.getRawButton(3));

		// Caster
		caster.set(leftStick.getRawButton(3));

		// Climbing
		if (!isPickupOrShooting) {
			double climbAmt = gamePad.getRawAxis(3); /** 3 - Z Rotate Axis **/
			logger.log("in teleop. climbAmt =  " + climbAmt) ;
			if (climbAmt > 0.0)
				climbAmt = 0.0;
			logger.log("in teleop. climbAmt post cap =  " + climbAmt) ;
			dashboard.setClimber(climbAmt);
			climber.set(climbAmt);
		}

		// Ball Pickup - This controls Climber and ball pickup
		if (ballPickup.isOn()) {
			climber.set(-1.0);
			ballShift.set(true);
			ballGrabber.set(1.0);
			isPickupOrShooting = true;
			isPickupToggle = true;
		} else {
			if (isPickupToggle) {
				climber.set(0.0);
				ballShift.set(false);
				ballGrabber.set(0.0);
				isPickupOrShooting = false;
				isPickupToggle = false;
			}
		}
		if (!isPickupToggle) {
			if (gamePad.getRawButton(5))
				ballGrabber.setSpeed(1.0);
			else if (gamePad.getRawButton(7))
				ballGrabber.setSpeed(-1.0);
			else 
				ballGrabber.setSpeed(0.0);
		}
			
		// Gear Pickup - This controls Gear Grabber and Gear Pickup 
		if (gearPickup.isOn()) {
			gearShift.set(true);
			gearGrabber.set(1.0);
			isGearToggle = true;
		} else {
			if (isGearToggle) {
				gearShift.set(false);
				gearGrabber.set(0.0);
				isGearToggle = false;
			}
		}	
		if (!isGearToggle) {
			if (gamePad.getRawButton(6))
				gearGrabber.setSpeed(1.0);
			else if (gamePad.getRawButton(8))
				gearGrabber.setSpeed(-0.5);
			else
				gearGrabber.setSpeed(0.0);
		}
		
		// Shooting controls Climber, Helix and Shooting
		if (shooter.isOn()) {
			shooterLeftLower.setSpeed(shooterRPMS[0]);
			shooterLeftUpper.setSpeed(shooterRPMS[1]);
			shooterRightLower.setSpeed(shooterRPMS[2]);
			shooterRightUpper.setSpeed(shooterRPMS[3]);
			if (logWatch.hasExpired()) {
				logWatch.reset();
				logger.log(shooterLeftLower.getSpeed() + ", " + shooterLeftUpper.getSpeed() + ", " + shooterRightLower.getSpeed() + ", " + shooterRightUpper.getSpeed());
			}
			climber.set(-1.0);
			helix.set(0.5);
			isPickupOrShooting = true;
			isShootingToggle = true;
		} else {
			if (isShootingToggle) {
				shooterLeftLower.stop();
				shooterLeftUpper.stop();
				shooterRightLower.stop();
				shooterRightUpper.stop();
		
				climber.set(0.0);
				helix.set(0.0);
				isPickupOrShooting = false;
				isShootingToggle = false;
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

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		/**
		 * This is crazy reverse code just to test that test mode is working
		 **/
		/** Joysticks work on x axis (left to right) **/
		double left = leftStick.getRawAxis(Joystick.AxisType.kX.value);
		double right = rightStick.getRawAxis(Joystick.AxisType.kX.value);
		driveTrain.drive(left, right);
	}

	@Override
	public void autonomousInit() {
		logger.writeLog();
		dashboard.setMode("autonomous init");
		String amode = dashboard.getSelected();
		logger.log("autonomousInit() called. mode is " + amode);

		driveTrain.reset();
	
		if (SmartDash.AUTO_DRIVE.equals(amode)) {
			autoClass = new DriveBackwards();
			// shifter.set(false);
		} else if (SmartDash.AUTO_SHOOT.equals(amode)) {
			autoClass = new ShootFuel();
		} else if (SmartDash.AUTO_GEAR_STRAIGHT.equals(amode)) {
			DropGearStraight auto = new DropGearStraight();
			auto.setPicker(gearShift);
			auto.setGrabber(gearGrabber);
			autoClass = auto;
		}  else if (SmartDash.AUTO_GEAR_LEFT.equals(amode) ||
				    SmartDash.AUTO_GEAR_RIGHT.equals(amode)) {
			boolean turnLeft = SmartDash.AUTO_GEAR_LEFT.equals(amode);
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
		dashboard.setLeftEncoderDistance(driveTrain.getLeftDistance());
		dashboard.setRightEncoderDistance(driveTrain.getRightDistance());
		dashboard.setAutoLP(driveTrain.getLeftPower());
		dashboard.setAutoRP(driveTrain.getRightPower());

		autoClass.autoPeriodic();
	}
}
