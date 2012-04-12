package cn.edu.nju.software.ripperCore;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import cn.edu.nju.software.GuitarModule.GIDGenerator;
import cn.edu.nju.software.GuitarModule.GUITARConstants;
import cn.edu.nju.software.GuitarModule.IO;
import cn.edu.nju.software.GuitarModule.JFCConstants;
import cn.edu.nju.software.GuitarModule.JFCDefaultIDGeneratorSimple;
import cn.edu.nju.software.GuitarWrapper.AttributesTypeWrapper;
import cn.edu.nju.software.GuitarWrapper.ComponentTypeWrapper;
import cn.edu.nju.software.ripper.filter.GComponentFilter;
import cn.edu.nju.software.ripper.filter.IgnoreSignExpandFilter;
import cn.edu.nju.software.ripper.filter.JFCTabFilter;
import cn.edu.nju.software.ripperModuleData.AttributesType;
import cn.edu.nju.software.ripperModuleData.ComponentListType;
import cn.edu.nju.software.ripperModuleData.ComponentType;
import cn.edu.nju.software.ripperModuleData.Configuration;
import cn.edu.nju.software.ripperModuleData.FullComponentType;
import cn.edu.nju.software.ripperModuleData.GUIStructure;
import cn.edu.nju.software.ripperModuleData.LogWidget;
import cn.edu.nju.software.ripperModuleData.ObjectFactory;
import cn.edu.nju.software.util.DefaultFactory;
import cn.edu.nju.software.util.GUIStructureInfoUtil;

public class RipperMain {

	private Ripper ripper;
	JFCRipperConfiguration configuration;

	public static void main(String args[]) {
		new RipperMain().ripperMain(args);
	}

	public void ripperMain(String args[]) {
		// define the launch info of the AUT
		String path = "E:\\SEAN\\aut.jar";
		String mainClass = "Project";
		String mainMethod = "main";
		String argument = "";
		// String path = "E:\\SEAN\\workspace\\AUT2.jar";
		// String mainClass = "Example2Frame";
		// String mainMethod = "main";
		// String argument = "";

		Launch launch = new Launch(path, mainClass, mainMethod, argument);
		launch.autLaunch();

		long nStartTime = System.currentTimeMillis();
		//OperationConfig config = new OperationConfig();
		configuration = new JFCRipperConfiguration();
		ripper = new Ripper(configuration);

		setupEnv();
		ripper.mainRoutine();

		GUIStructure dGUIStructure = ripper.getResult();
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
		// --------------------------
				// Terminal list

				// Try to find absolute path first then relative path

				Configuration conf = null;

				try {
					conf = (Configuration) IO.readObjFromFile(
							JFCRipperConfiguration.CONFIG_FILE, Configuration.class);

					if (conf == null) {
						InputStream in = getClass()
								.getClassLoader()
								.getResourceAsStream(JFCRipperConfiguration.CONFIG_FILE);
						conf = (Configuration) IO.readObjFromFile(in,
								Configuration.class);
					}

				} catch (Exception e) {
					System.out.println("*** No configuration file."
							+ " Using an empty one ***");
				}

				if (conf == null) {
					DefaultFactory df = new DefaultFactory();
					conf = df.createDefaultConfiguration();
				}

				List<FullComponentType> cTerminalList = conf.getTerminalComponents()
						.getFullComponent();

				for (FullComponentType cTermWidget : cTerminalList) {
					ComponentType component = cTermWidget.getComponent();
					AttributesType attributes = component.getAttributes();

					if (attributes != null) {
						JFCConstants.sTerminalWidgetSignature
								.add(new AttributesTypeWrapper(component
										.getAttributes()));
					}
				}

				//GRipperMonitor jMonitor = new JFCRipperMointor(CONFIG);

				List<FullComponentType> lIgnoredComps = new ArrayList<FullComponentType>();

				ComponentListType ignoredComponentList = conf.getIgnoredComponents();

				if (ignoredComponentList != null) {
					for (FullComponentType fullComp : ignoredComponentList
							.getFullComponent()) {
						ComponentType comp = fullComp.getComponent();

						// TODO: Shortcut here
						if (comp == null) {
							ComponentType win = fullComp.getWindow();
							ComponentTypeWrapper winAdapter = new ComponentTypeWrapper(
									win);
							String sWindowTitle = winAdapter
									.getFirstValueByName(GUITARConstants.TITLE_TAG_NAME);
							if (sWindowTitle != null)
								JFCConstants.sIgnoredWins.add(sWindowTitle);

						} else
							lIgnoredComps.add(fullComp);
					}
				}

				// --------------------------
				// Ignore components xml
				GComponentFilter jIgnoreExpand = new IgnoreSignExpandFilter(
						lIgnoredComps);
				ripper.addComponentFilter(jIgnoreExpand);

				// Setup tab components ripper filter
				GComponentFilter jTab = JFCTabFilter.getInstance();
				ripper.addComponentFilter(jTab);
		
		
		// TODO Auto-generated method stub
		//JFCRipperConfiguration configuration = new JFCRipperConfiguration();
		RipperMonitor monitor = new JFCRipperMointor(configuration);
		ripper.setMonitor(monitor);
		// Set up IDGenerator
		GIDGenerator jIDGenerator = JFCDefaultIDGeneratorSimple.getInstance();
		ripper.setIDGenerator(jIDGenerator);
	}

}
