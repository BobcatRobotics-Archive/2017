package org.usfirst.frc.team177.robot;

import org.usfirst.frc.team177.auto.DropGear;

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
	private final String AUTO_FULL = "afull";
	private final String AUTO_RAMP = "aramp";
	
	
	/** Drive Chain Motors **/
	DriveChain driveTrain = new DriveChain();

	/**Joysticks**/    
	Joystick leftStick = new Joystick(2);
	Joystick rightStick = new Joystick(1);
	Joystick gamePad = new Joystick(0);
	
	/** Solenoids **/ 
	public Solenoid shifter = new Solenoid(0); /* For shifting */
	
	/** GrayHill Encoders **/
	GrayHill rightEnc = null;
	GrayHill leftEnc = null;

	/** Talon */
	Talon talon1 = null;
	Talon talon2 = null;
	
	/* Victor */
	Victor climber = new Victor(7);
	Victor grabber = new Victor(8);
	
	/* Autonomous mode Class */
	DropGear dropGear = null;

	SendableChooser<String> chooser = new SendableChooser<>();
	//private boolean setSpeed = true;

	public Robot() {
	}

	@Override
	public void robotInit() {
		driveTrain.setRightMotors(4, 5, 6);
		driveTrain.setLeftMotors(0, 1, 2);
		driveTrain.setLeftMotorsReverse(false);
		
		rightEnc = new GrayHill(0,1);
		leftEnc = new GrayHill(2,3,true);

		talon1 = new Talon(1,false);
		talon2 = new Talon(2,true);

		initSmartDashboard();
		
	}

	@Override
	public void teleopInit() {
		SmartDashboard.putString("Mode","teleop init");
	}
	
	/**
     * This function is called periodically during operator control
     */
	@Override
    public void teleopPeriodic() {
		/** Display Joystick status **/
		/** For testing only **/
		
    	//Driving
		
    	double left = leftStick.getRawAxis(Joystick.AxisType.kY.value);
		double right = rightStick.getRawAxis(Joystick.AxisType.kY.value);
		driveTrain.drive(left, right);
		
		shifter.set(rightStick.getRawButton(3));
		
		double climbAmt = gamePad.getRawAxis(Joystick.AxisType.kZ.value);
		if (climbAmt > 0) 
			climbAmt = 0;
		else if (climbAmt < -1)
			climbAmt = -1.0;
			
			
		climber.set(climbAmt);
		
    	/* Percent voltage mode */
		/**
    	double left = leftStick.getRawAxis(Joystick.AxisType.kY.value);
    	talon1.setSpeed(left);
    	
    	if (setSpeed) {
    		setSpeed = false;
    		talon2.setSpeed(500); 
    	}
		*/
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
		SmartDashboard.putString("Auto",chooser.getSelected());
		
		leftEnc.reset();
		rightEnc.reset();
		dropGear = new DropGear();
		dropGear.setEncoders(leftEnc, rightEnc);
		dropGear.setDrive(driveTrain);
    }
    
    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {
    	
		dropGear.autonomousPeriodic();

		/** Smart Dashboard  Encoder Values */
		SmartDashboard.putNumber("Enc 1 Raw ", leftEnc.getRaw());	
		SmartDashboard.putNumber("Enc 1 Dist", leftEnc.getDistance());	
		SmartDashboard.putNumber("Enc 1 Rate", leftEnc.getRate());	
		SmartDashboard.putNumber("Enc 2 Raw ", rightEnc.getRaw());	
		SmartDashboard.putNumber("Enc 2 Dist", rightEnc.getDistance());	
		SmartDashboard.putNumber("Enc 2 Rate", rightEnc.getRate());	
		SmartDashboard.putNumber("Auto LP", dropGear.getLeftPower());	
		SmartDashboard.putNumber("Auto RP", dropGear.getRightPower());	
      }


    
    private void initSmartDashboard() {
		/** Add selections for autonomous mode **/
		chooser.addDefault("Auto - Full Speed", AUTO_FULL);
		chooser.addObject("Auto - Ramped Speed", AUTO_RAMP);
		SmartDashboard.putData("Auto modes", chooser);

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
