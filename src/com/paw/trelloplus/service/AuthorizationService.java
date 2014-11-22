package com.paw.trelloplus.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.paw.trelloplus.components.List;
import com.paw.trelloplus.models.User;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

public class AuthorizationService extends AbstractService {

	/**
	 * 
	 */

	private SQLContainer usersContainer;
	private final static Logger logger = Logger.getLogger(AuthorizationService.class.getName());

	public AuthorizationService() {
		super();

	}
	
	public void addUserByTask(User user, String id) throws SQLException
	{
		Connection conn = connectionPool.reserveConnection();
        Statement statement = conn.createStatement();
        statement.executeUpdate("INSERT INTO tasks_users values("+id+", "+user.getId()+")");
        statement.close();
        conn.commit(); 
	}
	public void deleteUserByTask(String id) throws SQLException
	{
		Connection conn = connectionPool.reserveConnection();
        Statement statement = conn.createStatement();
        statement.executeUpdate("DELETE FROM tasks_users WHERE id_task ="+id);
        statement.close();
        conn.commit(); 
	}
	
	public ArrayList<User> getAllUsers()
	{
		ArrayList<User> users = new ArrayList<User>();
		for (int i = 0; i < usersContainer.size(); i++) {
		
			Object id = usersContainer.getIdByIndex(i);
			Item item = usersContainer.getItem(id);
			Property user_id = item.getItemProperty("id");
			Property name = item.getItemProperty("login");
			User user = new User(user_id.getValue().toString(), name.getValue().toString());

			users.add(user);
		}
		return users;
	}
	
	public ArrayList<User> getUsersByTask(String task_id) throws SQLException
	{
		FreeformQuery q2 = new FreeformQuery("SELECT u.id, u.login from tasks_users tu join users u on tu.id_user = u.id WHERE id_task = "+task_id, connectionPool);
		SQLContainer usersByTaskContainer = new SQLContainer(q2);
		
		ArrayList<User> users = new ArrayList<User>();
		for (int i = 0; i < usersByTaskContainer.size(); i++) {
		
			Object id = usersByTaskContainer.getIdByIndex(i);
			Item item = usersByTaskContainer.getItem(id);
			Property user_id = item.getItemProperty("id");
			Property name = item.getItemProperty("login");
			User user = new User(user_id.getValue().toString(), name.getValue().toString());

			users.add(user);
		}
		return users;
	}
	
	public String getUserId(String username){
		usersContainer.addContainerFilter(new Compare.Equal("login", username));
		if (!(usersContainer.size() > 0))
			return null;

		Object id = usersContainer.getIdByIndex(0);
		Item item = usersContainer.getItem(id);
		Property userId = item.getItemProperty("id");
		return userId.getValue().toString();
	}
	
	
	public boolean checkUserCredentials(String user, String password) {

		try {
			password = getHashedPassword(password);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		usersContainer.addContainerFilter(new Compare.Equal("login", user));

		if (!(usersContainer.size() > 0))
			return false;

		Object id = usersContainer.getIdByIndex(0);
		Item item = usersContainer.getItem(id);
		Property passwordFromDB = item.getItemProperty("password");

		if (password.equals(passwordFromDB.getValue()))
			return true;
		else
			return false;
	}

	private String getHashedPassword(String password)
			throws NoSuchAlgorithmException {

		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(password.getBytes());

		byte byteData[] = md.digest();

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
					.substring(1));
		}

		return sb.toString();
	}

	@Override
	protected void initContainers() {
		try {

			TableQuery q2 = new TableQuery("users", connectionPool);
			q2.setVersionColumn("VERSION");
			usersContainer = new SQLContainer(q2);
			

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
