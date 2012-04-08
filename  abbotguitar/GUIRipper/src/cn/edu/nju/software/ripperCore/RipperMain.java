package cn.edu.nju.software.ripperCore;

import java.awt.Component;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import abbot.editor.HierarchyWriter;
import abbot.finder.AWTHierarchy;
import abbot.finder.Hierarchy;
import abbot.tester.JButtonTester;

public class RipperMain {

	public static void main(String args[]) {
		// define the launch info of the AUT
		String path = "C:\\Users\\huotuotuofly\\Desktop\\aut.jar";
		String mainClass = "Project";
		String mainMethod = "main";
		String argument = "";

		Launch launch = new Launch(path, mainClass, mainMethod, argument);
		launch.autLaunch();

		OperationConfig config = new OperationConfig();
		Ripper ripper = new Ripper(config);
		ripper.mainRoutine();
	}

}
