package org.usfirst.frc.team177.lib;

import edu.wpi.first.wpilibj.Joystick;

/**
 * This class acts as a button toggle. To uses 1 second for a debounce time
 */
public class TriToggleButton {
	private final long PRESS_TIME_MILLIS = 1000L;
	
	private StopWatch pressedTime = new StopWatch();
	private Joystick joyStick = null;
	private int buttonNbr = 0;
	
	// State values
	//  0 = off                isOff() == true
	//  1 =  on pressed once   isToggled() == true
	//  2 =  on pressed twice  isOn() == true 
	private int state = 0;

	private TriToggleButton() {
		super();
		pressedTime.setWatchInMillis(PRESS_TIME_MILLIS);
	}
	
	public TriToggleButton(Joystick joyStick, int buttonNbr) {
		this();
		this.joyStick = joyStick;
		this.buttonNbr = buttonNbr;
	}
	
	/**
	public TriToggleButton(Joystick joyStick, int buttonNbr,boolean turnOn) {
		this(joyStick,buttonNbr);
		state = turnOn;
	}
	*/
	
	public boolean isToggled() {
		if (state == 0) {
			// Check if the button is pressed
			if (joyStick.getRawButton(buttonNbr)) {
				pressedTime.setWatchInMillis(PRESS_TIME_MILLIS);
				state = 1;
			}
		}
		return state == 1;
	}

	public boolean isOn() {
		if (state == 1) {
			if (joyStick.getRawButton(buttonNbr)) {
				if (pressedTime.hasExpired()) {
					pressedTime.setWatchInMillis(0);
					state = 2; 
				}
			}
		}
		if (state == 2) {
			if (!joyStick.getRawButton(buttonNbr)) {
				state = 0;
			}
		}
		return state == 2;
	}
	
	public boolean isOff() {
		return state == 0;
	}
	
	public void reset() {
		state = 0;
		pressedTime.setWatchInMillis(0);
	}

}
