package org.usfirst.frc.team177.robot;

import org.usfirst.frc.team177.lib.RioLogger;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
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
	Joystick leftStick = new Joystick(1);
	Joystick rightStick = new Joystick(0);
	
	/** Solenoids **/ 
	public Solenoid shifter = new Solenoid(0); /* For shifting */
	
	/** GrayHill Encoders **/
	GrayHill rightEnc = null;
	GrayHill leftEnc = null;

	/** Talon */
	Talon talon1 = null;
	Talon talon2 = null;

	SendableChooser<String> chooser = new SendableChooser<>();
	RioLogger logger = new RioLogger();
	//private boolean setSpeed = true;

	public Robot() {
	}

	@Override
	public void robotInit() {
		driveTrain.setRightMotors(3, 4, 5);
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
	
	/** Variables used to control Automous mode */
	private static final long SAMPLE_RATE = 100L; /* 100 milliseconds = .1 seconds */
	private static final double INCREASE_CORRECTION = 1.02;
	private static final double DECREASE_CORRECTION = 0.98;
	private double left_power = 0.5;
	private double right_power = 0.5;
	private int sample_loop = 1;
	private long autoStartTime;
	private boolean automode = true;
	private String autoMode = null;

    
    @Override
    public void autonomousInit() {    			
		SmartDashboard.putString("Mode","autonomous init");
  		autoStartTime = System.currentTimeMillis();
 		autoMode = chooser.getSelected();
		SmartDashboard.putString("Auto",autoMode);
		
		leftEnc.reset();
		rightEnc.reset();
		left_power = 0.5;
		right_power = 0.5;
		automode = true;
    }
    
    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {
    	long currentTime = System.currentTimeMillis();
    	long currentDuration = currentTime - autoStartTime;		
    	
		/** Smart Dashboard  Encoder Values */
		SmartDashboard.putNumber("Enc 1 Raw ", leftEnc.getRaw());	
		SmartDashboard.putNumber("Enc 1 Dist", leftEnc.getDistance());	
		SmartDashboard.putNumber("Enc 1 Rate", leftEnc.getRate());	
		SmartDashboard.putNumber("Enc 2 Raw ", rightEnc.getRaw());	
		SmartDashboard.putNumber("Enc 2 Dist", rightEnc.getDistance());	
		SmartDashboard.putNumber("Enc 2 Rate", rightEnc.getRate());	
		SmartDashboard.putNumber("Auto LP", left_power);	
		SmartDashboard.putNumber("Auto RP", right_power);	
		SmartDashboard.putNumber("Sample", sample_loop);	

   	
		
		if (AUTO_FULL.equals(autoMode)) {
			autoFull(currentDuration);
		} else {
			autoRamp(currentDuration);
		}
      }

    public void autoFull (long currentDuration) {
    	if (automode) {
	    	if (currentDuration > (SAMPLE_RATE * sample_loop)) {
	    		double ld = leftEnc.getDistance();
	    		double rd = rightEnc.getDistance();
	    		sample_loop++;
	    		//logger.log(format(ld,rd,right_power));
	    		if (ld > rd) {
	    			right_power *= INCREASE_CORRECTION;
	    		} else 
		   		if (ld < rd) {
	    			right_power *= DECREASE_CORRECTION;
		   		}
	    	}
    	}
     	if (currentDuration < 5000L ) {
    		driveTrain.drive(left_power,right_power);
    	} else {
    		automode = false;
    		driveTrain.stop();
       }
   	
    }
    
    private String format(double ld,double rd,double rp) {
    	StringBuffer sb = new StringBuffer();
    	sb.append(ld);
    	sb.append(" ");
       	sb.append(rd);
    	sb.append(" ");
       	sb.append(rp);
    	sb.append(" ");
    	return sb.toString();
    }
    public void autoRamp (long currentDuration) {
    	   
     	if (currentDuration < 250L ) {
    		driveTrain.drive(0.2,0.2);
    	}
    	else
    	if (currentDuration < 500L ) {
    		driveTrain.drive(0.3,0.3);
    	}
    	else
    	if (currentDuration < 750L ) {
    		driveTrain.drive(0.4,0.4);
    	}
    	else
    	if (currentDuration < 1000L ) {
    		driveTrain.drive(0.5,0.5);
    	}
    	else
 		if (currentDuration > 3000L ) {
    		driveTrain.stop();
       }
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

		SmartDashboard.putNumber("Auto LP", left_power);	
		SmartDashboard.putNumber("Auto RP", right_power);	
		SmartDashboard.putNumber("Sample", sample_loop);	

   }
}
