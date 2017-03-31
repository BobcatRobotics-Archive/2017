package org.usfirst.frc.team177.robot;

import org.usfirst.frc.team177.lib.DashboardConfiguration;
import org.usfirst.frc.team177.lib.LocalReader;
import org.usfirst.frc.team177.lib.SmartDash;

public class Test {

	public static void main(String[] args) {
		SmartDash dashBoard  = SmartDash.getInstance();
		LocalReader lr = LocalReader.getInstance();
		DashboardConfiguration dashConfig = lr.readDashboardFile();
		if (lr.isReadFile()) {
			System.out.println(dashConfig.toString());
			System.out.println("config changed " + dashConfig.hasChanged());
			dashConfig.finishedInitialRead();
			System.out.println("config changed " + dashConfig.hasChanged());
			dashConfig.setValue(SmartDash.AUTO_TURN_ANGLE_1, 60.0);
			System.out.println("config changed " + dashConfig.hasChanged());
			dashConfig.setValue(SmartDash.AUTO_TURN_ANGLE_1, 58.5);
			System.out.println("config changed " + dashConfig.hasChanged());
			if(lr.isReadFile())
				if (dashConfig.hasChanged())  {
					lr.writeDashboardFile(dashConfig);
				}
		} else {
			System.out.println("could not read dashboard config file");
		}
	}

}
