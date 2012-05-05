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

import cn.edu.nju.software.GuitarModule.GComponent;
import cn.edu.nju.software.GuitarModule.JFCXComponent;

/**
 * 
 * Abstract class for all JFC events in GUITAR.
 * 
 * @author </a>
 */
public abstract class JFCEventHandler extends GThreadEvent {

	/**
	 * A helper method to get the real JFC Accessible object from a
	 * <code> GXComponent </code>
	 * 
	 * @param gComponent
	 * @return Accessible
	 */
	// protected Accessible getAccessible(GComponent gComponent) {
	// JFCXComponent jxComponent = (JFCXComponent) gComponent;
	// return jxComponent.getAComponent();
	// }

	protected Component getComponent(GComponent gComponent) {
		JFCXComponent jxComponent = (JFCXComponent) gComponent;
		return jxComponent.getComponent();
	}

}
