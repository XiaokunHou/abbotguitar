package cn.edu.nju.software.ripperCore;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.accessibility.AccessibleAction;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleText;
import javax.imageio.ImageIO;

import org.netbeans.jemmy.EventTool;

import abbot.finder.AWTHierarchy;
import abbot.finder.Hierarchy;

import cn.edu.nju.software.GuitarModule.GApplication;
import cn.edu.nju.software.GuitarModule.GComponent;
import cn.edu.nju.software.GuitarModule.GUITARConstants;
import cn.edu.nju.software.GuitarModule.GWindow;
import cn.edu.nju.software.GuitarModule.JFCApplication;
import cn.edu.nju.software.GuitarModule.JFCConstants;
import cn.edu.nju.software.GuitarModule.JFCXComponent;
import cn.edu.nju.software.GuitarModule.JFCXWindow;
import cn.edu.nju.software.event.EventManager;
import cn.edu.nju.software.event.GEvent;
import cn.edu.nju.software.event.JFCActionEDT;
import cn.edu.nju.software.event.JFCEventHandler;
import cn.edu.nju.software.exception.ApplicationConnectException;


public class RipperMonitor {

	// --------------------------
	// Configuartion Parameters
	// --------------------------

	/**
     * 
     */
	private static final int INITIAL_DELAY = 1000;

	// Logger logger;
	JFCRipperConfiguration configuration;

	List<String> sIgnoreWindowList = new ArrayList<String>();
	
	  /**
     * 
     * List of windows ripped
     * 
     */
    volatile Set<String> lRippedWindow = new HashSet<String>();

	/**
	 * Constructor
	 * 
	 * <p>
	 * 
	 * @param configuration
	 *            ripper configuration
	 */
	public RipperMonitor(JFCRipperConfiguration configuration) {
		super();
		// this.logger = logger;
		this.configuration = configuration;
	}

	List<String> sRootWindows = new ArrayList<String>();

	/**
	 * Temporary list of windows opened during the expand event is being
	 * performed. Those windows are in a native form to prevent data loss.
	 * 
	 */
	volatile LinkedList<Window> tempOpenedWinStack = new LinkedList<Window>();

	volatile LinkedList<Window> tempClosedWinStack = new LinkedList<Window>();

	// volatile LinkedList<GWindow> tempGWinStack = new LinkedList<GWindow>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see ..guitar.ripper.RipperMonitor#cleanUp()
	 */
	public void cleanUp() {
		// Debugger.pause("Clean up pause....");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ..guitar.ripper.RipperMonitor#closeWindow(..guitar.
	 * model.GXWindow)
	 */
	public void closeWindow(GWindow gWindow) {

		JFCXWindow jWindow = (JFCXWindow) gWindow;
		Window window = jWindow.getWindow();

		// TODO: A bug might happen here, will fix later
		// window.setVisible(false);
		window.dispose();

	}
	
	/**
	 * checks for the rippedWindow
	 * @param window
	 * @return ripped windowName
	 */
    public boolean isRippedWindow(GWindow window) {
        String sWindowName = window.getTitle();
        return (lRippedWindow.contains(sWindowName));
    }

	/**
	 * Adds the ripped winodws to ripped window list
	 * @param window
	 */
    public void addRippedList(GWindow window) {
        String windowTitle = window.getTitle();
        this.lRippedWindow.add(windowTitle);
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ..guitar.ripper.RipperMonitor#expand(..guitar.model
	 * .GXComponent)
	 */
	public void expandGUI(GComponent component) {

		if (component == null)
			return;

		System.out.println("Expanding *" + component.getTitle() + "*...");

		// GThreadEvent action = new JFCActionHandler();
		GEvent action = new JFCActionEDT();

		action.perform(component, null);
		System.out.println("Waiting  " + configuration.DELAY
				+ "ms for a new window to open");

		new EventTool().waitNoEvent(configuration.DELAY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ..guitar.ripper.RipperMonitor#getOpenedWindowCache()
	 */
	public LinkedList<GWindow> getOpenedWindowCache() {

		LinkedList<GWindow> retWindows = new LinkedList<GWindow>();

		for (Window window : tempOpenedWinStack) {
			GWindow gWindow = new JFCXWindow(window);
			if (gWindow.isValid())
				retWindows.addLast(gWindow);
		}
		return retWindows;
	}

	public LinkedList<GWindow> getClosedWindowCache() {

		LinkedList<GWindow> retWindows = new LinkedList<GWindow>();

		for (Window window : tempClosedWinStack) {
			GWindow gWindow = new JFCXWindow(window);
			if (gWindow.isValid())
				retWindows.addLast(gWindow);
		}
		return retWindows;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ..guitar.ripper.RipperMonitor#getRootWindows()
	 */
	public List<GWindow> getRootWindows() {

		List<GWindow> retWindowList = new ArrayList<GWindow>();

		retWindowList.clear();

//		Hierarchy hierarchy=AWTHierarchy.getDefault();
//		Collection roots=hierarchy.getRoots();

		Frame[] roots = Frame.getFrames();

		for (Object frame1 : roots) {
				Frame frame=(Frame)frame1;
			if (!isValidRootWindow(frame))
				continue;

			AccessibleContext xContext = frame.getAccessibleContext();
			String sWindowName = xContext.getAccessibleName();

			if (sRootWindows.size() == 0
					|| (sRootWindows.contains(sWindowName))) {

				GWindow gWindow = new JFCXWindow(frame);
				retWindowList.add(gWindow);
				// frame.requestFocus();
			}
		}

		// / Debugs:
		System.out.println("Root window size: " + retWindowList.size());
		for (GWindow window : retWindowList) {
			System.out.println("Window title: " + window.getTitle());
		}

		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			System.out.println(e);
		}
		return retWindowList;
	}

	/**
	 * Check if a root window is worth ripping
	 * 
	 * <p>
	 * 
	 * @param window
	 *            the window to consider
	 * @return true/false
	 */
	private boolean isValidRootWindow(Frame window) {

		// Check if window is valid
		// if (!window.isValid())
		// return false;

		// Check if window is visible
		if (!window.isVisible())
			return false;

		// Check if window is on screen
		// double nHeight = window.getSize().getHeight();
		// double nWidth = window.getSize().getWidth();
		// if (nHeight <= 0 || nWidth <= 0)
		// return false;

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ..guitar.ripper.RipperMonitor#isExpandable(..guitar
	 * .model.GXComponent, ..guitar.model.GXWindow)
	 */
	boolean isExpandable(GComponent gComponent, GWindow window) {

		JFCXComponent jComponent = (JFCXComponent) gComponent;
		// Accessible aComponent = jComponent.getAComponent();
		//
		// if (aComponent == null)
		// return false;

		Component component = jComponent.getComponent();
		AccessibleContext aContext = component.getAccessibleContext();

		String ID = gComponent.getTitle();
		if (ID == null)
			return false;

		if ("".equals(ID))
			return false;

		if (!gComponent.isEnable()) {
			System.out.println("Component is disabled");
			return false;
		}

		if (!isClickable(component)) {
			return false;
		}

		if (gComponent.getTypeVal().equals(GUITARConstants.TERMINAL))
			return false;

		// // Check for more details
		// AccessibleContext aContext = component.getAccessibleContext();

		if (aContext == null)
			return false;

		AccessibleText aText = aContext.getAccessibleText();

		if (aText != null)
			return false;

		return true;
	}

	/**
	 * Check if a component is click-able.
	 * 
	 * @param component
	 * @return true/false
	 */
	private boolean isClickable(Component component) {

		AccessibleContext aContext = component.getAccessibleContext();

		if (aContext == null)
			return false;

		AccessibleAction action = aContext.getAccessibleAction();

		if (action == null)
			return false;

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ..guitar.ripper.RipperMonitor#isIgnoredWindow(..guitar
	 * .model.GXWindow)
	 */
	public boolean isIgnoredWindow(GWindow window) {
		String sWindow = window.getTitle();
		// TODO: Ignore template
		return (this.sIgnoreWindowList.contains(sWindow));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ..guitar.ripper.RipperMonitor#isNewWindowOpened()
	 */
	public boolean isNewWindowOpened() {
		return (tempOpenedWinStack.size() > 0);
		// return (tempGWinStack.size() > 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ..guitar.ripper.RipperMonitor#resetWindowCache()
	 */
	public void resetWindowCache() {
		this.tempOpenedWinStack.clear();
		this.tempClosedWinStack.clear();
	}

	public class WindowOpenListener implements AWTEventListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.AWTEventListener#eventDispatched(java.awt.AWTEvent)
		 */
		@Override
		public void eventDispatched(AWTEvent event) {

			switch (event.getID()) {
			case WindowEvent.WINDOW_OPENED:
				processWindowOpened((WindowEvent) event);
				break;
			case WindowEvent.WINDOW_ACTIVATED:
			case WindowEvent.WINDOW_DEACTIVATED:
			case WindowEvent.WINDOW_CLOSING:
				processWindowClosed((WindowEvent) event);
				break;

			default:
				break;
			}

		}

		/**
		 * @param event
		 */
		private void processWindowClosed(WindowEvent wEvent) {
			Window window = wEvent.getWindow();
			tempClosedWinStack.add(window);
		}

		/**
		 * @param wEvent
		 */
		private void processWindowOpened(WindowEvent wEvent) {
			Window window = wEvent.getWindow();
			tempOpenedWinStack.add(window);
		}
	}

	Toolkit toolkit;

	/*
	 * (non-Javadoc)
	 * 
	 * @see ..guitar.ripper.RipperMonitor#setUp()
	 */
	public void setUp() {

		// Registering default supported events

		EventManager em = EventManager.getInstance();

		for (Class<? extends JFCEventHandler> event : JFCConstants.DEFAULT_SUPPORTED_EVENTS) {
			try {
				em.registerEvent(event.newInstance());
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Registering customized supported event
		Class<? extends GEvent> gCustomizedEvents;

		String[] sCustomizedEventList;
		if (JFCRipperConfiguration.CUSTOMIZED_EVENT_LIST != null)
			sCustomizedEventList = JFCRipperConfiguration.CUSTOMIZED_EVENT_LIST
					.split(GUITARConstants.CMD_ARGUMENT_SEPARATOR);
		else
			sCustomizedEventList = new String[0];

		for (String sEvent : sCustomizedEventList) {
			try {
				Class<? extends GEvent> cEvent = (Class<? extends GEvent>) Class
						.forName(sEvent);
				em.registerEvent(cEvent.newInstance());
			} catch (ClassNotFoundException e) {
				System.out.println(e);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		// Set up parameters
		sIgnoreWindowList = JFCConstants.sIgnoredWins;

		// Start the application
		GApplication application;
		try {
			String[] URLs;
			if (JFCRipperConfiguration.URL_LIST != null)
				URLs = JFCRipperConfiguration.URL_LIST
						.split(GUITARConstants.CMD_ARGUMENT_SEPARATOR);
			else
				URLs = new String[0];

			application = new JFCApplication(JFCRipperConfiguration.MAIN_CLASS,
					JFCRipperConfiguration.USE_JAR, URLs);

			// Parsing arguments
			String[] args;
			if (JFCRipperConfiguration.ARGUMENT_LIST != null)
				args = JFCRipperConfiguration.ARGUMENT_LIST
						.split(GUITARConstants.CMD_ARGUMENT_SEPARATOR);
			else
				args = new String[0];

			application.connect(args);

			// Delay
			try {
				System.out
						.println("Initial waiting: "
								+ JFCRipperConfiguration.INITIAL_WAITING_TIME
								+ "ms...");
				Thread.sleep(JFCRipperConfiguration.INITIAL_WAITING_TIME);
			} catch (InterruptedException e) {
				System.out.println(e);
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		} catch (ApplicationConnectException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// -----------------------------
		// Assign listener
		toolkit = java.awt.Toolkit.getDefaultToolkit();

		WindowOpenListener listener = new WindowOpenListener();
		toolkit.addAWTEventListener(listener, AWTEvent.WINDOW_EVENT_MASK);

	}

	/**
	 * 
	 * Add a root window to be ripped
	 * 
	 * <p>
	 * 
	 * @param sWindowName
	 *            the window name
	 */
	public void addRootWindow(String sWindowName) {
		this.sRootWindows.add(sWindowName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ..guitar.ripper.GRipperMonitor#isWindowClose()
	 */
	public boolean isWindowClosed() {
		return (tempClosedWinStack.size() > 0);
	}

	/**
	 * Captures the image of a GUITAR GUI component and saves it to a the
	 * specified image file.
	 * 
	 * @param component
	 *            GUITAR component to capture
	 * @param strFilePath
	 *            File path name to store the image (w/o extension)
	 * @return void
	 */
	public void captureImage(GComponent component, String strFilePath)
			throws AWTException, IOException {
		Robot robot;

		try {
			robot = new Robot();

			JFCXComponent gComp = (JFCXComponent) component;
			Component comp = gComp.getComponent();

			// Bail out if component is not really displayed on screen
			if (!comp.isShowing()) {
				throw new AWTException("Component is not visible");
			}

			Point pos = comp.getLocationOnScreen();
			Dimension dim = comp.getSize();

			// Ignore non-visible components
			if (dim.getHeight() == 0 || dim.getWidth() == 0) {
				throw new AWTException("Width or height is 0");
			}

			Rectangle bounder = new Rectangle(pos, dim);

			BufferedImage screenshot = robot.createScreenCapture(bounder);
			File outputfile = new File(strFilePath + ".png");
			ImageIO.write(screenshot, "png", outputfile);

		} catch (IOException e) {
			throw e;
		} catch (AWTException e) {
			throw e;
		}
	}

}
