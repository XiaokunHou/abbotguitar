/*	
 *  Copyright (c) 2009-@year@. The GUITAR group at the University of Maryland. Names of owners of this group may
 *  be obtained by sending an e-mail to atif@cs.umd.edu
 * 
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
 *  documentation files (the "Software"), to deal in the Software without restriction, including without 
 *  limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 *	the Software, and to permit persons to whom the Software is furnished to do so, subject to the following 
 *	conditions:
 * 
 *	The above copyright notice and this permission notice shall be included in all copies or substantial 
 *	portions of the Software.
 *
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT 
 *	LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO 
 *	EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER 
 *	IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR 
 *	THE USE OR OTHER DEALINGS IN THE SOFTWARE. 
 */
package cn.edu.nju.software.ripper.filter;

import java.awt.Component;

import javax.swing.JTabbedPane;

import cn.edu.nju.software.event.GEvent;
import cn.edu.nju.software.event.JFCSelectFromParent;
import cn.edu.nju.software.model.GComponent;
import cn.edu.nju.software.model.GUITARConstants;
import cn.edu.nju.software.model.GWindow;
import cn.edu.nju.software.model.JFCXComponent;
import cn.edu.nju.software.model.data.ComponentType;
import cn.edu.nju.software.model.data.ContainerType;
import cn.edu.nju.software.model.wrapper.ComponentTypeWrapper;

/**
 * @author <a href="mailto:baonn@cs.umd.edu"> Bao Nguyen </a>
 * 
 */
public class JFCTabFilter extends GComponentFilter {

	static GComponentFilter cmIgnoreMonitor = null;

	public synchronized static GComponentFilter getInstance() {
		if (cmIgnoreMonitor == null) {
			cmIgnoreMonitor = new JFCTabFilter();
		}
		return cmIgnoreMonitor;
	}

	private JFCTabFilter() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.umd.cs.guitar.ripper.ComponentFilter#isProcess(edu.umd.cs.guitar.
	 * model.GXComponent)
	 */
	@Override
	public boolean isProcess(GComponent component, GWindow window) {

		if (!(component instanceof JFCXComponent))
			return false;

		JFCXComponent jComponent = (JFCXComponent) component;
		boolean isProcess = (jComponent.getComponent() instanceof JTabbedPane);
		return isProcess;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.umd.cs.guitar.ripper.ComponentFilter#ripComponent(edu.umd.cs.guitar
	 * .model.GXComponent)
	 */
	@Override
	public ComponentType ripComponent(GComponent component, GWindow window) {

		ComponentType retComp = component.extractProperties();
		ComponentTypeWrapper wRetComp = new ComponentTypeWrapper(retComp);

		// Remove the event in Tab
		wRetComp.removeProperty(GUITARConstants.EVENT_TAG_NAME);

		JFCXComponent jComponent = (JFCXComponent) component;

		// Accessible aTabPanel = jComponent.getAComponent();
		// AccessibleContext aContext = aTabPanel.getAccessibleContext();
		// AccessibleSelection eSelection = aContext.getAccessibleSelection();
		//        
		// List<GXComponent> jTabItems = component.getChildren();
		//        
		// for(GXComponent jTabItem: jTabItems){
		// eSelection.
		// }

		JTabbedPane jTab = (JTabbedPane) jComponent.getComponent();

		// Debug
		// debbug(jTab);

		int nChild = jTab.getComponentCount();

		for (int i = 0; i < nChild; i++) {

			Component child = jTab.getComponent(i);
			// GComponent gChild = new JFCXComponent((Accessible) child);
			GComponent gChild = new JFCXComponent(child,window);
			// Select tab
			// Debugger.pause("About to select");
			GEvent eTabSelect = new JFCSelectFromParent();
			eTabSelect.perform(gChild, null);
			// jTab.setSelectedComponent(child);
			// End Select tab

			ComponentType guiChild = ripper.ripComponent(gChild, window);
			ComponentTypeWrapper wGuiChild = new ComponentTypeWrapper(guiChild);

			wGuiChild.addValueByName(GUITARConstants.EVENT_TAG_NAME,
					JFCSelectFromParent.class.getName());

			// Debug
			// Debugger.pause("Child index: "+jTab.get);

			// End of Debug

			((ContainerType) retComp).getContents().getWidgetOrContainer().add(
					guiChild);
		}

		// for (GXComponent gTabItem : gTabItems) {
		// Debugger.pause("Tab");
		//            
		// Component jTabIem = (Component) ((JFCXComponent) gTabItem)
		// .getAComponent();
		// jTab.setSelectedComponent(jTabIem);
		//            
		// }

		return retComp;
	}
}
