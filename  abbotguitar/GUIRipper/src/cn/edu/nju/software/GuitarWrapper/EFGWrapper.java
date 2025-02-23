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

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.software.GuitarModule.GUITARConstants;
import cn.edu.nju.software.ripperModuleData.EFG;
import cn.edu.nju.software.ripperModuleData.EventGraphType;
import cn.edu.nju.software.ripperModuleData.EventType;
import cn.edu.nju.software.ripperModuleData.EventsType;
import cn.edu.nju.software.ripperModuleData.GUIStructure;
import cn.edu.nju.software.ripperModuleData.ObjectFactory;
import cn.edu.nju.software.ripperModuleData.RowType;

/**
 * @author  </a>
 */
public class EFGWrapper {

	/**
     * 
     */
	private static final String EVENT_NAME_PREFIX = "e";
	ObjectFactory factory = new ObjectFactory();
	EFG efg;
	EventsType dEventList;

	List<EventWrapper> wEventList;

	// EventGraphType eventGraph;
	List<List<String>> eventGraph;

	/**
	 * Generate efg graph (follow relations) from the GUI Structure
	 * <p>
	 * 
	 * @param dGUIStructure
	 */
	@Deprecated
	public void parseEFG(GUIStructure dGUIStructure) {
		GUIStructureWrapper wGUIStrcuture = new GUIStructureWrapper(
				dGUIStructure);
		wGUIStrcuture.parseData();

		// Create event list
		wEventList = new ArrayList<EventWrapper>();

		List<GUITypeWrapper> wWindowList = wGUIStrcuture.lGUI;

		for (GUITypeWrapper window : wWindowList) {
			readEventList(window.container);
		}

		efg = factory.createEFG();

		// Reading event name
		dEventList = factory.createEventsType();

		for (EventWrapper wEvent : wEventList) {
			EventType dEvent = factory.createEventType();
			dEvent.setWidgetId(wEvent.component
					.getFirstValueByName(GUITARConstants.ID_TAG_NAME));
			dEvent.setAction(wEvent.action);

			String eventType;

			// // Change to match Charlie's code
			// if (wEvent.component.window.isRoot() && !wEvent.isHidden())
			// eventType = "r";
			// else
			// eventType = "e";
			//
			// dEvent.setEventId(eventType + wEventList.indexOf(wEvent));
			dEvent.setEventId(EVENT_NAME_PREFIX + wEventList.indexOf(wEvent));

			dEventList.getEvent().add(dEvent);
		}
		efg.setEvents(dEventList);

		eventGraph = new ArrayList<List<String>>();
		for (List<String> row : eventGraph) {
			row = new ArrayList<String>();
		}
		//
		EventGraphType dEventGraph = factory.createEventGraphType();

		List<RowType> lRowList = new ArrayList<RowType>();

		for (EventWrapper firstEvent : wEventList) {
			int indexFirst = wEventList.indexOf(firstEvent);
			System.out.println("Anlyzing row: " + indexFirst);
			RowType row = factory.createRowType();

			for (EventWrapper secondEvent : wEventList) {
				int indexSecond = wEventList.indexOf(secondEvent);
				int cellValue = firstEvent.isFollowedBy(secondEvent);
				row.getE().add(indexSecond, cellValue);
			}
			lRowList.add(indexFirst, row);
		}
		dEventGraph.setRow(lRowList);
		efg.setEventGraph(dEventGraph);
	}

	/**
	 * @return the efg
	 */
	public EFG getEfg() {
		return efg;
	}

	/**
	 * @param window
	 */
	private void readEventList(ComponentTypeWrapper component) {
		// if (component.window != null) {
		// System.out.println(component.window.getID()
		// + " :"
		// + component
		// .getFirstValueByName(GUITARConstants.ID_TAG_NAME));
		// }

		List<String> sActionList = component
				.getValueListByName(GUITARConstants.EVENT_TAG_NAME);

		if (sActionList != null)
			for (String action : sActionList) {
				EventWrapper wEvent = new EventWrapper();
				wEvent.action = action;
				wEvent.component = component;
				wEventList.add(wEvent);
			}
		List<ComponentTypeWrapper> wChildren = component.getChildren();

		if (wChildren != null)
			for (ComponentTypeWrapper wChild : wChildren) {
				readEventList(wChild);
			}
	}

	/**
	 * @return the wEventList
	 */
	public List<EventWrapper> getwEventList() {
		return wEventList;
	}

	public EventType getEventByID(String ID) {
		for (EventType event : this.efg.getEvents().getEvent()) {
			if (ID.equals(event.getEventId()))
				return event;
		}
		return null;

	}

	public void addEdge(String sourceID, String targetID) {
		addEdge(sourceID, targetID, GUITARConstants.FOLLOW_EDGE);
	}

	public EFGWrapper(EFG efg) {
		super();
		this.efg = efg;
	}

	/**
	 * @param sourceID
	 * @param targetID
	 * @param relation
	 */
	public void addEdge(String sourceID, String targetID, int relation) {
		EventType sourceEvent = getEventByID(sourceID);
		EventType targetEvent = getEventByID(targetID);

		int sourceIndex = efg.getEvents().getEvent().indexOf(sourceEvent);
		int targeIndex = efg.getEvents().getEvent().indexOf(targetEvent);

		RowType row = efg.getEventGraph().getRow().get(sourceIndex);
		row.getE().set(targeIndex, relation);
		efg.getEventGraph().getRow().set(sourceIndex, row);

	}

	/**
	 * @param sourceID
	 * @param targetID
	 * @return
	 */
	public int getEdge(String sourceID, String targetID) {
		EventType source = getEventByID(sourceID);
		EventType target = getEventByID(targetID);
		if (source == null || target == null)
			return GUITARConstants.NO_EDGE;

		int nRow = efg.getEvents().getEvent().indexOf(source);

		int nCol = efg.getEvents().getEvent().indexOf(target);

		RowType row = efg.getEventGraph().getRow().get(nRow);
		return row.getE().get(nCol);

	}

	/**
	 * @param sourceID
	 * @param targetID
	 * @return
	 */
	public int getEdge(EventType source, EventType target) {
		if (source == null || target == null)
			return GUITARConstants.NO_EDGE;
		return getEdge(source.getEventId(), target.getEventId());

	}
}
