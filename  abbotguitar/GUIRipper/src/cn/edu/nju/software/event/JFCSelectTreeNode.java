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
package cn.edu.nju.software.event;

import java.awt.Component;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JTree;
import javax.swing.text.Position;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import cn.edu.nju.software.GuitarModule.GComponent;
import cn.edu.nju.software.GuitarModule.JFCConstants;
import cn.edu.nju.software.GuitarModule.JFCXComponent;
 

/**
 * @author   Bao N. Nguyen </a>
 * 
 */
public class JFCSelectTreeNode extends JFCEventHandler {

	/**
	 * 
	 */
	public JFCSelectTreeNode() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.edu.nju.software.event.GThreadEvent#performImpl(cn.edu.nju.software.GuitarModule
	 * .GComponent)
	 */
	@Override
	protected void performImpl(GComponent gComponent,
			Hashtable<String, List<String>> optionalData) {
		
		if (!(gComponent instanceof JFCXComponent)){
		
			System.out.println("JFCXComponent! ");
			return;
		}

		JFCXComponent jComponent = (JFCXComponent) gComponent;
		Component component = jComponent.getComponent();

		if (!(component instanceof JTree)){
			System.out.println("NOT JTree! ");
			return;
		}

		JTree tree = (JTree) component;

		List<String> nodes = optionalData.get(JFCConstants.TITLE_TAG);

		if (nodes == null){
			System.out.println("No option! ");
			return;
		}
		
		if (nodes.size() < 1){
			System.out.println("Selecting....");
			return;	
		}
		
		String node = nodes.get(0);
		expandAll(tree, true);
//		new EventTool().waitNoEvent(1000);

		TreePath path = tree.getNextMatch(node, 0, Position.Bias.Forward);
		
		
		tree.setSelectionPath(path);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.edu.nju.software.event.GThreadEvent#performImpl(cn.edu.nju.software.GuitarModule
	 * .GComponent, java.lang.Object)
	 */
	@Override
	protected void performImpl(GComponent gComponent, Object parameters,
			Hashtable<String, List<String>> optionalData) {
		performImpl(gComponent, optionalData);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.edu.nju.software.event.GEvent#isSupportedBy(cn.edu.nju.software.GuitarModule.
	 * GComponent)
	 */
	@Override
	public boolean isSupportedBy(GComponent gComponent) {
		// TODO Auto-generated method stub
		return false;
	}

	private void expandAll(JTree tree, boolean expand) {
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		// Traverse tree from root
		expandAllHelper(tree, new TreePath(root), expand);
	}

	/**
	 * @param tree
	 */
	private void expandAllHelper(JTree tree, TreePath parent, boolean expand) {
		// Traverse children
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				expandAllHelper(tree, path, expand);
			}
		}

		// Expansion or collapse must be done bottom-up
		if (expand) {
			tree.expandPath(parent);
		} else {
			tree.collapsePath(parent);
		}
	}
}
