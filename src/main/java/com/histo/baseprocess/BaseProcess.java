package com.histo.baseprocess;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.xml.sax.SAXException;


public class BaseProcess {
	public static void main(String[] arg) throws FileNotFoundException,SAXException,IOException {
		if(arg.length > 0) {
		String programName = arg[0];
		try {
			Class<?> dynamicClass = Class.forName(programName);
			System.out.println("Loaded class: " + dynamicClass);
			Method dynamicClassMainMethod = dynamicClass.getDeclaredMethod("main", String[].class);
			System.out.println("Got method: " + dynamicClassMainMethod);
			arg = Arrays.copyOfRange(arg, 1, arg.length);
			dynamicClassMainMethod.invoke(null,(Object) arg);

		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
			System.err.println("Class name not found.");
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
		else {
			System.err.println("Illigal arguments");
		}
	}

}
