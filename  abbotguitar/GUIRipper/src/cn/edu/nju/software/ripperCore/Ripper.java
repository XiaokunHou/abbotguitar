package cn.edu.nju.software.ripperCore;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import cn.edu.nju.software.GuitarModule.GComponent;
import cn.edu.nju.software.GuitarModule.GIDGenerator;
import cn.edu.nju.software.GuitarModule.GUITARConstants;
import cn.edu.nju.software.GuitarModule.GWindow;
import cn.edu.nju.software.GuitarWrapper.ComponentTypeWrapper;
import cn.edu.nju.software.GuitarWrapper.GUITypeWrapper;
import cn.edu.nju.software.exception.GException;
import cn.edu.nju.software.exception.RipperStateException;
import cn.edu.nju.software.ripper.filter.GComponentFilter;
import cn.edu.nju.software.ripperModuleData.ComponentListType;
import cn.edu.nju.software.ripperModuleData.ComponentType;
import cn.edu.nju.software.ripperModuleData.ContainerType;
import cn.edu.nju.software.ripperModuleData.FullComponentType;
import cn.edu.nju.software.ripperModuleData.GUIStructure;
import cn.edu.nju.software.ripperModuleData.GUIType;
import cn.edu.nju.software.ripperModuleData.ObjectFactory;
import cn.edu.nju.software.util.AppUtil;

public class Ripper {

	JFCRipperConfiguration config;
	static ObjectFactory factory = new ObjectFactory();

	/**
	 * GUIStructure storing the ripped result
	 */
	GUIStructure dGUIStructure = new GUIStructure();
	
	/**
	    * Indicates if regular expression patterns will be used for
	    * matching window titles.
	    */
	   boolean useReg = false;

	/*
	 * Ripper monitor. Monitor performs tasks such as detecting windows.
	 */
	RipperMonitor monitor = null;
	// Opened / closed window list
	ComponentListType lOpenWindowComps;
	ComponentListType lCloseWindowComp;

	// Component filter
	LinkedList<GComponentFilter> lComponentFilter = new LinkedList<GComponentFilter>();

	/**
	 * Comparator for widgets
	 */
	GIDGenerator idGenerator = null;

	// Window filter
	LinkedList<GWindowFilter> lWindowFilter = new LinkedList<GWindowFilter>();

	/**
	 * Indicates thay GUI components images are to be ripped and archived.
	 */
	boolean useImage = false;

	public Ripper() {
		lOpenWindowComps = factory.createComponentListType();
		lCloseWindowComp = factory.createComponentListType();
	}

	public Ripper(JFCRipperConfiguration config) {
		// TODO Auto-generated constructor stub
		this.config = config;
	}

	public void mainRoutine() {
		try {
			if (monitor == null) {
				System.out.println("No monitor hasn't been assigned");
				throw new RipperStateException();
			}

			// 1. Set Up the environment
			//monitor.setUp();

			// 2. Get the list of root window
			List<GWindow> gRootWindows = monitor.getRootWindows();

			if (gRootWindows == null) {
				System.out.println("No root window");
				throw new RipperStateException();
			}

			System.out
					.println("Number of root windows: " + gRootWindows.size());

			// 3. Main step: ripping starting from each root window in the list
			for (GWindow xRootWindow : gRootWindows) {
				xRootWindow.setRoot(true);
				monitor.addRippedList(xRootWindow);

				GUIType gRoot = ripWindow(xRootWindow);
				this.dGUIStructure.getGUI().add(gRoot);
			}

			// 4. Generate ID for widgets
			if (this.idGenerator == null) {
				System.out.println("No ID generator assigned");
				throw new RipperStateException();
			} else {
				idGenerator.generateID(dGUIStructure);
			}

			// 5. Clean up
			monitor.cleanUp();
		} catch (GException e) {
			System.out.println("GUITAR error while ripping" + e);

		} catch (IOException e) {
			System.out.println("IO error while ripping" + e);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Uncaught exception while ripping" + e);
			System.out.println("Likely AUT bug. If not, file GUITAR bug");

		}
	}

	/**
	 * Rip a window
	 * <p>
	 * 
	 * @param gWindow
	 * @return
	 */
	public GUIType ripWindow(GWindow gWindow) throws Exception, IOException {
		System.out.println("------- BEGIN WINDOW -------");
		System.out.println("Ripping window: *" + gWindow.getTitle() + "*");

		// 1. Rip special/customized components
		for (GWindowFilter wf : lWindowFilter) {
			if (wf.isProcess(gWindow)) {
				System.out.println("Window filter "
						+ wf.getClass().getSimpleName() + " is applied");
				return wf.ripWindow(gWindow);
			}
		}

		// 2. Save an image of the window
		 String sUUID = null;
		// if (useImage) {
		// try {
		// sUUID = captureImage(gWindow.getContainer());
		// } catch (AWTException e) {
		// // Ignore AWT exceptions sUUID is null
		// } catch (IOException e) {
		// throw e;
		// }
		// }

		// 3. Rip all components of this window
		try {
			GUIType retGUI = gWindow.extractWindow();
			GComponent gWinContainer = gWindow.getContainer();

			ComponentType container = null;

			// Replace window title with pattern if requested (useReg)
			if (gWinContainer != null) {
				if (this.useReg) {
					AppUtil appUtil = new AppUtil();
					GUITypeWrapper guiTypeWrapper = new GUITypeWrapper(retGUI);
					String sTitle = guiTypeWrapper.getTitle();
					String s = appUtil.findRegexForString(sTitle);
					guiTypeWrapper.setTitle(s);
				}
				container = ripComponent(gWinContainer, gWindow);
			}

			if (container != null) {
				retGUI.getContainer().getContents().getWidgetOrContainer()
						.add(container);
			}

			// Add generated UUID for the component
			if (useImage && sUUID != null) {
				GUITypeWrapper guiTypeWrapper = new GUITypeWrapper(retGUI);
				guiTypeWrapper.addWindowProperty(GUITARConstants.UUID_TAG_NAME,
						sUUID);
			}

			return retGUI;
		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * Rip a component
	 * 
	 * As of now this method does not propagate exceptions. It needs to be
	 * modified to progate exceptions. All callers need to be modified to handle
	 * exceptions.
	 * 
	 * <p>
	 * 
	 * @param component
	 * @return
	 */
	public ComponentType ripComponent(GComponent component, GWindow window) {
		System.out.println("");
		System.out.println("----------------------------------");
		System.out.println("Ripping component: ");
		System.out.println("Signature: ");

		// printComponentInfo(component, window);

		// 1. Rip special/customized components
		for (GComponentFilter cm : lComponentFilter) {
			if (cm.isProcess(component, window)) {
				System.out.println("Filter " + cm.getClass().getSimpleName()
						+ " is applied");

				return cm.ripComponent(component, window);
			}
		}

		// 2. Rip regular components
		ComponentType retComp = null;
		try {
			retComp = component.extractProperties();
			ComponentTypeWrapper compA = new ComponentTypeWrapper(retComp);

			// if (useImage) {
			// String sUUID = null;
			// try {
			// sUUID = captureImage(component);
			// } catch (AWTException e) {
			// // Ignore AWTException. sUUID is null.
			// } catch (IOException e) {
			// throw e;
			// }
			//
			// if (sUUID != null) {
			// compA.addProperty(GUITARConstants.UUID_TAG_NAME, sUUID);
			// }
			// }

			GUIType guiType = null;

			if (window != null) {
				guiType = window.extractGUIProperties();
			}

			retComp = compA.getDComponentType();

			// 2.1 Try to perform action on the component
			// to reveal more windows/components

			// clear window opened cache before performing actions
			monitor.resetWindowCache();

			if (monitor.isExpandable(component, window)) {
				monitor.expandGUI(component);
			} else {
				System.out.println("Component is Unexpandable");
			}

			// Trigger terminal widget

			LinkedList<GWindow> lClosedWindows = monitor.getClosedWindowCache();

			String sTitle = window.getTitle();

			if (lClosedWindows.size() > 0) {

				System.out.println("!!!!! Window closed");

				for (GWindow closedWin : lClosedWindows) {
					String sClosedWinTitle = closedWin.getTitle();

					// Only consider widget closing the current window
					if (sTitle.equals(sClosedWinTitle)) {

						System.out.println("\t" + sClosedWinTitle);

						List<FullComponentType> lCloseComp = lCloseWindowComp
								.getFullComponent();

						FullComponentType cCloseComp = factory
								.createFullComponentType();
						cCloseComp.setWindow(closedWin.extractWindow()
								.getWindow());
						cCloseComp.setComponent(retComp);
						lCloseComp.add(cCloseComp);
						lCloseWindowComp.setFullComponent(lCloseComp);
					} // if
				} // for
			} // if

			if (monitor.isNewWindowOpened()) {

				List<FullComponentType> lOpenComp = lOpenWindowComps
						.getFullComponent();
				FullComponentType cOpenComp = factory.createFullComponentType();
				cOpenComp.setWindow(window.extractWindow().getWindow());
				cOpenComp.setComponent(retComp);
				lOpenComp.add(cOpenComp);
				lOpenWindowComps.setFullComponent(lOpenComp);

				LinkedList<GWindow> lNewWindows = monitor
						.getOpenedWindowCache();
				monitor.resetWindowCache();
				System.out.println(lNewWindows.size()
						+ " new window(s) opened!!!");
				for (GWindow newWins : lNewWindows) {
					System.out
							.println("*\t Title:*" + newWins.getTitle() + "*");
				}

				// Process the opened windows in a FIFO order
				while (!lNewWindows.isEmpty()) {

					GWindow gNewWin = lNewWindows.getLast();
					lNewWindows.removeLast();

					GComponent gWinComp = gNewWin.getContainer();

					if (gWinComp != null) {

						// Add invokelist property for the component
						String sWindowTitle = gNewWin.getTitle();
						compA = new ComponentTypeWrapper(retComp);
						compA.addValueByName(
								GUITARConstants.INVOKELIST_TAG_NAME,
								sWindowTitle);

						System.out.println(sWindowTitle + " recorded");

						retComp = compA.getDComponentType();

						// Check ignore window
						if (!monitor.isIgnoredWindow(gNewWin)) {

							if (!monitor.isRippedWindow(gNewWin)) {
								gNewWin.setRoot(false);
								monitor.addRippedList(gNewWin);

								GUIType dWindow = ripWindow(gNewWin);

								if (dWindow != null)
									dGUIStructure.getGUI().add(dWindow);
							} else {
								System.out.println("Window is ripped!!!");
							}

						} else {
							System.out.println("Window is ignored!!!");
						}
					}

					monitor.closeWindow(gNewWin);
				}
			}

			// TODO: check if the component is still available after ripping
			// its child window
			List<GComponent> gChildrenList = component.getChildren();
			int nChildren = gChildrenList.size();
			int i = 0;

			// Debug

			String lChildren = "[";
			for (int j = 0; j < nChildren; j++) {
				lChildren += gChildrenList.get(j).getTitle() + " - "
						+ gChildrenList.get(j).getClassVal() + "; ";
			}
			lChildren += "]";
			System.out.println("*" + component.getTitle() + "* in window *"
					+ window.getTitle() + "* has " + nChildren + " children: "
					+ lChildren);

			// End debug

			while (i < nChildren) {
				GComponent gChild = gChildrenList.get(i++);
				ComponentType guiChild = ripComponent(gChild, window);

				if (guiChild != null) {
					((ContainerType) retComp).getContents()
							.getWidgetOrContainer().add(guiChild);
				}

				if (nChildren < gChildrenList.size()) {
					nChildren = gChildrenList.size();
				}
			}

		} catch (Exception e) {
			if (e.getClass().getName()
					.contains("StaleElementReferenceException")) {
				/**
				 * This can happen when performing an action causes a page
				 * navigation in the current window, for example, when
				 * submitting a form.
				 */
				System.out.println("Element went away: " + e.getMessage());
			} else {
				// TODO: Must throw exception
				e.printStackTrace();

			}

			/**
			 * We'll return the component we calculated anyway so it gets added
			 * to the GUI map. I'm not entirely sure this is the right thing to
			 * do, but it gets us further anyway.
			 */
			return retComp;
		}

		return retComp;
	}

	/**
	 * @return the monitor
	 */
	public RipperMonitor getMonitor() {
		return monitor;
	}

	/**
	 * @param monitor
	 *            The monitor to set
	 */
	public void setMonitor(RipperMonitor monitor) {
		this.monitor = monitor;
	}

	/**
	 * @return the dGUIStructure
	 */
	public GUIStructure getResult() {
		return dGUIStructure;
	}

	/**
	 * @return the iDGenerator
	 */
	public GIDGenerator getIDGenerator() {
		return idGenerator;
	}

	/**
	 * @param iDGenerator
	 *            IDGenerator to use for the Ripper
	 */
	public void setIDGenerator(GIDGenerator iDGenerator) {
		idGenerator = iDGenerator;
	}

	public ComponentListType getlOpenWindowComps() {
		return lOpenWindowComps;
	}

	public void setlOpenWindowComps(ComponentListType lOpenWindowComps) {
		this.lOpenWindowComps = lOpenWindowComps;
	}

	public ComponentListType getlCloseWindowComp() {
		return lCloseWindowComp;
	}

	public void setlCloseWindowComp(ComponentListType lCloseWindowComp) {
		this.lCloseWindowComp = lCloseWindowComp;
	}

	// Window filter
	// LinkedList<GWindowFilter> lWindowFilter = new
	// LinkedList<GWindowFilter>();
	/**
	 * Add a component filter
	 * 
	 * @param filter
	 */
	public void addComponentFilter(GComponentFilter filter) {
		if (this.lComponentFilter == null) {
			lComponentFilter = new LinkedList<GComponentFilter>();
		}
		lComponentFilter.addLast(filter);
		//filter.setRipper(this);
	}
}
