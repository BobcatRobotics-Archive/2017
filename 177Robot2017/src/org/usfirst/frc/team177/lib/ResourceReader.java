package org.usfirst.frc.team177.lib;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Scanner;

public class ResourceReader {

	private RioLoggerThread logger = new RioLoggerThread();
	
	public ResourceReader() {
		super();
	}

	public double[][] readFile() {
		InputStream in = getClass().getResourceAsStream("curve.dat");
		Scanner reader = new Scanner(new InputStreamReader(in));
		
		int nbrDoubles = 0;
		 while (reader.hasNextLine()) {
			 nbrDoubles++;
		 }
		reader.close();
		
		logger.log("nbr of lines " + nbrDoubles);
		
		double[][] curveData = new double[nbrDoubles][nbrDoubles];
		int idx = 0;
		reader = new Scanner(new InputStreamReader(in));
		 while (reader.hasNextLine()) {
			String line = reader.nextLine();
			String val[] = line.split(" ");
			curveData [idx][0] = new Double(val[0]);
			curveData [idx][1] = new Double(val[1]);
			idx++;
		 }
		return curveData;
	
	}
	
}
