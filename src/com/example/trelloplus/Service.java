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
import com.vaadin.ui.GridLayout;

public class Service implements Serializable {
	private JDBCConnectionPool connectionPool = null;
	SQLContainer tasksContainer;
	SQLContainer usersContainer;
	SQLContainer listsContainer;

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
		rowItem.getItemProperty("id_list").setValue(Integer.parseInt(id)- 1);
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

		// rowItem.getItemProperty("name").setValue(listsContainer.getItemIds()
		// + "");

		listsContainer.commit();
	}

	public ArrayList<Task> fillTable(/* GridLayout gl */) {

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
			
			// gl.addComponent(t);
			listAllTasks.add(t);
		}

		return listAllTasks;

	}

	public void cleanTableList() {

	}

	public ArrayList<List> fillTableList() {
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
			
			// gridLayout.addComponent(board);
			lists.add(list);
		}

		return lists;
		/*
		 * int widthPerList;
		 * 
		 * if (lists.size() < 1) { widthPerList = 100 / 1;// lists.size(); }
		 * 
		 * else { widthPerList = 100 / lists.size(); }
		 * 
		 * for (List b : lists) { // b.setWidth(widthPerList + "%");
		 * 
		 * 
		 * // b.setWidth("30%"); b.setw
		 * 
		 * 
		 * Button addNewListBtn = new Button("Dodaj zadanie");
		 * addNewListBtn.addClickListener(new Button.ClickListener() {
		 * 
		 * @Override public void buttonClick(ClickEvent event) {
		 * createNewListWin.center(); getUI().addWindow(createNewListWin);
		 * 
		 * } });
		 * 
		 * //gridLayout.addComponent(b);
		 * 
		 * VerticalLayout buttonMainGroupLayout = new VerticalLayout();
		 * buttonMainGroupLayout.setStyleName("list");
		 * buttonMainGroupLayout.setSizeFull();
		 * buttonMainGroupLayout.setSpacing(true);
		 * 
		 * buttonMainGroupLayout.addComponent(b);
		 * buttonMainGroupLayout.addComponent(addNewListBtn);
		 * gridLayout.setStyleName("list");
		 * gridLayout.addComponent(buttonMainGroupLayout);
		 * //gridLayout.setExpandRatio(b, 1f);
		 * 
		 * gridLayout.setExpandRatio(buttonMainGroupLayout, 1f);
		 * 
		 * }
		 */
	}

	public boolean checkUserCredentials(String user, String password) {

		password = getHashedPassword(password);

		usersContainer.addContainerFilter(new Compare.Equal("login", user));

		// if(usersContainer.size()==0){
		// return false;
		// }

		Object id = usersContainer.getIdByIndex(0);
		Item item = usersContainer.getItem(id);
		Property passwordFromDB = item.getItemProperty("password");

		if (password.equals(passwordFromDB.getValue()))
			return true;
		else
			return false;

	}
	
	/*public String CompareTasksToList(String id_l)
	{
		tasksContainer.addContainerFilter(new Compare.Equal("id_list", id_l));
		
		Object id_list = tasksContainer.getIdByIndex(1);
		Item item = tasksContainer.getItem(id_list);
		
		Property p_name = item.getItemProperty("name");
//		Property p_descritpion = item.getItemProperty("description");
		
		return p_name.getValue() + "";
	}*/

	private String getHashedPassword(String password) {
		return password;
	}

}