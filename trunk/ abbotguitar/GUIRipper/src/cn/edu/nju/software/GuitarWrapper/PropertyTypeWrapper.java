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
package cn.edu.nju.software.GuitarWrapper;

import java.util.List;

import cn.edu.nju.software.ripperModuleData.PropertyType;

/**
 * @author  </a>
 */
public class PropertyTypeWrapper {
    PropertyType property;

    /**
     * @param property
     */
    public PropertyTypeWrapper(PropertyType property) {
        super();
        this.property = property;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof PropertyTypeWrapper))
            return false;
        PropertyTypeWrapper oPropertyAdapter = (PropertyTypeWrapper) obj;
        PropertyType oProperty = oPropertyAdapter.property;

        String sName = property.getName();
        String oName = oProperty.getName();
        if (sName == null || oName == null)
            return false;

        if (!sName.equals(oName))
            return false;

        List<String> lValue = property.getValue();
        List<String> loValue = oProperty.getValue();

        if (lValue == null && loValue == null)
            return true;
        else if (lValue == null || loValue == null)
            return false;
        else {
            int iLength = lValue.size();
            int ioLength = loValue.size();

            if (iLength != ioLength)
                return false;

            for (int i = 0; i < iLength; i++) {
                String sValue = lValue.get(i);
                String soValue = loValue.get(i);
                if (!sValue.equals(soValue))
                    return false;
            }
            return true;
        }

    }
}
