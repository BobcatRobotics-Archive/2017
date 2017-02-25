package org.usfirst.frc.team177.lib;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ResourceReader {

	// private RioLoggerThread logger = new RioLoggerThread();

	public ResourceReader() {
		super();
	}

	public Double[] readFile() {
		List<Double> data = new ArrayList<Double>();
		Scanner reader = new Scanner(new InputStreamReader(getClass().getResourceAsStream("curve.dat")));
		while (reader.hasNextLine()) {
			String line = reader.nextLine();
			String val[] = line.split(" ");
			data.add(new Double(val[0]));
			data.add(new Double(val[1]));
		}
		reader.close();

		return data.toArray(new Double[0]);
	}

}
