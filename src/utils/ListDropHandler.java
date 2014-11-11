package utils;

import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import com.paw.trelloplus.components.List;
import com.paw.trelloplus.components.Task;
import com.paw.trelloplus.service.TaskService;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.Notification;

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
		return AcceptAll.get();
	}
	
	@Override
	public void drop(DragAndDropEvent event) {
		DragAndDropWrapper dd = (DragAndDropWrapper)event.getTransferable().getSourceComponent();
		Task t= (Task) dd.getData();
		DragAndDropWrapper dd2 = (DragAndDropWrapper)event.getTargetDetails().getTarget();
		List targetList = (List)dd2.getData();
		String currentDraggedTaskId = t.getTask_id();
		final int targetListId = Integer.parseInt( targetList.getId_list() );
		try {
			taskService.moveTask(currentDraggedTaskId, targetListId);
			List targetListObj = (List)CollectionUtils.find(allLists, new Predicate() {
				@Override
				public boolean evaluate(Object object) {
					List l = (List)(object);
					return l.getId_list().equals(targetListId+"");
				}
			});
			List oldList = (List)t.getParent().getParent();
			oldList.removeComponent(t.getParent());
			targetListObj.addComponent(t.getParent());
			
		} catch (UnsupportedOperationException | SQLException e) {
			Notification.show("blad");
		}
		
	}

}
