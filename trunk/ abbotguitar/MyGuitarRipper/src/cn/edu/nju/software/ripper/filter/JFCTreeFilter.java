/*  
 *  Copyright (c) 2009-@year@. The GUITAR group at the University of Maryland. Names of owners of this group may
 *  be obtained by sending an e-mail to atif@cs.umd.edu
 * 
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
 *  documentation files (the "Software"), to deal in the Software without restriction, including without 
 *  limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 *  the Software, and to permit persons to whom the Software is furnished to do so, subject to the following 
 *  conditions:
 * 
 *  The above copyright notice and this permission notice shall be included in all copies or substantial 
 *  portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT 
 *  LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO 
 *  EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER 
 *  IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR 
 *  THE USE OR OTHER DEALINGS IN THE SOFTWARE. 
 */
package cn.edu.nju.software.ripper.filter;

import java.awt.Component;
import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import cn.edu.nju.software.event.JFCSelectTreeNode;
import cn.edu.nju.software.model.GComponent;
import cn.edu.nju.software.model.GUITARConstants;
import cn.edu.nju.software.model.GWindow;
import cn.edu.nju.software.model.JFCConstants;
import cn.edu.nju.software.model.JFCXComponent;
import cn.edu.nju.software.model.data.AttributesType;
import cn.edu.nju.software.model.data.ComponentType;
import cn.edu.nju.software.model.data.ContainerType;
import cn.edu.nju.software.model.data.ObjectFactory;
import cn.edu.nju.software.model.data.PropertyType;
import cn.edu.nju.software.model.wrapper.ComponentTypeWrapper;

/**
 * @author <a href="mailto:baonn@cs.umd.edu"> Bao N. Nguyen </a>
 * 
 */
public class JFCTreeFilter extends GComponentFilter {

	static GComponentFilter cmIgnoreMonitor = null;
	static ObjectFactory factory = new ObjectFactory();

	public synchronized static GComponentFilter getInstance() {
		if (cmIgnoreMonitor == null) {
			cmIgnoreMonitor = new JFCTreeFilter();
		}
		return cmIgnoreMonitor;
	}

	private JFCTreeFilter() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.umd.cs.guitar.ripper.filter.GComponentFilter#isProcess(edu.umd.cs
	 * .guitar.model.GComponent, edu.umd.cs.guitar.model.GWindow)
	 */
	@Override
	public boolean isProcess(GComponent component, GWindow window) {
		if (!(component instanceof JFCXComponent))
			return false;

		JFCXComponent jComponent = (JFCXComponent) component;

		if (jComponent.getComponent() instanceof JTree)
			return true;

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.umd.cs.guitar.ripper.filter.GComponentFilter#ripComponent(edu.umd
	 * .cs.guitar.model.GComponent, edu.umd.cs.guitar.model.GWindow)
	 */
	@Override
	public ComponentType ripComponent(GComponent gGomponent, GWindow window) {

		ComponentType retComp = gGomponent.extractProperties();
		ComponentTypeWrapper wRetComp = new ComponentTypeWrapper(retComp);
		wRetComp.removeProperty(GUITARConstants.EVENT_TAG_NAME);

		JFCXComponent jComponent = (JFCXComponent) gGomponent;
		Component component = jComponent.getComponent();
		JTree tree = (JTree) component;
		TreeModel model = tree.getModel();

		Object root = model.getRoot();

		if (!(root instanceof DefaultMutableTreeNode))
			return retComp;

		DefaultMutableTreeNode nRoot = (DefaultMutableTreeNode) root;
		expandAll(tree, true);

		for (Enumeration e = nRoot.children(); e.hasMoreElements();) {
			DefaultMutableTreeNode nChild = (DefaultMutableTreeNode) e
					.nextElement();

			GComponent gChild = new JFCXComponent(jComponent.getComponent(),window);
			
			ComponentType cChild = gChild .extractProperties();
			
			AttributesType at = factory.createAttributesType();

			PropertyType property;

			property = factory.createPropertyType();
			at.getProperty().add(property);
			property.setName(JFCConstants.TITLE_TAG);
			String sTitle = nChild.toString();
			property.getValue().add(sTitle);

			ComponentTypeWrapper wChild = new ComponentTypeWrapper(cChild);
			
			wChild.removeProperty(GUITARConstants.EVENT_TAG_NAME);
			wChild.addValueByName(GUITARConstants.EVENT_TAG_NAME,
					JFCSelectTreeNode.class.getName());
			
			cChild = wChild.getDComponentType();
			cChild.setOptional(at);
			((ContainerType) retComp).getContents().getWidgetOrContainer().add(
					cChild);
		}

		return wRetComp.getDComponentType();
	}

	// private ComponentType getTreeProperties(){
	//		
	// }

	/**
	 * @param root
	 * @return
	 */
	private int getNodeCount(Object root) {

		// GUITARLog.log.debug("Entering getNodeCount");
		if (!(root instanceof DefaultMutableTreeNode))
			return 0;

		DefaultMutableTreeNode nRoot = (DefaultMutableTreeNode) root;
		int nChildren = nRoot.getChildCount();

		// GUITARLog.log.debug("nRoot.getChildCount(): "+nChildren );

		if (nChildren >= 0) {
			for (Enumeration e = nRoot.children(); e.hasMoreElements();) {
				DefaultMutableTreeNode nChild = (DefaultMutableTreeNode) e
						.nextElement();
				nChildren += getNodeCount(nChild);
			}
		}

		return nChildren;

	}

	private void expandAll(JTree tree, boolean expand) {
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		// Traverse tree from root
		expandAllHelper(tree, new TreePath(root), expand);
	}

	/**
	 * @param tree
	 */
	private void expandAllHelper(JTree tree, TreePath parent, boolean expand) {
		// Traverse children
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				expandAllHelper(tree, path, expand);
			}
		}

		// Expansion or collapse must be done bottom-up
		if (expand) {
			tree.expandPath(parent);
		} else {
			tree.collapsePath(parent);
		}
	}

}
