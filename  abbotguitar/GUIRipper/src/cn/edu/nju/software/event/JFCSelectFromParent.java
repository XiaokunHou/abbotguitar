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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.List;

import cn.edu.nju.software.GuitarModule.GComponent;
import cn.edu.nju.software.GuitarModule.JFCXComponent;
 

/**
 * Select a sub-item by calling a selection function form its parent
 * 
 * @author     </a>
 */
public class JFCSelectFromParent extends JFCEventHandler {

	/**
     * 
     */
	public JFCSelectFromParent() {
		// TODO Auto-generated constructor stub
	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see
	// * cn.edu.nju.software.event.ThreadEventHandler#actionPerformImp(.
	// * .guitar.model.GXComponent)
	// */
	// @Override
	// protected void performImpl(GComponent gComponent) {
	// Accessible aChild = ((JFCXComponent) gComponent).getAComponent();
	// Component cChild = (Component) aChild;
	//
	// // Find the closet parent which is support selection
	// Accessible aParent = getSelectableParent(aChild);
	//
	// if (aParent != null) {
	// Method selectionMethod;
	//
	// try {
	// selectionMethod = aParent.getClass().getMethod(
	// "setSelectedComponent", Component.class);
	// selectionMethod.invoke(aParent, cChild);
	// } catch (SecurityException e) {
	// e.printStackTrace();
	// } catch (NoSuchMethodException e) {
	// e.printStackTrace();
	// } catch (IllegalArgumentException e) {
	// e.printStackTrace();
	// } catch (IllegalAccessException e) {
	// e.printStackTrace();
	// } catch (InvocationTargetException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.edu.nju.software.event.ThreadEventHandler#actionPerformImp(.
	 * .guitar.model.GXComponent)
	 */
	@Override
	protected void performImpl(GComponent gComponent,Hashtable<String, List<String>> optionalData) {
//		Accessible aChild = ((JFCXComponent) gComponent).getComponent();
		Component cChild = ((JFCXComponent) gComponent).getComponent();

		// Find the closet parent which is support selection
		Component aParent = getSelectableParent(cChild);

		if (aParent != null) {

			Method selectionMethod;

			try {
				selectionMethod = aParent.getClass().getMethod(
						"setSelectedComponent", Component.class);
				selectionMethod.invoke(aParent, cChild);
			} catch (SecurityException e) {
				e.printStackTrace();
				
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.edu.nju.software.event.ThreadEventHandler#actionPerformImp(.
	 * .guitar.model.GXComponent, java.lang.Object)
	 */
	@Override
	protected void performImpl(GComponent gComponent, Object parameters,Hashtable<String, List<String>> optionalData) {

		Integer index = 0;

		try {
			List<String> lParameter = (List<String>) parameters;

			if (lParameter == null) {
				index = 0;
			} else
				index = (lParameter.size() != 0) ? Integer.parseInt(lParameter
						.get(0)) : 0;
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Accessible aChild = ((JFCXComponent) gComponent).getAComponent();

		Component component = ((JFCXComponent) gComponent).getComponent();
		// Find the closet parent which is support selection

		// Accessible aParent = getSelectableParent(aChild);

		Component parent = getSelectableParent(component);

		System.out.println("!!!!!Parent:"+ parent);
		
		if (parent != null) {
			
			System.out.println("GOT HERE");
			
			Method selectionMethod;

			try {

				selectionMethod = parent.getClass().getMethod(
						"setSelectedIndex", int.class);

				selectionMethod.invoke(parent, index);

			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 
	 * A helper method to find the closest ancestor having setSelectedComponent
	 * method Presumably this method will select the current element.
	 * 
	 * <p>
	 * 
	 * @param aComponent
	 * @return Accessible
	 */
	// private Accessible getSelectableParent(Accessible aComponent) {
	private Component getSelectableParent(Component component) {
		// if (aComponent == null)
		if (component == null)
			return null;

		Component parent = component.getParent();
		Method[] methods = parent .getClass().getMethods();
		for (Method m : methods) {
			if ("setSelectedComponent".equals(m.getName()))
				return parent;
		}

		return getSelectableParent(parent);
		
		
//		AccessibleContext aContext = component.getAccessibleContext();
//
//		if (aContext == null)
//			return null;
//		
//		
//		
//
//		Accessible aParent = aContext.getAccessibleParent();
//		if (aParent == null)
//			return null;
//
//		if (!(aParent instanceof Component))
//			return null;
//
//		


	}

	/* (non-Javadoc)
	 * @see cn.edu.nju.software.event.GEvent#isSupportedBy(cn.edu.nju.software.GuitarModule.GComponent)
	 */
	@Override
	public boolean isSupportedBy(GComponent gComponent) {
		// TODO Auto-generated method stub
		return false;
	}
}
