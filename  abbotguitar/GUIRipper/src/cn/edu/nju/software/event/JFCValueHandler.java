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

import javax.accessibility.AccessibleContext;

import cn.edu.nju.software.GuitarModule.GComponent;
import cn.edu.nju.software.GuitarModule.JFCXComponent;

/**
 * @author     </a>
 */
public class JFCValueHandler extends JFCEventHandler {

	/**
     * 
     */
	public JFCValueHandler() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.edu.nju.software.event.JEventHandler#actionPerformImp(..guitar
	 * .model.GXComponent)
	 */
	@Override
	protected void performImpl(GComponent component,
			Hashtable<String, List<String>> optionalData) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.edu.nju.software.event.JFCEventHandler#actionPerformImp(..guitar
	 * .model.GXComponent, java.lang.Object)
	 */
	@Override
	protected void performImpl(GComponent gComponent, Object parameters,
			Hashtable<String, List<String>> optionalData) {
		// TODO Auto-generated method stub

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
		gFilterEvent = new JFCActionHandler();
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
		event = aContext.getAccessibleValue();
		if (event != null)
			return true;

		return false;
	}

}
