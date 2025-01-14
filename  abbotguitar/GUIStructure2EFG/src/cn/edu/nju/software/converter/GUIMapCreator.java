package cn.edu.nju.software.converter;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.software.GuitarModule.*;
import cn.edu.nju.software.GuitarWrapper.*;
import cn.edu.nju.software.ripperModuleData.*;



public class GUIMapCreator {

	ObjectFactory factory = new ObjectFactory();

	public GUIMap getGUIMap(GUIStructure gui, EFG efg) {
		GUIMap map = factory.createGUIMap();

		EventMapType eventMap = factory.createEventMapType();
		eventMap.setEventMapElement(efg.getEvents().getEvent());
		map.setEventMap(eventMap);
		WidgetMapType widgetMap = getWidgetMapFromGUIStructure(gui);
		map.setWidgetMap(widgetMap);
		return map;
	}
	
	
	/**
	 * @param oracle
	 * @param map
	 * @return
	 */
	public GUIMap getGUIMapFromOracle(TestCase oracle, GUIMap  map){
		return map;
	}
	
	
	
	

	private WidgetMapType getWidgetMapFromGUIStructure(GUIStructure guiStr) {
		WidgetMapType widgetMap = factory.createWidgetMapType();

		for (GUIType gui : guiStr.getGUI()) {
			List<ComponentType> widgetList = getWidgetListFromGUI(gui);
			for (ComponentType widget : widgetList) {
				WidgetMapElementType widgetMapElement = factory
						.createWidgetMapElementType();
				
				ComponentTypeWrapper wComponent = new ComponentTypeWrapper(
						widget);
				widgetMapElement.setWidgetId(wComponent
						.getFirstValueByName(GUITARConstants.ID_TAG_NAME));

				
				widgetMapElement.setComponent(widget);
				widgetMapElement.setWindow(gui.getWindow());

				widgetMap.getWidgetMapElement().add(widgetMapElement);

			}
		}

		return widgetMap;
	}

	private List<ComponentType> getWidgetListFromGUI(GUIType gui) {
		ComponentType container = gui.getContainer();
		List<ComponentType> widgetList = getWidgetListFromComponent(container);
		return widgetList;
	}

	private List<ComponentType> getWidgetListFromComponent(
			ComponentType component) {

		List<ComponentType> widgetList = new ArrayList<ComponentType>();

		if (hasEvent(component)) {
			ComponentType simpleComponent = factory.createComponentType();
			simpleComponent.setAttributes(component.getAttributes());

			widgetList.add(simpleComponent);
		}

		if (component instanceof ContainerType) {
			ContainerType container = (ContainerType) component;
			for (ComponentType child : container.getContents()
					.getWidgetOrContainer()) {
				List<ComponentType> childWidgetList = getWidgetListFromComponent(child);
				widgetList.addAll(childWidgetList);
			}
		}
		return widgetList;
	}

	private boolean hasEvent(ComponentType component) {
		ComponentTypeWrapper wComp = new ComponentTypeWrapper(component);
		PropertyType event = wComp
				.getFirstPropertyByName(GUITARConstants.EVENT_TAG_NAME);

		if (event == null)
			return false;
		else
			return true;
	}

}
