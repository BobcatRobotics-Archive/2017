package org.usfirst.frc.team177.lib;

import java.util.TreeMap;
import java.util.Iterator;

/**
 * Reads and saves "important" dashboard values
 *
 */
public class DashboardConfiguration {
	/**
	 * Auto - Distance #1 = 90.0 
	 * Auto - Turn Angle = 60.0 
	 * Auto - Distance #2 = 80.8
	 * 
	 * Gyro - pid P = 0.03 
	 * Gyro - pid I = 0.00 
	 * Gyro - pid D = 0.00 
	 * Gyro - Degree Tolerance = 2.0
	 */
	private static DashboardConfiguration singleton;
	private TreeMap<String, Double> dashVals = new TreeMap<String, Double>();
	private boolean hasChanged = false;

	/* Create private constructor */
	private DashboardConfiguration() {
	}

	/* Create a static method to get instance. */
	public static DashboardConfiguration getInstance() {
		if (singleton == null) {
			singleton = new DashboardConfiguration();
		}
		return singleton;
	}

	public String[] getEntries() {
		String[] values = new String[dashVals.size()];
		Iterator<String> keySet = dashVals.keySet().iterator();
		int idx = 0;
		while (keySet.hasNext()) {
			String key = keySet.next();
			values[idx] = key + " = " + dashVals.get(key);
			idx++;
		}
		return values;
	}

	public void setValue(String key, double val) {
		boolean update = false;
		double existingVal = dashVals.getOrDefault(key, -999999.99);
		if (existingVal == -999999.99)
			update = true;
		else {
			if (val != existingVal)
				update = true;
		}
		if (update) {
			dashVals.put(key, val);
			hasChanged = true;
		}
	}

	public double getValue(String key) {
		return dashVals.get(key);
	}

	public boolean hasChanged() {
		return hasChanged;
	}

	public void finishedInitialRead() {
		hasChanged = false;
	}


	//
	// Specific variables
	//
	public double getAutoDistance1() {
		return getValue("Auto - Distance #1");
	}

	public double getAutoDistance2() {
		return getValue("Auto - Distance #2");
	}

	public double getAutoTurnAngle() {
		return getValue("Auto - Turn Angle");
	}

	public double getGyroP() {
		return getValue("Gyro - pid P");
	}
	
	public double getGyroI() {
		return getValue("Gyro - pid I");
	}

	public double getGyroD() {
		return getValue("Gyro - pid D");
	}
	
	public double getGyroTolerance() {
		return getValue("Gyro - Degree Tolerance");
	}
	//
	// End Specific Variables

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		Iterator<String> keySet = dashVals.keySet().iterator();
		while (keySet.hasNext()) {
			String key = keySet.next();
			sb.append(key + ": " + dashVals.get(key) + System.lineSeparator());
		}
		return sb.toString();
	}

}
