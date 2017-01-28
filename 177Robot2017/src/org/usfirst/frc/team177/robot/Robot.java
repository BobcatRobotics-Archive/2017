package org.usfirst.frc.team177.robot;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Solenoid;
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
	DriveChain driveTrain = new DriveChain();

	/**Joysticks**/    
	Joystick leftStick = new Joystick(0);
	Joystick rightStick = new Joystick(1);
	
	/** Solenoids **/ 
	public Solenoid shiftPneumatic = new Solenoid(0); /* For shifting */

	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	SendableChooser<String> chooser = new SendableChooser<>();
	
    long autoStartTime;
    boolean shiftGears = false;
    long buttonStartTime = System.currentTimeMillis();


	public Robot() {
		//myRobot.setExpiration(0.1);
	}

	@Override
	public void robotInit() {
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto modes", chooser);

		driveTrain.setLeftMotors(3, 4, 5);
		driveTrain.setRightMotors(0, 1, 2);
	}

	@Override
	public void teleopInit() {
		SmartDashboard.putString("Status","teleop mode initailized");
		shiftGears = false;
		SmartDashboard.putString("GearStatus","gear status = " + shiftGears);
	}
	
	/**
     * This function is called periodically during operator control
     */
	@Override
    public void teleopPeriodic() {
		/** Display Joystick status **/
		/** For testing only **/
		SmartDashboard.putBoolean("Right 0",rightStick.getRawButton(Joystick.ButtonType.kTrigger.value));
		SmartDashboard.putBoolean("Right 1",rightStick.getRawButton(Joystick.ButtonType.kTop.value));
		SmartDashboard.putBoolean("Right 2",rightStick.getRawButton(Joystick.ButtonType.kNumButton.value));
		
    	//Driving
    	double left = leftStick.getRawAxis(Joystick.AxisType.kY.value)  * INVERT_MOTOR;
		double right = rightStick.getRawAxis(Joystick.AxisType.kY.value);
		driveTrain.drive(left, right);
		
		shiftPneumatic.set(checkPneumaticStatus());
	}

	private boolean checkPneumaticStatus() {
		if (rightStick.getRawButton(Joystick.ButtonType.kTop.value)) {
			if (System.currentTimeMillis()-buttonStartTime > 1500L) {
				shiftGears = !shiftGears;
				buttonStartTime = System.currentTimeMillis();
			}
		}
		
		return shiftGears;
	}
	/**
     * This function is called periodically during test mode
     */
	@Override
    public void testPeriodic() {
    	/** This is crazy reverse code just to test that test mode is working **/
     	/** Joysticks work on x axis (left to right) **/
		/**
     	double left = leftStick.getRawAxis(Joystick.AxisType.kX.value)  * INVERT_MOTOR;
 		double right = rightStick.getRawAxis(Joystick.AxisType.kX.value);
 		driveTrain.drive(left, right);
 		*/
		
    }
    
    @Override
    public void autonomousInit() {    			
  		autoStartTime = System.currentTimeMillis();
    }
    
    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {
    	/** This is sample code just to test automonous mode **/
    	/** Code drive left motors for 2 sec, right motors for 2 secs, then stops */
    	long currentTime = System.currentTimeMillis();
    	long currentDuration = currentTime - autoStartTime;
    	
    	if (currentDuration < 2000L ) {
    		driveTrain.drive(1.0,0.0);
    	}
    	else
    	if (currentDuration < 4000L ) {
    		driveTrain.drive(0.0,1.0);
    	}
    	else
		if (currentDuration > 6000L ) {
    		driveTrain.stop();
       }
   }

}
