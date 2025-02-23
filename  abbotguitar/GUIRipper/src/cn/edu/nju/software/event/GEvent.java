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

import java.util.Hashtable;
import java.util.List;

import cn.edu.nju.software.GuitarModule.GComponent;

/**
 * Abstract class for all events in GUITAR.
 * 
 * <p>
 * 
 * @author     </a>
 *
 */
public interface GEvent {
	
	
	/**
	 * Perform the event with arguments and optional data
	 * 
	 * <p>
	 * 
	 * @param gComponent
	 * @param parameters
	 * @param optionalData
	 */
//	public abstract void perform(GComponent gComponent, Object parameters, Object optionalData);
	
	/**
	 * Perform the event with arguments
	 *  
	 * <p>
	 * 
	 * @param gComponent
	 * @param parameters
	 */
	public abstract void perform(GComponent gComponent, List<String> parameters, Hashtable<String, List<String>> optionalData);

	/**
	 * Perform the event without argument
	 *  
	 * <p>
	 * 
	 * @param gComponent
	 */
	public abstract void perform(GComponent gComponent,Hashtable<String, List<String>> optionalData);
	
	
//	public abstract void perform(GComponent gComponent,Hashtable<String, List<String>>);
	
	
	/**
	 * Check if the event is supported by a <code> GComponent </code>
	 * 
	 *  <p>
	 *   
	 * @param gComponent
	 * @return
	 * 	<code> true </code> if the event is supported by the component <code> gComponent </code>  
	 */
	public abstract boolean isSupportedBy(GComponent gComponent);
	
}