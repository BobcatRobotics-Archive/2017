package org.usfirst.frc.team177.robot;

import org.usfirst.frc.team177.auto.Autonomous;
import org.usfirst.frc.team177.auto.DriveAway;
import org.usfirst.frc.team177.auto.DropGear;
import org.usfirst.frc.team177.auto.ShootFuel;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This is a demo program showing the use of the RobotDrive class. The
 * SampleRobot class is the base of a robot application that will automatically
 * call your Autonomous and OperatorControl methods at the right time as
 * controlled by the switches on the driver station or the field controls.
 *
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SampleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 *
 * WARNING: While it may look like a good choice to use for your code if you're
 * inexperienced, don't. Unless you know what you are doing, complex code will
 * be much more difficult under this system. Use IterativeRobot or Command-Based
 * instead if you're new.
 */
public class Robot extends IterativeRobot {
	private final double SHOOTER_RPMS = 1000.0f;
	private final String AUTO_GEAR = "agear";
	private final String AUTO_SHOOT = "ashoot";
	private final String AUTO_DRIVE = "adrive";
	
	
	/** Drive Chain Motors **/
	DriveChain driveTrain = new DriveChain();

	/**Joysticks**/    
	Joystick leftStick = new Joystick(1);
	Joystick rightStick = new Joystick(0);
	Joystick gamePad = new Joystick(3);
	
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
	
	/* Autonomous mode Class */
	Autonomous autoClass = null;

	SendableChooser<String> chooser = new SendableChooser<>();
	//private boolean setSpeed = true;

	public Robot() {
	}

	@Override
	public void robotInit() {
		initSmartDashboard();
		driveTrain.setRightMotors(4, 5, 6);
		driveTrain.setLeftMotors(0, 1, 2);
		driveTrain.setLeftMotorsReverse(false);
		
	}

	@Override
	public void teleopInit() {
		SmartDashboard.putString("Mode","teleop init");
		pickup.set(true);
	}
	
	/**
     * This function is called periodically during operator control
     */
	@Override
    public void teleopPeriodic() {
		SmartDashboard.putNumber("Enc 1 Dist", leftEnc.getDistance());	
		SmartDashboard.putNumber("Enc 2 Dist", rightEnc.getDistance());	

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
		
		// Feeder Balls 
		double feederspeed = gamePad.getRawAxis(Joystick.AxisType.kY.value);
		feeder.setSpeed(feederspeed);
		
		if (gamePad.getRawButton(7)) 
			grabber.setSpeed(0.5);
		else
			grabber.setSpeed(0.0);
		if (gamePad.getRawButton(5)) 
			grabber.setSpeed(-0.5);
		else
			grabber.setSpeed(0.0);
	
		
    	/* Shooting */
		if (gamePad.getRawButton(8)) {
			shooterLeft1.setSpeed(SHOOTER_RPMS);
			shooterLeft2.setSpeed(SHOOTER_RPMS);
			shooterRight1.setSpeed(SHOOTER_RPMS);
			shooterRight2.setSpeed(SHOOTER_RPMS);
		} else {
			/** TODO:: Fix code to coast or break */
			shooterLeft1.setSpeed(0.0);
			shooterLeft2.setSpeed(0.0);
			shooterRight1.setSpeed(0.0);
			shooterRight2.setSpeed(0.0);
	}
		
		// Emergency Button for pick up
		if (gamePad.getRawButton(4)) {
			pickup.set(false);
			pickup.set(true);
		}
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
		SmartDashboard.putString("Mode","autonomous init");
		String amode = chooser.getSelected();
		SmartDashboard.putString("Auto",amode);
		
		leftEnc.reset();
		rightEnc.reset();
		
		if(AUTO_GEAR.equals(amode)) {
			autoClass = new DropGear();
			//shifter.set(false);
		}
		else if(AUTO_SHOOT.equals(amode)) {
			autoClass = new ShootFuel();
		}
		else if(AUTO_DRIVE.equals(amode)) {
			autoClass = new DriveAway();
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
    	
		/** Smart Dashboard  Encoder Values */
		SmartDashboard.putNumber("Enc 1 Raw ", leftEnc.getRaw());	
		SmartDashboard.putNumber("Enc 1 Dist", leftEnc.getDistance());	
		SmartDashboard.putNumber("Enc 1 Rate", leftEnc.getRate());	
		SmartDashboard.putNumber("Enc 2 Raw ", rightEnc.getRaw());	
		SmartDashboard.putNumber("Enc 2 Dist", rightEnc.getDistance());	
		SmartDashboard.putNumber("Enc 2 Rate", rightEnc.getRate());	
		SmartDashboard.putNumber("Auto LP", autoClass.getLeftPower());	
		SmartDashboard.putNumber("Auto RP", autoClass.getRightPower());	
		
		autoClass.autoPeriodic();
    }
    
    private void initSmartDashboard() {
		/** Add selections for autonomous mode **/
		chooser.addDefault("Auto - Drop Gear", AUTO_GEAR);
		chooser.addObject("Auto - Shoot Fuel", AUTO_SHOOT);
		chooser.addObject("Auto - Drive Away", AUTO_DRIVE);
		SmartDashboard.putData("Auto modes", chooser);
		SmartDashboard.putString("Drop Gear Distance", "90");
		SmartDashboard.putString("Shooter Time", "5");
		SmartDashboard.putString("Mode","startup");

		/** Encoder Values **/
		SmartDashboard.putNumber("Enc 1 Raw ", leftEnc.getRaw());	
		SmartDashboard.putNumber("Enc 1 Dist", leftEnc.getDistance());	
		SmartDashboard.putNumber("Enc 1 Rate", leftEnc.getRate());	
		
		SmartDashboard.putNumber("Enc 2 Raw ", rightEnc.getRaw());	
		SmartDashboard.putNumber("Enc 2 Dist", rightEnc.getDistance());	
		SmartDashboard.putNumber("Enc 2 Rate", rightEnc.getRate());	

		SmartDashboard.putNumber("Auto LP", 0.0);	
		SmartDashboard.putNumber("Auto RP", 0.0);	

   }
}
