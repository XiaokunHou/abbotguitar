package cn.edu.nju.software.virtualize;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.software.GuitarModule.*;
import cn.edu.nju.software.GuitarWrapper.*;

import cn.edu.nju.software.ripperModuleData.*;


/**
 * Gui Structure to Event Flow Graph converter
 * 
 * @version 1.1
 */
public class EFGConverter implements GraphConverter {

   /**
    * 
    */
   private static final String EVENT_ID_SPLITTER = "_";
   /**
     * 
     */
   private static final String EVENT_ID_PREFIX = "e";
   ObjectFactory factory = new ObjectFactory();

   EFG efg;
   EventsType dEventList;
   List<List<String>> eventGraph;

   /**
    * Event list wrapper
    */
   List<EventWrapper> wEventList = new ArrayList<EventWrapper>();

   /*
    * (non-Javadoc)
    * 
    * @see cn.edu.nju.software.guitar.graph.plugin.GraphConverter#getInputType()
    */
   public Class<?> getInputType() {
      return GUIStructure.class;
   }

   /**
    * Convert the GUI Structure to an Event Flow Graph and Return the Event
    * Flow Graph Object
    * 
    * @param url
    *            GUI Structure to convert
    * @return the Event Flow Graph corresponding to the i,put GUI Structure
    * @see GraphConverter
    */
   /*
    * (non-Javadoc)
    * 
    * @see
    * cn.edu.nju.software.guitar.graph.plugin.GraphConverter#generate(java.lang.Object)
    */
   public Object generate(Object obj) throws InstantiationException {
      if (!(obj instanceof GUIStructure)) {
         throw new InstantiationException("The input class should be "
               + GUIStructure.class.getName());
      }

      GUIStructure dGUIStructure = (GUIStructure) obj;

      dGUIStructure.getGUI().get(0).getContainer().getContents()
            .getWidgetOrContainer();
      GUIStructureWrapper wGUIStructure = new GUIStructureWrapper(
            dGUIStructure);

      wGUIStructure.parseData();

      List<GUITypeWrapper> wWindowList = wGUIStructure.getGUIs();

      for (GUITypeWrapper window : wWindowList) {
         readEventList(window.getContainer());
      }

      efg = factory.createEFG();

      // -------------------------------------
      // Reading event name
      // -------------------------------------
      dEventList = factory.createEventsType();
      for (EventWrapper wEvent : wEventList) {
         EventType dEvent = factory.createEventType();

         String index = getIndexFromWidget(wEvent);

         // dEvent.setEventId(EVENT_ID_PREFIX + index);
         dEvent.setEventId(wEvent.getID());

         dEvent.setWidgetId(wEvent.getComponent().getFirstValueByName(
               GUITARConstants.ID_TAG_NAME));

         dEvent.setType(wEvent.getType());
         dEvent.setName(wEvent.getName());
         dEvent.setAction(wEvent.getAction());
         dEvent.setListeners(wEvent.getListeners());

         if (wEvent.getComponent().getWindow().isRoot()
               && !wEvent.isHidden())
            dEvent.setInitial(true);
         else
            dEvent.setInitial(false);

         dEventList.getEvent().add(dEvent);
      }

      efg.setEvents(dEventList);

      // -----------------------------
      // Building graph
      // -----------------------------
      eventGraph = new ArrayList<List<String>>();
      //
      EventGraphType dEventGraph = factory.createEventGraphType();

      List<RowType> lRowList = new ArrayList<RowType>();
      String cache="";
      for (EventWrapper firstEvent : wEventList) {
         int indexFirst = wEventList.indexOf(firstEvent);

         RowType row = factory.createRowType();
         
         for (EventWrapper secondEvent : wEventList) {
            int indexSecond = wEventList.indexOf(secondEvent);

            int cellValue = firstEvent.isFollowedBy(secondEvent);
            String firstComponent="";
        	String secondComponent="";
        	
        	List<PropertyType> firstPropertyList=firstEvent.getComponent().getDComponentType().getAttributes().getProperty();
        	for(PropertyType property:firstPropertyList){
        		if(property.getName().equals("Title")){
        			firstComponent="\""+property.getValue().toString()+"\"";
        		}		
        	}
        	
        	List<PropertyType> secondPropertyList=secondEvent.getComponent().getDComponentType().getAttributes().getProperty();
        	for(PropertyType property:secondPropertyList){
        		if(property.getName().equals("Title")){
        			secondComponent="\""+property.getValue().toString()+"\"";
        		}		
        	}
        	String result=(firstComponent+"->"+secondComponent).replace("[", "").replace("]", "");
            if(cellValue==1&&(cache.indexOf(result)==-1)){
            	cache=cache+result;
            	System.out.println(result);
            }else if(cellValue==2&&(cache.indexOf(result)==-1)){
            	cache=cache+result+"[color=\"red\"]";
            	System.out.println(result+"[color=\"red\"]");
            }

            row.getE().add(indexSecond, cellValue);
         }

         lRowList.add(indexFirst, row);
      }
      dEventGraph.setRow(lRowList);
      efg.setEventGraph(dEventGraph);

      return efg;
   }

   public StringBuffer printGraphviz() {

      StringBuffer result = new StringBuffer();
      result.append("{\n");
      // result.append("/* NODES */");
      // result.append("\n");
      // for (EventWrapper event : wEventList) {
      //
      // result.append("\t\""
      // + event.getComponent().getFirstValueByName(
      // GUITARConstants.TITLE_TAG_NAME) + "\"");
      // result.append("\n");
      // }

      List<RowType> lRow = efg.getEventGraph().getRow();
      result.append("\n");
      result.append("/* EDGES */");
      result.append("\n");
      for (int row = 0; row < lRow.size(); row++) {
         List<Integer> rowVals = lRow.get(row).getE();

         for (int col = 0; col < rowVals.size(); col++) {

            int cell = rowVals.get(col);
            if (cell > 0) {
               EventWrapper firstEvent = wEventList.get((row));
               EventWrapper secondEvent = wEventList.get(col);
               String sFirstTitle = firstEvent.getID();

               // =firstEvent.getComponent().getFirstValueByName(GUITARConstants.TITLE_TAG_NAME);
               String sSecondTitle = secondEvent.getID();
               // =secondEvent.getComponent().getFirstValueByName(GUITARConstants.TITLE_TAG_NAME);

               result.append("\t" + "\"" + sFirstTitle + "\"" + "->"
                     + "\"" + sSecondTitle + "\"");
               result.append("\t/*" + cell + "*/");

               result.append("\n");

            }

         }

      }
      result.append("}\n");
      return (result);
   }

   /**
    * @param firstEvent
    * @param secondEvent
    */
   private void printGraphvizEdge(EventWrapper firstEvent,
         EventWrapper secondEvent, int cellValue) {
      if (cellValue > 0) {
         String sSourceTitle = firstEvent.getComponent()
               .getFirstValueByName(GUITARConstants.TITLE_TAG_NAME);
         String sTargetTitle = secondEvent.getComponent()
               .getFirstValueByName(GUITARConstants.TITLE_TAG_NAME);

         System.out.println("\"" + sSourceTitle + "\"->\"" + sTargetTitle
               + "\"");

      }
   }

   /**
    * @param wEvent
    * @return
    */
   private String getIndexFromWidget(EventWrapper wEvent) {
      // TODO Auto-generated method stub

      String index = wEvent.getComponent().getFirstValueByName(
            GUITARConstants.ID_TAG_NAME);
      index = index.substring(1);

      return index;

   }

   /**
    * Get the event list contained in a component
    * 
    * @param component
    * @return
    */
   private void readEventList(ComponentTypeWrapper component) {

      List<String> sActionList = component
            .getValueListByName(GUITARConstants.EVENT_TAG_NAME);

      if (sActionList != null)
         for (String action : sActionList) {
            EventWrapper wEvent = new EventWrapper();

            // Calculate event ID
            String sWidgetID = component
                  .getFirstValueByName(GUITARConstants.ID_TAG_NAME);

            sWidgetID = sWidgetID.substring(1);

            String sEventID = EVENT_ID_PREFIX + sWidgetID;

            String posFix = (sActionList.size() <= 1) ? ""
                  : EVENT_ID_SPLITTER
                        + Integer.toString(sActionList.indexOf(action));
            sEventID = sEventID + posFix;

            wEvent.setID(sEventID);
            wEvent.setAction(action);
            wEvent.setComponent(component);
            wEvent.setListeners(component
                  .getValueListByName("ActionListeners"));
            wEventList.add(wEvent);
         }

      List<ComponentTypeWrapper> wChildren = component.getChildren();
      if (wChildren != null)
         for (ComponentTypeWrapper wChild : wChildren) {
            readEventList(wChild);
         }
   }

}
