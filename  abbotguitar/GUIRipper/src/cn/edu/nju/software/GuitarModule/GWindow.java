/*	
 
 
 * 
 
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
package cn.edu.nju.software.GuitarModule;

import java.util.List;

import cn.edu.nju.software.GuitarWrapper.ComponentTypeWrapper;
import cn.edu.nju.software.ripperModuleData.AttributesType;
import cn.edu.nju.software.ripperModuleData.ComponentType;
import cn.edu.nju.software.ripperModuleData.ContainerType;
import cn.edu.nju.software.ripperModuleData.ContentsType;
import cn.edu.nju.software.ripperModuleData.GUIType;
import cn.edu.nju.software.ripperModuleData.PropertyType;

/**
 * Abstract class for accessible windows in GUITAR
 * 
 * @author  </a>
 * 
 */
public abstract class GWindow implements GObject {

	boolean isRoot = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public abstract boolean equals(Object window);

	/**
	 * Extract window GUI information and convert to GUITAR data format
	 * 
	 * <p>
	 * 
	 * @return GUI properties of the current window
	 */

	public abstract GUIType extractGUIProperties();

	/**
	 * Check if a window is actually valid and worth considering.
	 * 
	 * <p>
	 * 
	 * @return <code>true</code> if the window is valid
	 */
	public abstract boolean isValid();

	/**
	 * Get the component corresponding to the window
	 * 
	 * <p>
	 * 
	 * @return the window component
	 */
	public abstract GComponent getContainer();

	/**
	 * Check if the window is modal or not
	 * 
	 * <p>
	 * 
	 * @return <code>true</code> if the window is modal
	 */
	public abstract boolean isModal();

	/**
	 * Extract window GUI information and convert to GUITAR data format
	 * 
	 * <p>
	 * 
	 * @return GUIType the GUI properties of the window
	 */
	public GUIType extractWindow() {
		GUIType retGUI;

		retGUI = factory.createGUIType();

		// ---------------------
		// Window
		// ---------------------

		ComponentType window = factory.createComponentType();

		ComponentTypeWrapper windowAdapter = new ComponentTypeWrapper(window);

		// Add properties required by GUITAR
		windowAdapter.addValueByName(GUITARConstants.TITLE_TAG_NAME, ""
				+ getTitle());

		// Modal
		windowAdapter.addValueByName(GUITARConstants.MODAL_TAG_NAME, ""
				+ isModal());

		// Is Root window
		windowAdapter.addValueByName(GUITARConstants.ROOTWINDOW_TAG_NAME, ""
				+ isRoot());

		window = windowAdapter.getDComponentType();

		AttributesType attributes = window.getAttributes();
		List<PropertyType> lProperties = attributes.getProperty();
		List<PropertyType> lGUIProperties = getGUIProperties();

		if (lGUIProperties != null)
			lProperties.addAll(lGUIProperties);

		attributes.setProperty(lProperties);
		window.setAttributes(attributes);

		retGUI.setWindow(window);

		// ---------------------
		// Container

		ContainerType container = factory.createContainerType();
		ContentsType contents = factory.createContentsType();
		container.setContents(contents);
		retGUI.setContainer(container);

		return retGUI;
	}

	// --------------------
	// Get window properties used for GUITAR
	// --------------------
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see
	// cn.edu.nju.software.GuitarModule.GXObject#getFirstChildByID(java.lang.String)
	// */
	// public GComponent getFirstChildByID(String sID) {
	// GComponent container = getContainer();
	// return container.getFirstChildByID(sID);
	// }

	/**
	 * Check if the window is a root window or not
	 * 
	 * @return the isRoot
	 */

	public boolean isRoot() {
		return isRoot;
	}

	/**
	 * Indicate that the window is a root window, from which we start ripping.
	 * This flag is set depending on our ripping purposes.
	 * 
	 * <p>
	 * 
	 * @param isRoot
	 *            the isRoot to set
	 */
	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}

}
