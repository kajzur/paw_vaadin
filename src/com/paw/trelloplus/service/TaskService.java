package com.paw.trelloplus.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.paw.trelloplus.components.Task;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

public class TaskService extends AbstractService {

	private SQLContainer tasksContainer;

	
	public TaskService() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public boolean moveTask(String taskId, int newListId) throws UnsupportedOperationException, SQLException {
		tasksContainer.removeAllContainerFilters();
		tasksContainer.addContainerFilter(new Compare.Equal("id", taskId));
		if (!(tasksContainer.size() > 0))
			return false;		
		Object object = tasksContainer.getIdByIndex(0);
				
		Item item = tasksContainer.getItem(object);
		item.getItemProperty("id_list").setValue(newListId);
		tasksContainer.commit();
		return true;
	}
	public Task addTask(String listId, String title, String description)
			throws UnsupportedOperationException, SQLException {

		Object rowId = tasksContainer.addItem();
		Item rowItem = tasksContainer.getItem(rowId);
		rowItem.getItemProperty("id_list").setValue(Integer.parseInt(listId));	
		rowItem.getItemProperty("name").setValue(title);
		rowItem.getItemProperty("description").setValue(description);
		tasksContainer.commit();
		
		Connection conn = connectionPool.reserveConnection();
        Statement statement = conn.createStatement();
        statement.executeUpdate("INSERT INTO tasks_users values("+(((RowId) tasksContainer.lastItemId()).toString())+", "+"11"+")");
        statement.close();
        conn.commit(); 
        
        
		String newId = (String)(((RowId)tasksContainer.lastItemId()).getId()[0]+"");
		Task t = new Task(title, description);
		t.setId_list(listId);
		t.setTask_id(newId);
		return t;
	}

	public ArrayList<Task> getAllTask() {

		ArrayList<Task> listAllTasks = new ArrayList<>();

		for (int i = 0; i < tasksContainer.size(); i++) {
			Object id = tasksContainer.getIdByIndex(i);
			Item item = tasksContainer.getItem(id);
			Property task_id = item.getItemProperty("id");
			Property id_list = item.getItemProperty("id_list");
			Property name = item.getItemProperty("name");
			Property desc = item.getItemProperty("description");
			Task task = new Task((String) name.getValue(),
					(String) desc.getValue());
			task.setTask_id(task_id.getValue()+"");
			task.setId_list(id_list.getValue() + "");
			task.setTitle(name.getValue() + "");
			task.setDesc(desc.getValue() + "");

			listAllTasks.add(task);
		}
		return listAllTasks;

	}

	@Override
	protected void initContainers() {
		try {
			TableQuery q1 = new TableQuery("tasks", connectionPool);
			q1.setVersionColumn("VERSION");
			tasksContainer = new SQLContainer(q1);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
