package cn.edu.nju.software.ripper.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import cn.edu.nju.software.GuitarModule.GIDGenerator;
import cn.edu.nju.software.GuitarModule.IO;
import cn.edu.nju.software.GuitarWrapper.AttributesTypeWrapper;
import cn.edu.nju.software.ripperModuleData.AttributesType;
import cn.edu.nju.software.ripperModuleData.ComponentListType;
import cn.edu.nju.software.ripperModuleData.ComponentType;
import cn.edu.nju.software.ripperModuleData.Configuration;
import cn.edu.nju.software.ripperModuleData.FullComponentType;
import cn.edu.nju.software.ripperModuleData.GUIStructure;
import cn.edu.nju.software.ripperModuleData.LogWidget;
import cn.edu.nju.software.ripperModuleData.ObjectFactory;
import cn.edu.nju.software.util.GUITARLog;


/** Base template for ripper main function. This takes care of a lot
    of the boilerplate code such as setting up the logger, the ripper
    monitor, the id generator, and the widget configuration file.
 */
public abstract class RipperMain {
    protected NewGRipperConfiguration config;

    public RipperMain(NewGRipperConfiguration config) {
        this.config = config;
    }

    private Ripper ripper;
    private String log4jFile;

    public void execute() {
        log4jFile = config.LOG_FILE;
        System.setProperty("file.name", log4jFile);
//        PropertyConfigurator.configure(GUITARConstants.LOG4J_PROPERTIES_FILE);

        GUITARLog.log = Logger.getLogger(this.getClass().getSimpleName());
        long nStartTime = System.currentTimeMillis();

        ripper = new Ripper(GUITARLog.log);

        try {
			/*
			   WEBGUITAR11

			   (1) Setting up environment
			   (2) Start of execution

			   */

            setupEnv();
            ripper.execute();
        } catch (Exception e) {
            GUITARLog.log.error("RipperMain: ", e);
        }

        GUIStructure dGUIStructure = ripper.getResult();
        IO.writeObjToFile(dGUIStructure, config.GUI_FILE);

        GUITARLog.log.info("-----------------------------");
        GUITARLog.log.info("OUTPUT SUMARY: ");
        GUITARLog.log.info("Number of Windows: "
                + dGUIStructure.getGUI().size());
        GUITARLog.log.info("GUI file:" + config.GUI_FILE);
        GUITARLog.log.info("Open Component file:" + config.LOG_WIDGET_FILE);
        ComponentListType lOpenWins = ripper.getlOpenWindowComps();
        ComponentListType lCloseWins = ripper.getlCloseWindowComp();
        ObjectFactory factory = new ObjectFactory();

        LogWidget logWidget = factory.createLogWidget();
        logWidget.setOpenWindow(lOpenWins);
        logWidget.setCloseWindow(lCloseWins);

        IO.writeObjToFile(logWidget, config.LOG_WIDGET_FILE);

        // ------------------
        // Elapsed time:
        long nEndTime = System.currentTimeMillis();
        long nDuration = nEndTime - nStartTime;
        DateFormat df = new SimpleDateFormat("HH : mm : ss: SS");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        GUITARLog.log.info("Ripping Elapsed: " + df.format(nDuration));
        GUITARLog.log.info("Log file: " + config.LOG_FILE);
    }

    protected void setupEnv() {
        // Load configuration file from disk
        Configuration conf = (Configuration) IO.readObjFromFile(
            config.CONFIG_FILE, Configuration.class);

        GRipperMonitor monitor = createMonitor();
        ripper.setMonitor(monitor);

        GIDGenerator idGenerator = getIdGenerator();
        ripper.setIDGenerator(idGenerator);
    }

    private void loadWidgetConfiguration(
        List<FullComponentType> widgetList,
        List<AttributesTypeWrapper> widgetSignatures)
    {
        for (FullComponentType widget : widgetList) {
            ComponentType component = widget.getWindow();
            if (component == null) {
                component = widget.getComponent();
            }
            AttributesType attributes = component.getAttributes();
            widgetSignatures.add(
                new AttributesTypeWrapper(component.getAttributes()));
        }
    }

    protected abstract GRipperMonitor createMonitor();

    protected abstract GIDGenerator getIdGenerator();
}
