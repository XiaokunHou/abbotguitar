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
package cn.edu.nju.software.GuitarWrapper;

import cn.edu.nju.software.ripperModuleData.WidgetMapElementType;
import cn.edu.nju.software.ripperModuleData.WidgetMapType;

/**
 * @author  </a>
 */
public class WidgetMapWrapper extends WidgetMapType
{

    public WidgetMapWrapper(WidgetMapType wm)
    {
        this.widgetMapElement = wm.getWidgetMapElement();
    }

    /**
     * Get widget map element using id
     * <p>
     * 
     * @param id
     * @return
     */
    WidgetMapElementType getWidgetMap(String id)
    {
        for (WidgetMapElementType  wm: widgetMapElement)
        {
            if (id.equals(wm.getWidgetId()))
                return wm;
        }
        return null;

    }
}
