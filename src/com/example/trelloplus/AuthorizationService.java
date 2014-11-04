package com.example.trelloplus;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

public class AuthorizationService extends AbstractService {

	private SQLContainer usersContainer;

	public AuthorizationService() {
		super();

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
