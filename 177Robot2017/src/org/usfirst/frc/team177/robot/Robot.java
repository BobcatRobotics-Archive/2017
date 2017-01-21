package org.usfirst.frc.team177.robot;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SampleRobot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
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
	
	private static final double INVERT_MOTOR = -1.0;
	
	/** Drive Chain Motors **/
	DriveChain drive = new DriveChain();

	//RobotDrive myRobot = new RobotDrive(0, 1);
	/**Joysticks**/    
	Joystick leftStick = new Joystick(0);
	Joystick rightStick = new Joystick(1);
	
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	SendableChooser<String> chooser = new SendableChooser<>();
	
    long autoStartTime;


	public Robot() {
		//myRobot.setExpiration(0.1);
	}

	@Override
	public void robotInit() {
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto modes", chooser);
		
		drive.setLeftMotors(3, 4, 5);
		drive.setRightMotors(0, 1, 2);
	}

	  /**
     * This function is called periodically during operator control
     */
	@Override
    public void teleopPeriodic() {
		
    	//Driving
    	double left = leftStick.getRawAxis(Joystick.AxisType.kY.value)  * INVERT_MOTOR;
		double right = rightStick.getRawAxis(Joystick.AxisType.kY.value);
		drive.drive(left, right);
	}

	/**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	/** This is crazy reverse code just to test that test mode is working **/
     	/** Joysticks work on x axis (left to righ) **/
     	double left = leftStick.getRawAxis(Joystick.AxisType.kX.value)  * INVERT_MOTOR;
 		double right = rightStick.getRawAxis(Joystick.AxisType.kX.value);
 		drive.drive(left, right);
    }
    
    public void autonomousInit() {    			
  		autoStartTime = System.currentTimeMillis();
    }
    
    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	/** This is sample code just to test automonous mode **/
    	/** Code drive left motors for 2 sec, right motors for 2 secs, then stops */
    	long currentTime = System.currentTimeMillis();
    	long currentDuration = currentTime - autoStartTime;
    	
    	if (currentDuration < 2000L ) {
    		drive.drive(1.0,0.0);
    	}
    	else
    	if (currentDuration < 4000L ) {
    		drive.drive(0.0,1.0);
    	}
    	else
		if (currentDuration > 6000L ) {
    		drive.stop();
       }
   }

}
