/**
 * 
 */
package cn.edu.nju.software.ripper.filter;

import cn.edu.nju.software.GuitarModule.GWindow;
import cn.edu.nju.software.ripperCore.GWindowFilter;
import cn.edu.nju.software.ripperModuleData.GUIType;



/**
 * @author  
 *
 */
public class IgnoreWindowFilter extends GWindowFilter {

    /**
     * 
     */
    public IgnoreWindowFilter() {
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see ..guitar.ripper.GWindowFilter#isProcess(..guitar.model.GWindow)
     */
    @Override
    public boolean isProcess(GWindow window) {
        String ID = window.getTitle();
        return false;
    }

    /* (non-Javadoc)
     * @see ..guitar.ripper.GWindowFilter#ripWindow(..guitar.model.GWindow)
     */
    @Override
    public GUIType ripWindow(GWindow window) {
        return null;
    }

}
