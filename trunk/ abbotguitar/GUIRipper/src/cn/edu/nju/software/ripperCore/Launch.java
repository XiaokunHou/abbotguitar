package cn.edu.nju.software.ripperCore;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class Launch {
	private String path;
	private String mainClass;
	private String mainMethod;
	private String argument;

	public Launch(String path, String mainClass, String mainMethod,
			String argument) {
		// TODO Auto-generated constructor stub
		this.path = path;
		this.mainClass = mainClass;
		this.mainMethod = mainMethod;
		this.argument=argument;
	}

	public void autLaunch() {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		// dynamic include the jar file. And run it.
		String arg[] = new String[] {};
		File jarFile = new File(path);
		Class classToLoad = null;
		Method method = null;
		try {
			URL[] url = new URL[] { jarFile.toURI().toURL() };
			URLClassLoader child = new URLClassLoader(url, this.getClass()
					.getClassLoader());
			classToLoad = Class.forName(mainClass, true, child);
			// Method method=classToLoad.getMethod(mainMethod);
			method = classToLoad.getDeclaredMethod(mainMethod, String[].class);
			Object instance = classToLoad.newInstance();
			String arg1[] = {};
			Object obj[] = new Object[] { arg1 };
			// method.invoke(instance,obj);
			method.invoke(instance, (Object) arg1);
			// ripperCore();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.out.println("Can not get the url of the jar file!");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Can not initialize the main class!");
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			System.out
					.println("Can not access the main methd because of security problems!");
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			System.out.println("Can not find the main method!");
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			try {
				// String [] args=new String[]{};
				String arg1[] = {};
				Object obj[] = new Object[] { arg1 };
				method.invoke(classToLoad, obj);
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvocationTargetException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// e.printStackTrace();
		}

	}

}
