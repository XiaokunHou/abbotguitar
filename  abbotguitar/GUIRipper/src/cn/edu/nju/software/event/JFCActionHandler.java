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
package cn.edu.nju.software.event;

import java.awt.Component;
import java.util.Hashtable;
import java.util.List;

import javax.accessibility.AccessibleAction;
import javax.accessibility.AccessibleContext;
import javax.swing.SwingUtilities;

import cn.edu.nju.software.GuitarModule.GComponent;
import cn.edu.nju.software.GuitarModule.JFCXComponent;

/**
 * @author     </a>
 */
public class JFCActionHandler extends JFCEventHandler {

	/**
     * 
     */
	public JFCActionHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void performImpl(GComponent gComponent,Hashtable<String, List<String>> optionalData) {

		// System.out.println("Waiting  2000ms for a new window to open");
		// new QueueTool().waitEmpty(2000);

		if (gComponent == null) {
			return;
		}
		// Accessible aComponent = getAccessible(gComponent);
		//
		// if (aComponent == null)
		// return;
		Component component = getComponent(gComponent);

		AccessibleContext aContext = component.getAccessibleContext();

		if (aContext == null)
			return;
		final AccessibleAction aAction = aContext.getAccessibleAction();

		if (aAction == null)
			return;

		// try {

		int nActions = aAction.getAccessibleActionCount();
		if (nActions > 0) {

			// aAction.doAccessibleAction(0);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					aAction.doAccessibleAction(0);
				}
			});

		}
		// } catch (Exception e) {
		// System.out.println("Cannot expand", e);
		//
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.edu.nju.software.event.JFCEventHandler#actionPerformImp(..guitar
	 * .model.GXComponent, java.lang.Object)
	 */
	@Override
	protected void performImpl(GComponent gComponent, Object parameters,Hashtable<String, List<String>> optionalData) {
		performImpl(gComponent,optionalData);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.edu.nju.software.event.GEvent#isSupportedBy(cn.edu.nju.software.GuitarModule.
	 * GComponent)
	 */
	@Override
	public boolean isSupportedBy(GComponent gComponent) {

		GEvent gFilterEvent;
		gFilterEvent= new JFCEditableTextHandler();
		if (gFilterEvent.isSupportedBy(gComponent))
			return false;
		
		if (!(gComponent instanceof JFCXComponent))
			return false;
		JFCXComponent jComponent = (JFCXComponent) gComponent;
		Component component = jComponent.getComponent();
		AccessibleContext aContext = component.getAccessibleContext();

		if (aContext == null)
			return false;

		Object event;
		// Action
		event = aContext.getAccessibleAction();
		if (event != null)
			return true;

		return false;
	}
}
