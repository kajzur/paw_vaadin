package com.paw.trelloplus.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.paw.trelloplus.models.User;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.sqlcontainer.SQLContainer;


public class AuthorizationService extends AbstractService {

	/**
	 * 
	 */

//	private SQLContainer usersContainer;
	private final static Logger logger = Logger.getLogger(AuthorizationService.class.getName());

	public AuthorizationService() {
		super();}
	
	public void addUserByTask(User user, String id) throws SQLException
	{
        Statement statement = connection.createStatement();
        statement.executeUpdate("INSERT INTO tasks_users values("+id+", "+user.getId()+")");
        statement.close();
        connection.commit(); 
	}
	
	public void deleteUserByTask(String id) throws SQLException
	{
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM tasks_users WHERE id_task ="+id);
        statement.close();
        connection.commit(); 
	}
	
	public ArrayList<User> getAllUsers() throws SQLException
	{
		ArrayList<User> users = new ArrayList<User>();
		
		String sql = "SELECT * from users";
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sql);
		while (rs.next()) 
		{
			String id = rs.getString("id");
			String name = rs.getString("login");
			users.add(new User(id, name));
		}
		return users;
	}
	
	public ArrayList<User> getUsersByTask(String task_id) throws SQLException
	{
		ArrayList<User> users = new ArrayList<User>();
		String sql = "SELECT u.id, u.login from tasks_users tu join users u on tu.id_user = u.id WHERE id_task = "+task_id;
		Statement statement =  connection.createStatement();
		ResultSet rs = statement.executeQuery(sql);
		while (rs.next()) 
		{
			String id = rs.getString("id");
			String name = rs.getString("login");
			users.add(new User(id, name));
		}
		return users;
	}
	
	public String getUserId(String username) throws SQLException{
		String sql = "SELECT id from users WHERE login = ?";
		PreparedStatement statement =  connection.prepareStatement(sql);
		statement.setString(1, username);
		ResultSet rs = statement.executeQuery();
		if(rs.next()) 
		{
			return rs.getString("id");
		}
		return null;
	}

	public boolean checkUserCredentials(String user, String password) throws NoSuchAlgorithmException, SQLException {
		
		password = getHashedPassword(password);
		String sql = "SELECT password from users WHERE login = ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, user);
		ResultSet rs = statement.executeQuery();

		if (!rs.next())
			return false;


		if (password.equals(rs.getString("password")))
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
//		try {
//
//			TableQuery q2 = new TableQuery("users", connectionPool);
//			q2.setVersionColumn("VERSION");
//			usersContainer = new SQLContainer(q2);
//			
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}

	}

}
