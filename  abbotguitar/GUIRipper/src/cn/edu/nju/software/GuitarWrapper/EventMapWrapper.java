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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import cn.edu.nju.software.ripperModuleData.EventMapType;
import cn.edu.nju.software.ripperModuleData.EventType;
import cn.edu.nju.software.ripperModuleData.ObjectFactory;

/**
 * @author  </a>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EventMapType", propOrder = { "eventMapElement" })
public class EventMapWrapper extends EventMapType
{
    static ObjectFactory factory = new ObjectFactory();

    public EventMapWrapper(EventMapType em)
    {
        this.eventMapElement = em.getEventMapElement();
    }

    /**
     * Look up event using id
     * 
     * <p>
     * 
     * @param id
     * @return
     */
    EventType getEvent(String id)
    {
        for (EventType event : eventMapElement)
        {
            if (id.equals(event.getEventId()))
                return event;
        }
        return null;

    }
}
