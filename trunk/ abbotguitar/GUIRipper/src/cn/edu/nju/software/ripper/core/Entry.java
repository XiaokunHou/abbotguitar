package cn.edu.nju.software.ripper.core;

public class Entry {
	public static void ripperBin(String mainClass, String guiStrL,
			String configL, String delay) {
		String arguments[] = { "-c", mainClass, "-g", guiStrL, "-cf", configL,
				"-d", delay };
		JFCRipperMain.main(arguments);
	}

	public static void main(String args[]) {
		boolean useJar = false;
		String mainClass = "Project";
		String guiStrL = ".\\Ripper\\Demo.GUI";
		String confL = ".\\Ripper\\configuration.xml";
		String delay = "500";
		// boolean useJar = true;
		// String mainClass = "Project";
		// String guiStrL = "C:\\Demo.GUI";
		// String confL = "E:\\SEAN\\aut.jar";
		// String delay = "500";
		// if (false == useJar)
		ripperBin(mainClass, guiStrL, confL, delay);
		// if (true == useJar)
		// ripperJar();
	}

	private static void ripperJar() {
		// TODO Auto-generated method stub

	}

}
