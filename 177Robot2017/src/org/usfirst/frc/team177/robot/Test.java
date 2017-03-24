package org.usfirst.frc.team177.robot;

import org.usfirst.frc.team177.lib.DashboardConfiguration;
import org.usfirst.frc.team177.lib.LocalReader;

public class Test {

	public static void main(String[] args) {
		LocalReader lr = new LocalReader();
		DashboardConfiguration dashConfig = lr.readDashboardFile();
		if (lr.isReadFile()) {
			System.out.println(dashConfig.toString());
			System.out.println("config changed " + dashConfig.hasChanged());
			dashConfig.finishedInitialRead();
			System.out.println("config changed " + dashConfig.hasChanged());
			dashConfig.setValue("Auto - Turn Angle", 60.0);
			System.out.println("config changed " + dashConfig.hasChanged());
			dashConfig.setValue("Auto - Turn Angle", 58.5);
			System.out.println("config changed " + dashConfig.hasChanged());
			if (dashConfig.hasChanged())  {
				lr.writeDashboardFile(dashConfig);
			}
		} else {
			System.out.println("could not read dashboard config file");
		}
	}

}
