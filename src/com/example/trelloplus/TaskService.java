package com.example.trelloplus;

import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

public class TaskService extends AbstractService {

	private SQLContainer tasksContainer;

	
	public TaskService() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Task addTask(String id, String title, String description)
			throws UnsupportedOperationException, SQLException {

		Object rowId = tasksContainer.addItem();
		Item rowItem = tasksContainer.getItem(rowId);
		rowItem.getItemProperty("id_list").setValue(Integer.parseInt(id));
		rowItem.getItemProperty("name").setValue(title);
		rowItem.getItemProperty("description").setValue(description);

		tasksContainer.commit();
		Task t = new Task(title, description);
		t.setId_list(id);
		return t;
	}

	public ArrayList<Task> getAllTask() {

		ArrayList<Task> listAllTasks = new ArrayList<>();

		for (int i = 0; i < tasksContainer.size(); i++) {
			Object id = tasksContainer.getIdByIndex(i);
			Item item = tasksContainer.getItem(id);
			Property id_list = item.getItemProperty("id_list");
			Property name = item.getItemProperty("name");
			Property desc = item.getItemProperty("description");
			Task task = new Task((String) name.getValue(),
					(String) desc.getValue());

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
