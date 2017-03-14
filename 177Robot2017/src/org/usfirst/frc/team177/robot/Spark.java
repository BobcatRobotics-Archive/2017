package org.usfirst.frc.team177.robot;

import edu.wpi.first.wpilibj.PWMSpeedController;

public class Spark extends  PWMSpeedController {

	public Spark(int channel) {
		super(channel);
		initSpark();
	}
	
	protected void initSpark() {
		setBounds(2.003, 1.55, 1.50, 1.46, 0.999);
		setPeriodMultiplier(PeriodMultiplier.k1X);
		setSpeed(0.0);
		setZeroLatch();
	}
}
