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
 * Abstract class for all GUITAR events requiring to run in a separate thread.
 * All subclasses must implement the <code> actionPerformImp </code> methods.
 * 
 * <p>
 * 
 * @author </a>
 */
public abstract class GThreadEvent implements GEvent {

	/**
     * 
     */
	public GThreadEvent() {
		super();
		if (threadGroup == null) {
			threadGroup = new DispatchThreadGroup("GThreadEvent group");
		}
	}

	static DispatchThreadGroup threadGroup;

	@Override
	public void perform(GComponent gComponent, List<String> parameters,
			Hashtable<String, List<String>> optionalData) {
		Thread t = new Thread(threadGroup, new DispatchThread(gComponent,
				parameters, optionalData));

		t.start();

	}

	@Override
	public void perform(GComponent gComponent,
			Hashtable<String, List<String>> optionalData) {
		Thread t = new Thread(threadGroup, new DispatchThread(gComponent,
				optionalData));
		t.start();
	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see
	// *
	// cn.edu.nju.software.event.GEvent#perform(cn.edu.nju.software.GuitarModule.GComponent
	// * , java.lang.Object, java.lang.Object)
	// */
	// @Override
	// public void perform(GComponent gComponent, Object parameters,
	// Object optionalData) {
	// Thread t = new Thread(threadGroup, new DispatchThread(gComponent));
	// // Thread t = new Thread(new DispatchThread(gComponent));
	// t.start();
	// }

	/**
	 * The actual implementation of the event without parameters
	 * 
	 * <p>
	 * 
	 * @param gComponent
	 */
	protected abstract void performImpl(GComponent gComponent,
			Hashtable<String, List<String>> optionalData);

	/**
	 * 
	 * The actual implementation of the event with parameters
	 * 
	 * @param gComponent
	 * @param parameters
	 */
	protected abstract void performImpl(GComponent gComponent,
			Object parameters, Hashtable<String, List<String>> optionalData);

	/**
	 * A helper class to group all event dispatching threads
	 * 
	 * <p>
	 * 
	 * @author Bao N. Nguyen </a>
	 * 
	 */
	class DispatchThreadGroup extends ThreadGroup {

		/**
		 * @param name
		 */
		public DispatchThreadGroup(String name) {
			super(name);
		}

		@Override
		public void uncaughtException(Thread t, Throwable e) {
			e.printStackTrace();
			System.err.println(this.getName() + " uncaught Exception!!!");
			throw (RuntimeException) e;
		}
	}

	/**
	 * A helper class to run action in a separate thread.
	 * 
	 * <p>
	 * 
	 * @author </a>
	 */
	// private class DispatchThread extends Thread {
	private class DispatchThread implements Runnable {

		GComponent gComponent;
		Object parameters = null;
		Hashtable<String, List<String>> optionalData = null;

		/**
		 * @param gComponent
		 * @param optionalData
		 */
		public DispatchThread(GComponent gComponent,
				Hashtable<String, List<String>> optionalData) {
			super();
			this.gComponent = gComponent;
			this.optionalData = optionalData;
		}

		/**
		 * @param gComponent
		 * @param parameters
		 * @param optionalData
		 */
		public DispatchThread(GComponent gComponent, Object parameters,
				Hashtable<String, List<String>> optionalData) {
			super();
			this.gComponent = gComponent;
			this.parameters = parameters;
			this.optionalData = optionalData;
		}

		@Override
		public void run() {
			synchronized (gComponent) {

				if (parameters == null)
					performImpl(gComponent, optionalData);
				else
					performImpl(gComponent, parameters, optionalData);

			}
		}
	}
}