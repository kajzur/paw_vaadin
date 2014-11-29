package com.paw.trelloplus.utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import com.paw.trelloplus.components.List;
import com.paw.trelloplus.components.Task;
import com.paw.trelloplus.service.TaskService;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.Not;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.Notification;

import fi.jasoft.dragdroplayouts.DDVerticalLayout;
import fi.jasoft.dragdroplayouts.DDVerticalLayout.VerticalLayoutTargetDetails;
import fi.jasoft.dragdroplayouts.events.LayoutBoundTransferable;
import fi.jasoft.dragdroplayouts.events.VerticalLocationIs;

public class ListDropHandler implements DropHandler{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1751473658138526600L;
	private TaskService taskService = new TaskService();
	private ArrayList<List> allLists;
	public ListDropHandler(ArrayList<List> all) {
		this.allLists=all;
	}
	
	@Override
	public AcceptCriterion getAcceptCriterion() {
		return  new Not(VerticalLocationIs.MIDDLE);
	}
	
	@Override
	public void drop(DragAndDropEvent event) {
//		DragAndDropWrapper dd = (DragAndDropWrapper)event.getTransferable().getSourceComponent();
//		Task t= (Task) dd.getData();
//		DragAndDropWrapper dd2 = (DragAndDropWrapper)event.getTargetDetails();
//		.getTarget();
//		List targetList = (List)dd2.getData();
//		String currentDraggedTaskId = t.getTask_id();
		LayoutBoundTransferable transferable = (LayoutBoundTransferable) event
                .getTransferable();
        VerticalLayoutTargetDetails details = (VerticalLayoutTargetDetails) event
                .getTargetDetails();
        List targetList = (List)((DDVerticalLayout)details.getTarget()).getParent();
        Task t = (Task)transferable.getComponent();
		
        List layout = targetList;
        
        int currentIndex = layout.getTaskContainer().getComponentIndex(t);
        int newIndex = details.getOverIndex();
        if(newIndex == -1 && details.getDropLocation() != VerticalDropLocation.BOTTOM) return;
        Logger.getGlobal().log(Level.SEVERE, "new index: "+newIndex);
		Logger.getGlobal().log(Level.SEVERE, "cureent Index: "+currentIndex);
        layout.removeComponent(t);

        if (currentIndex > newIndex
                && details.getDropLocation() == VerticalDropLocation.BOTTOM && newIndex==-1) {
            newIndex++;
        }
        
        
		final int targetListId = Integer.parseInt( targetList.getId_list() );
		try {
			
			List targetListObj = (List)CollectionUtils.find(allLists, new Predicate() {
				@Override
				public boolean evaluate(Object object) {
					List l = (List)(object);
					return l.getId_list().equals(targetListId+"");
				}
			});
			List oldList = (List)t.getParent().getParent();
			if(oldList.equals(targetListObj))
			{
				oldList.removeComponent(t);
				targetListObj.getTaskContainer().addComponent(t, newIndex);
				Logger.getGlobal().log(Level.SEVERE, "wstawiam pod: "+newIndex);
				taskService.moveTask(t.getTask_id(), targetListId, t.getParent());
			}else{
				oldList.removeComponent(t);
				targetListObj.getTaskContainer().addComponent(t);
				taskService.moveTaskToOtherList(t.getTask_id(), targetListId);
			}
			
		} catch (UnsupportedOperationException | SQLException e) {
			Notification.show("err");
		}
		
	}

}
