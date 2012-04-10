package cn.edu.nju.software.ripperCore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import cn.edu.nju.software.GuitarModule.GIDGenerator;
import cn.edu.nju.software.GuitarModule.IO;
import cn.edu.nju.software.GuitarModule.JFCDefaultIDGeneratorSimple;
import cn.edu.nju.software.ripperModuleData.ComponentListType;
import cn.edu.nju.software.ripperModuleData.GUIStructure;
import cn.edu.nju.software.ripperModuleData.LogWidget;
import cn.edu.nju.software.ripperModuleData.ObjectFactory;
import cn.edu.nju.software.util.GUIStructureInfoUtil;

public class RipperMain {

	private Ripper ripper;

	public static void main(String args[]) {
		new RipperMain().ripperMain(args);
	}

	public void ripperMain(String args[]) {
		// define the launch info of the AUT
//		String path = "E:\\SEAN\\aut.jar";
//		String mainClass = "Project";
//		String mainMethod = "main";
//		String argument = "";
		String path = "E:\\SEAN\\workspace\\AUT2.jar";
		String mainClass = "Example2Frame";
		String mainMethod = "main";
		String argument = "";

		Launch launch = new Launch(path, mainClass, mainMethod, argument);
		launch.autLaunch();

		
		long nStartTime = System.currentTimeMillis();
		OperationConfig config = new OperationConfig();
		ripper = new Ripper(config);
		
		setupEnv();
		ripper.mainRoutine();
		
		
		GUIStructure dGUIStructure=ripper.getResult();
		GUIStructureInfoUtil guistructureinfoutil = new GUIStructureInfoUtil();
		guistructureinfoutil.generate(dGUIStructure, false);
		
		IO.writeObjToFile(dGUIStructure, "Ripper.GUI");
		
		
		ComponentListType lOpenWins = ripper.getlOpenWindowComps();
		ComponentListType lCloseWins = ripper.getlCloseWindowComp();
		ObjectFactory factory = new ObjectFactory();

		LogWidget logWidget = factory.createLogWidget();

		logWidget.setOpenWindow(lOpenWins);
		logWidget.setCloseWindow(lCloseWins);

		// ------------------
		// Elapsed time:
		long nEndTime = System.currentTimeMillis();
		long nDuration = nEndTime - nStartTime;
		DateFormat df = new SimpleDateFormat("HH : mm : ss: SS");

		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		System.out.println("Ripping Elapsed: " + df.format(nDuration));
	}

	private void setupEnv() {
		// TODO Auto-generated method stub
		JFCRipperConfiguration configuration = new JFCRipperConfiguration();
		RipperMonitor monitor = new RipperMonitor(configuration);
		ripper.setMonitor(monitor);
		// Set up IDGenerator
		GIDGenerator jIDGenerator = JFCDefaultIDGeneratorSimple.getInstance();
		ripper.setIDGenerator(jIDGenerator);
	}

}
