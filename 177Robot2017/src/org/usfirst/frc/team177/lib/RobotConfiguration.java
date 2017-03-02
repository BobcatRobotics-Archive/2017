package org.usfirst.frc.team177.lib;

public class RobotConfiguration {
	private String autoMode = "agear";
	private long sampleRate = 25L; /* How often to sample 25 milliseconds = 40 times/second	 */
	private double increaseFactor = 1.05; /* Increase Adjustment */
	private double decreaseFactor = 0.95; /* Decrease Adjustment */
	private double deadBandRange = 0.0;   /* Range to not make an adjustment */

	private static RobotConfiguration singleton;

	/* Create private constructor */
	private RobotConfiguration() {
	}

	/* Create a static method to get instance. */
	public static RobotConfiguration getInstance() {
		if (singleton == null) {
			singleton = new RobotConfiguration();
		}
		return singleton;
	}

	public String getAutoMode() {
		return autoMode;
	}

	public void setAutoMode(String autoMode) {
		this.autoMode = autoMode;
	}

	public long getSampleRate() {
		return sampleRate;
	}

	public void setSampleRate(long sampleRate) {
		this.sampleRate = sampleRate;
	}

	public double getIncreaseFactor() {
		return increaseFactor;
	}

	public void setIncreaseFactor(double increaseFactor) {
		this.increaseFactor = increaseFactor;
	}

	public double getDecreaseFactor() {
		return decreaseFactor;
	}

	public void setDecreaseFactor(double decreaseFactor) {
		this.decreaseFactor = decreaseFactor;
	}

	public double getDeadBandRange() {
		return deadBandRange;
	}

	public void setDeadBandRange(double deadBandRange) {
		this.deadBandRange = deadBandRange;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("AutoMode: " + autoMode + System.lineSeparator());
		sb.append("SampleRate: " + sampleRate + System.lineSeparator());
		sb.append("Increase: " + increaseFactor + System.lineSeparator());
		sb.append("Decrease: " + decreaseFactor + System.lineSeparator());
		sb.append("Dead Band: " + deadBandRange + System.lineSeparator());
		return sb.toString();
	}

}
