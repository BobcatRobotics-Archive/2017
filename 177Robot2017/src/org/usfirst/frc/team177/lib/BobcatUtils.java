package org.usfirst.frc.team177.lib;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

//Repository for some generically useful functions

public class BobcatUtils {
	
	
	//Blatantly stolen for ChezzyPoofs 2014 code
	public static double boundAngle0to2PiRadians(double angle) {
	    // Naive algorithm
	    while (angle >= 2.0 * Math.PI) {
	      angle -= 2.0 * Math.PI;
	    }
	    while (angle < 0.0) {
	      angle += 2.0 * Math.PI;
	    }
	    return angle;
	  }

	  public static String getFile(String fileName) {
		  DataInputStream fileStream;
 
		  byte[] buffer = new byte[255];
		  String content = "";
		  System.out.println(fileName);
		  try {
			  fileStream = new DataInputStream(new FileInputStream(fileName));
		      
		      while (fileStream.read(buffer, 0, 255) != -1) {
		        content += new String(buffer);
		      }
		      fileStream.close();
		      
		  } catch (IOException e) {
		      System.out.println( "*******" + fileName + " was not found!");
		  }
		  System.out.println("Filename:"+fileName+"\t#:"+content.length());
		  
		  return content;
	  }
	  
	  /**
	   * Returns the array of substrings obtained by dividing the given input string at each occurrence
	   * of the given delimiter.
	   */
	  public static String[] split(String input, String delimiter) {
	    Vector<String> node = new Vector<String>();
	    int index = input.indexOf(delimiter);
	    while (index >= 0) {
	      node.addElement(input.substring(0, index));
	      input = input.substring(index + delimiter.length());
	      index = input.indexOf(delimiter);
	    }
	    node.addElement(input);

	    String[] retString = new String[node.size()];
	    for (int i = 0; i < node.size(); ++i) {
	      retString[i] = (String) node.elementAt(i);
	    }

	    return retString;
	  }
	
	  
	  public static double boundAngleNegPiToPiRadians(double angle) {
		  // Naive algorithm
		   while (angle >= Math.PI) {
			   angle -= 2.0 * Math.PI;
		   }
		   while (angle < -Math.PI) {
			   angle += 2.0 * Math.PI;
		   }
		   return angle;
	  }
	  
	  /**
	   * Get the difference in angle between two angles.
	   *
	   * @param from The first angle
	   * @param to The second angle
	   * @return The change in angle from the first argument necessary to line up
	   * with the second. Always between -Pi and Pi
	   */
	  public static double getDifferenceInAngleRadians(double from, double to) {
	    return boundAngleNegPiToPiRadians(to - from);
	  }

	
}
