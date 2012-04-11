package cn.edu.nju.software.converter;

import cn.edu.nju.software.GuitarModule.*;
import cn.edu.nju.software.ripperModuleData.*;



public class MainGraphConverter {
	public String GUI_FILE = "GUITAR-Default.GUI";
	public String EFG_FILE = "GUITAR-Default.EFG";
	public String PLUGIN="EFGConverter";
	public String MAP_FILE;
	public MainGraphConverter(String gUIPath) {
		// TODO Auto-generated constructor stub
		this.GUI_FILE=gUIPath;
	}

	public static void main(String args[]){
		System.out.println("start converting... ");
		String GUIPath="GUITAR-Default.GUI";
		MainGraphConverter converter=new MainGraphConverter(GUIPath);
		converter.execute();
	}

	private void execute() {
		// TODO: Make a configuration for parameters

		XMLHandler xmlHandler = new XMLHandler();

		String converterFullName = EFGConverter.class.getPackage().getName()
				+ "." + PLUGIN;

		Class<?> converterClass;
		Object graph = null;
		GUIStructure gui = null;
		try {
			converterClass = Class.forName(converterFullName);
			GraphConverter plugin = (GraphConverter) converterClass
					.newInstance();

			gui = (GUIStructure) (xmlHandler.readObjFromFile(GUI_FILE,
					GUIStructure.class));
			graph = plugin.generate(gui);
			xmlHandler.writeObjToFile(graph, EFG_FILE);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out
					.println("The converter can not be found. Please make ensure that the converter name is correct and the corresponding .jar file can be reached.");
		} catch (Exception e) {
			System.out.println("Unknown ERROR");
			e.printStackTrace();
		}

		// Convert map file
		if (gui != null && graph != null)
			if (MAP_FILE != null && (graph instanceof EFG)) {
				EFG efg = (EFG) graph;
				GUIMapCreator gui2map = new GUIMapCreator();
				GUIMap map = gui2map.getGUIMap(gui, efg);
				xmlHandler.writeObjToFile(map, MAP_FILE);
			}

		StringBuffer buff = new StringBuffer();

		System.out.println("===========================================");

		System.out.println("GUIStructure2GraphConverter");
		System.out.println("\tPlugin: \t" + PLUGIN);
		System.out.println("\tInput GUI: \t" + GUI_FILE);
		System.out.println("\tOutput EFG: \t" + EFG_FILE);
		if (MAP_FILE != null) {
			System.out.println("\tOutput MAP: \t" + MAP_FILE);
		}

		System.out.println("===========================================");

		System.out.println(buff);
	}

}
