package org.usfirst.frc.team177.lib;

import edu.wpi.first.wpilibj.Joystick;

/**
 * This class acts as a button toggle. To avoid a bounce it
 * maintains
 * @author bobcat177
 */
public class ToggleButton {
	private final long PRESS_TIME_MILLIS = 1000L;
	
	private StopWatch pressedTime = new StopWatch();
	private Joystick joyStick = null;
	private int buttonNbr = 0;
	private boolean state = false;

	private ToggleButton() {
		super();
		pressedTime.setWatchInMillis(PRESS_TIME_MILLIS);
	}
	
	public ToggleButton(Joystick joyStick, int buttonNbr) {
		this();
		this.joyStick = joyStick;
		this.buttonNbr = buttonNbr;
	}
	
	public ToggleButton(Joystick joyStick, int buttonNbr,boolean turnOn) {
		this(joyStick,buttonNbr);
		state = turnOn;
	}

	public boolean isOn() {
		// Check if the button is pressed
		if (joyStick.getRawButton(buttonNbr)) {
			if (pressedTime.hasExpired()) {
				pressedTime.setWatchInMillis(PRESS_TIME_MILLIS);
				state = !state; 
			}
		}
		return state;
	}
	
	public boolean isOff() {
		return !isOn();
	}
	
}
