package org.usfirst.frc.team177.robot;

import edu.wpi.first.wpilibj.Encoder;


public class GrayHill {
	private Encoder enc = null;
	
	private GrayHill() {
		super();
	}
	
	public GrayHill(int channel1, int channel2)	{
		this(channel1,channel2,false);
	}

	public GrayHill(int channel1, int channel2, boolean reverse)	{
		this();
		enc = new Encoder(channel1,channel2, reverse, Encoder.EncodingType.k2X);
		enc.setMaxPeriod(0.5);
		enc.setMinRate(10.0);
		enc.setDistancePerPulse(0.098175);
		enc.setReverseDirection(reverse);
		enc.setSamplesToAverage(10);
	}
	
	public double getRaw() {
		return enc.getRaw();
	}

	public double getDistance() {
		return enc.getDistance();
	}
	
	public double getRate() {
		return enc.getRate();
	}
	
	public void reset() {
		enc.reset();
	}
}
