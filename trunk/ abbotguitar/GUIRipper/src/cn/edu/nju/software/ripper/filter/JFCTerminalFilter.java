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
package cn.edu.nju.software.ripper.filter;

import cn.edu.nju.software.GuitarModule.GComponent;
import cn.edu.nju.software.GuitarModule.GUITARConstants;
import cn.edu.nju.software.GuitarModule.GWindow;
import cn.edu.nju.software.ripperModuleData.ComponentType;



/**
 * @author     </a>
 * 
 */
public class JFCTerminalFilter extends GComponentFilter {

	static GComponentFilter cmIgnoreMonitor = null;

	public synchronized static GComponentFilter getInstance() {
		if (cmIgnoreMonitor == null) {
			cmIgnoreMonitor = new JFCTerminalFilter();
		}
		return cmIgnoreMonitor;
	}

	private JFCTerminalFilter() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ..guitar.ripper.ComponentFilter#isProcess(..guitar.model.GXComponent)
	 */
	@Override
	public boolean isProcess(GComponent gComponent, GWindow window) {
	    
	    if(GUITARConstants.TERMINAL.equals(gComponent.getTypeVal())){
	        return true;
	    }
		return false;
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ..guitar.ripper.ComponentMonitor#ripComponent(..guitar.model.GXComponent)
	 */
	@Override
	public ComponentType ripComponent(GComponent component, GWindow window) {
		return component.extractProperties();
	}
}
