/**
 * 
 */
package cn.edu.nju.software.ripper.filter;

import cn.edu.nju.software.model.GWindow;
import cn.edu.nju.software.model.data.GUIType;
import cn.edu.nju.software.ripper.GWindowFilter;

/**
 * @author Bao Nguyen
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
     * @see edu.umd.cs.guitar.ripper.GWindowFilter#isProcess(edu.umd.cs.guitar.model.GWindow)
     */
    @Override
    public boolean isProcess(GWindow window) {
        String ID = window.getTitle();
        return false;
    }

    /* (non-Javadoc)
     * @see edu.umd.cs.guitar.ripper.GWindowFilter#ripWindow(edu.umd.cs.guitar.model.GWindow)
     */
    @Override
    public GUIType ripWindow(GWindow window) {
        return null;
    }

}
