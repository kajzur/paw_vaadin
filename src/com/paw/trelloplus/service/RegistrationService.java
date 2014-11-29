package com.paw.trelloplus.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.vaadin.data.Item;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

public class RegistrationService extends AbstractService {

	private SQLContainer usersContainer;

	public RegistrationService() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void addUser(String name, String password) throws UnsupportedOperationException, SQLException, ReadOnlyException, NoSuchAlgorithmException {
		
		String sql = "INSERT INTO users values(NULL, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, name);
		statement.setString(2, password);
	    statement.executeUpdate();
	    statement.close();
	    connection.commit(); 

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
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}

	}

}
