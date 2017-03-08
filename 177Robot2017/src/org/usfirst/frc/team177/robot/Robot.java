package org.usfirst.frc.team177.robot;

import org.usfirst.frc.team177.auto.Autonomous;
import org.usfirst.frc.team177.auto.DoNothing;
import org.usfirst.frc.team177.auto.DriveBackwards;
import org.usfirst.frc.team177.auto.DropGear;
import org.usfirst.frc.team177.auto.ShootFuel;
import org.usfirst.frc.team177.lib.RioLoggerThread;
import org.usfirst.frc.team177.lib.SmartDash;
import org.usfirst.frc.team177.lib.SmartPID;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;

/**
 * 
 */
public class Robot extends IterativeRobot {
	private SmartDash dashboard = SmartDash.getInstance();
	private RioLoggerThread logger = null;

	/* Autonomous mode Class */
	Autonomous autoClass = null;
	
	/** Drive Chain Motors **/
	DriveChain driveTrain = new DriveChain();

	/** Joysticks **/    
	Joystick leftStick = new Joystick(1);
	Joystick rightStick = new Joystick(0);
	Joystick gamePad = new Joystick(2);
	Joystick switchBoard = new Joystick(3);
	
	/** Solenoids **/ 
	public Solenoid shifter = new Solenoid(0); /* For shifting */
	public Solenoid caster = new Solenoid(1);  /* For engaging casters */
	public Solenoid pickup = new Solenoid(2);  /* Kick out the pick up mechanism */
	
	/** GrayHill Encoders **/
	GrayHill rightEnc = new GrayHill(0,1);
	GrayHill leftEnc =  new GrayHill(2,3,false);

	/** Talon */
	Talon shooterLeft1 = new Talon(1,true);
	Talon shooterLeft2 = new Talon(2,true);
	Talon shooterRight1 = new Talon(3,true);
	Talon shooterRight2 = new Talon(4,true);
	
	/* Victor */
	Victor climber = new Victor(7);
	Victor grabber = new Victor(8);
	Victor feeder = new Victor(9);

	/* Variables used in teleop */
	private double shooterRPM = 0.0;
	private SmartPID pid;
	/* End teleop vars */

	/* Emergency Button for Climber */
	boolean isEmergency = false;

	public Robot() {
		logger = RioLoggerThread.getInstance();
		//logger.setLoggingParameters(3600, 15); /* 1 hour, every 30 seconds */
		logger.setLoggingParameters(300, 15); /* 5 minutes, every 30 seconds */
		logger.start();
		logger.log("robot constructor called");
	}

	@Override
	public void robotInit() {
		logger.log("robotInit() called");
	
		dashboard.init();
		driveTrain.setRightMotors(4, 5, 6);
		driveTrain.setLeftMotors(0, 1, 2);
		driveTrain.setLeftMotorsReverse(false);
	}

   @Override
	public void disabledInit(){
	   	logger.log("disabledInit() called");
		}
   
    @Override
	public void disabledPeriodic(){
 	}
    
	@Override
	public void teleopInit() {
    	logger.log("teleopInit() called");
       	logger.writeLog();
       	
		dashboard.setMode("teleop init");
		pickup.set(true);
				
		// Read PID Parameters
		pid = dashboard.getPID();
		shooterLeft1.setPIDParameters(pid);
		shooterLeft2.setPIDParameters(pid);
		shooterRight1.setPIDParameters(pid);
		shooterRight2.setPIDParameters(pid);
		
		shooterRPM = dashboard.getShooterRPM();
		logger.log("RPM = " + shooterRPM);
	}
	

	/*
     * This function is called periodically during operator control
     */
	@Override
    public void teleopPeriodic() {
		dashboard.setLeftEncoder(leftEnc);
		dashboard.setRightEncoder(rightEnc);

    	//Driving
    	double left = leftStick.getRawAxis(Joystick.AxisType.kY.value);
		double right = rightStick.getRawAxis(Joystick.AxisType.kY.value);
		driveTrain.drive(left, right);

		// Shifting 
		shifter.set(rightStick.getRawButton(3));
	
		// Caster 
		caster.set(leftStick.getRawButton(3));
		
		// Climbing
		//double climbAmt = rightStick.getRawAxis(Joystick.AxisType.kX.value); 
		double climbAmt = gamePad.getRawAxis(3) * -1.0; /** 3 - Z Rotate Axis **/
		if (climbAmt > 1.0) 
			climbAmt = 1.0;
		else if (climbAmt < 0.0)
			climbAmt = 0.0;
		climber.set(climbAmt);
		
		// Emergency Code if the Climber ratchet shifts
		if (switchBoard.getRawButton(4))  {
			climber.set(-0.5);
			isEmergency = true;
		} 
		if (isEmergency && !switchBoard.getRawButton(4)) {
			climber.set(0.0);
			isEmergency = false;
		}
		// End Emergency Code
		
		// Feeder Balls 
		double feederspeed = gamePad.getRawAxis(Joystick.AxisType.kY.value);
		feeder.setSpeed(feederspeed);
		
		grabber.setSpeed(0.0);
		if (gamePad.getRawButton(7)) 
			grabber.setSpeed(0.5);
		else
		if (gamePad.getRawButton(5)) 
			grabber.setSpeed(-0.5);
		
    	/* Shooting */
		if (gamePad.getRawButton(8)) {
			shooterLeft1.setSpeed(shooterRPM);
			shooterLeft2.setSpeed(shooterRPM);
			shooterRight1.setSpeed(shooterRPM);
			shooterRight2.setSpeed(shooterRPM);
		} else {
			shooterLeft1.stop();
			shooterLeft2.stop();
			shooterRight1.stop();
			shooterRight2.stop();
		}
		
		// Emergency Button for pick up
		if (gamePad.getRawButton(4)) {
			pickup.set(false);
		}
		// Emergency Button for pick up
		if (gamePad.getRawButton(3)) {
			pickup.set(true);
		}
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
    	/** This is crazy reverse code just to test that test mode is working **/
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
       	
		leftEnc.reset();
		rightEnc.reset();
		
		if(SmartDash.AUTO_DRIVE.equals(amode)) {
			autoClass = new DriveBackwards();
			//shifter.set(false);
		}
		else if(SmartDash.AUTO_SHOOT.equals(amode)) {
			autoClass = new ShootFuel();
		}
		else if(SmartDash.AUTO_GEAR.equals(amode)) {
			DropGear auto = new DropGear();
			auto.setPicker(pickup);
			auto.setGrabber(grabber);
			autoClass = auto;
		} else {
			autoClass = new DoNothing();
		}
		autoClass.setEncoders(leftEnc, rightEnc);
		autoClass.setDrive(driveTrain);
		autoClass.autoInit();
    }
    
    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {
		/** Smart Dashboard Encoder Values */
    	dashboard.setLeftEncoder(leftEnc);
    	dashboard.setRightEncoder(rightEnc);
    	dashboard.setAutoLP(autoClass.getLeftPower());
    	dashboard.setAutoRP(autoClass.getRightPower());
		
		autoClass.autoPeriodic();
    }  
  }
