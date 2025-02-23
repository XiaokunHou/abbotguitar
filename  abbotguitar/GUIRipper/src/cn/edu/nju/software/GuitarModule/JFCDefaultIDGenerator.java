/*  
 
 
 * 
 
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
package cn.edu.nju.software.GuitarModule;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.software.ripperModuleData.ObjectFactory;

/**
 * Default ID generator for JFC application
 * 
 * @author  </a>
 * 
 */
public class JFCDefaultIDGenerator extends DefaultIDGenerator {

	static JFCDefaultIDGenerator instance = null;
	static ObjectFactory factory = new ObjectFactory();
	static final int prime = 31;

	public static JFCDefaultIDGenerator getInstance() {
		if (instance == null)
			instance = new JFCDefaultIDGenerator();
		return instance;
	}

	private JFCDefaultIDGenerator() {
		super(ID_PROPERTIES);
	}

	static List<String> ID_PROPERTIES = new ArrayList<String>(
			JFCConstants.ID_PROPERTIES);
}
