package cn.edu.nju.software.ripper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.cli.ParseException;

public class Launcher {
	public static void main(String[] args) throws ClassNotFoundException,
			NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, InstantiationException, Exception {
		try {
			// First argument is the name of the factory class
			if (args.length == 0) {
				throw new Exception("Expected factory class as first argument");
			}

			// Retrieve plugin info from the command line
			Class class_ = Class.forName(args[0]);
			Constructor<PluginInfo> init = class_.getConstructor();
			PluginInfo plugin = init.newInstance();

			// String[] pluginArgs = Arrays.copyOfRange(args, 1, args.length);
			int newLength = args.length - 1;
			String[] pluginArgs = new String[newLength];
			System.arraycopy(args, 1, pluginArgs, 0, newLength);
			RipperMain ripper = PluginFactory.createRipper(pluginArgs, plugin);

			/*
			 * WEBGUITAR11
			 * 
			 * The ripper starts execution here.
			 */
			ripper.execute();
		} catch (ParseException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}

		System.exit(0);
	}
}
