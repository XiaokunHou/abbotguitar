package cn.edu.nju.software.ripperCore;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.WindowEvent;
import java.util.*;

public class RipperMonitor {

	private OperationConfig config;
	private List<String> ignoredWindowsList =new ArrayList<String>();
	/**
	 * Temporary list of windows opened during the expand event is being
	 * performed. Those windows are in a native form to prevent data loss.
	 * 
	 */
	volatile LinkedList<Window> tempOpenedWinStack = new LinkedList<Window>();

	volatile LinkedList<Window> tempClosedWinStack = new LinkedList<Window>();
	public RipperMonitor(OperationConfig config) {
		// TODO Auto-generated constructor stub
		this.config=config;
	}
	public void setup() {
		// TODO Auto-generated method stub
		Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
		WindowOpenListener listener = new WindowOpenListener();
		toolkit.addAWTEventListener(listener, AWTEvent.WINDOW_EVENT_MASK);
	}
	public void resetWindowCache() {
		this.tempOpenedWinStack.clear();
		this.tempClosedWinStack.clear();
	}
	public boolean isNewWindowOpened() {
		return (tempOpenedWinStack.size() > 0);
		// return (tempGWinStack.size() > 0);
	}
	public LinkedList<GWindow> getOpenedWindowCache() {

		LinkedList<GWindow> retWindows = new LinkedList<GWindow>();

		for (Window window : tempOpenedWinStack) {
			GWindow gWindow = new JFCXWindow(window);
			if (gWindow.isValid())
				retWindows.addLast(gWindow);
		}
		return retWindows;
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
}
