package com.example.trelloplus;

import java.io.Serializable;
import java.sql.SQLException;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.ui.GridLayout;

public class Service implements Serializable{
	private JDBCConnectionPool connectionPool = null;
	SQLContainer tasksContainer;
	SQLContainer usersContainer;

	public Service() {
		initConnectionPool();
		initContainers();
	}

	private void initConnectionPool() {
		try {
			connectionPool = new SimpleJDBCConnectionPool(
					"com.mysql.jdbc.Driver",
					"jdbc:mysql://localhost/trello_plus?useUnicode=true&characterEncoding=UTF-8", "root", "");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void initContainers() {
		try {
			TableQuery q1 = new TableQuery("tasks", connectionPool);
			q1.setVersionColumn("VERSION");
			tasksContainer = new SQLContainer(q1);
			TableQuery q2 = new TableQuery("users", connectionPool);
			q2.setVersionColumn("VERSION");
			usersContainer = new SQLContainer(q2);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addTask(String title, String description) throws UnsupportedOperationException, SQLException {

		Object rowId = tasksContainer.addItem();
		Item rowItem = tasksContainer.getItem(rowId);
		rowItem.getItemProperty("name").setValue(title);
		rowItem.getItemProperty("description").setValue(description);

		tasksContainer.commit();

	}
	public void fillTable(GridLayout gl){
		for (int i = 0; i < tasksContainer.size(); i++) {
		    Object id = tasksContainer.getIdByIndex(i);
		    Item item = tasksContainer.getItem(id);
		    Property name = item.getItemProperty("name");
		    Property desc = item.getItemProperty("description");
		    Task t = new Task((String)name.getValue(), (String)desc.getValue());
		    gl.addComponent(t);
		   
		}
	}
	
	public boolean checkUserCredentials(String user, String password){
		
		password = getHashedPassword(password);
		
		usersContainer.addContainerFilter(new Compare.Equal("login", user));
		
	
		Object id = usersContainer.getIdByIndex(0);
		Item item = usersContainer.getItem(id);
		Property passwordFromDB = item.getItemProperty("password");
		
		if(password.equals(passwordFromDB.getValue()))
			return true;
		else
			return false;
		
	}
	
	private String getHashedPassword(String password)
	{
		return password;
	}

	
}