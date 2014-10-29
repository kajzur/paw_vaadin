package com.example.trelloplus;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

public class Service implements Serializable {
	private JDBCConnectionPool connectionPool = null;
	SQLContainer tasksContainer;
	SQLContainer usersContainer;
	SQLContainer listsContainer;
	public static ArrayList<List> listCache = new ArrayList<List>();

	public Service() {
		initConnectionPool();
		initContainers();
	}

	private void initConnectionPool() {
		try {
			connectionPool = new SimpleJDBCConnectionPool(
					"com.mysql.jdbc.Driver",
					"jdbc:mysql://localhost/trello_plus?useUnicode=true&characterEncoding=UTF-8",
					"root", "");
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

			TableQuery q3 = new TableQuery("lists", connectionPool);
			q3.setVersionColumn("VERSION");
			listsContainer = new SQLContainer(q3);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addTask(String id, String title, String description)
			throws UnsupportedOperationException, SQLException {

		Object rowId = tasksContainer.addItem();
		Item rowItem = tasksContainer.getItem(rowId);
		rowItem.getItemProperty("id_list").setValue(Integer.parseInt(id));
		rowItem.getItemProperty("name").setValue(title);
		rowItem.getItemProperty("description").setValue(description);

		tasksContainer.commit();
	}

	public void addList(int id, String title)
			throws UnsupportedOperationException, SQLException {
		Object rowId = listsContainer.addItem();
		Item rowItem = listsContainer.getItem(rowId);
		rowItem.getItemProperty("id_board").setValue(id);
		rowItem.getItemProperty("name").setValue(title);

		listsContainer.commit();
	}

	public ArrayList<Task> fillTable() {

		ArrayList<Task> listAllTasks = new ArrayList<>();

		for (int i = 0; i < tasksContainer.size(); i++) {

			Object id = tasksContainer.getIdByIndex(i);
			Item item = tasksContainer.getItem(id);
			Property id_list = item.getItemProperty("id_list");
			Property name = item.getItemProperty("name");
			Property desc = item.getItemProperty("description");

			Task t = new Task((String) name.getValue(),
					(String) desc.getValue());

			t.setId_list(id_list.getValue() + "");
			t.setTitle(name.getValue() + "");
			t.setDesc(desc.getValue() + "");

			listAllTasks.add(t);
		}

		return listAllTasks;

	}

	public ArrayList<List> fillTableList(boolean refresh) {
		if(listCache.size()>0 && false)
			return listCache;
		ArrayList<List> lists = new ArrayList<List>();
		for (int i = 0; i < listsContainer.size(); i++) {
			Object id = listsContainer.getIdByIndex(i);
			Item item = listsContainer.getItem(id);
			Property id_list = item.getItemProperty("id_list");
			Property id_board = item.getItemProperty("id_board");
			Property name = item.getItemProperty("name");
			List list = new List((String) name.getValue());

			list.setId_list(id_list.getValue() + "");
			list.setId_board(id_board.getValue() + "");
			list.setName(name.getValue() + "");

			lists.add(list);
		}
		listCache.addAll(lists);
		return lists;
	}

	public boolean checkUserCredentials(String user, String password) {

		password = getHashedPassword(password);

		usersContainer.addContainerFilter(new Compare.Equal("login", user));

		Object id = usersContainer.getIdByIndex(0);
		Item item = usersContainer.getItem(id);
		Property passwordFromDB = item.getItemProperty("password");

		if (password.equals(passwordFromDB.getValue()))
			return true;
		else
			return false;
	}

	private String getHashedPassword(String password) {
		return password;
	}

}