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

import cn.edu.nju.software.ripperModuleData.ObjectFactory;
import cn.edu.nju.software.ripperModuleData.PropertyType;

/**
 * Abstract class for accessible objects in GUITAR
 * 
 * @author  </a>
 * 
 */
public interface GObject {

	static ObjectFactory factory = new ObjectFactory();

	/**
	 * Get Title of the component from a combination of its GUI properties
	 * 
	 * @return component title
	 */
	public String getTitle();

	/**
	 * Get x coordinate of the object
	 * 
	 * @return component's X position
	 */
	public abstract int getX();

	/**
	 * Get y coordinate of the object
	 * 
	 * @return component's Y position
	 */
	public abstract int getY();

	/**
	 * Get all GUI properties of the GUI object (in addition to GUITAR
	 * properties)
	 * 
	 * <p>
	 * 
	 * @return list of component's property list
	 */
	public List<PropertyType> getGUIProperties();
}