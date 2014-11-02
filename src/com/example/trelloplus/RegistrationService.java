package com.example.trelloplus;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import com.vaadin.data.Item;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

public class RegistrationService extends AbstractService {

	private SQLContainer usersContainer;

	public void addUser(String name, String password)
			throws UnsupportedOperationException, SQLException,
			ReadOnlyException, NoSuchAlgorithmException {

		Object rowId = usersContainer.addItem();
		Item rowItem = usersContainer.getItem(rowId);
		rowItem.getItemProperty("login").setValue(name);
		rowItem.getItemProperty("password").setValue(getHashedPassword(password));

		usersContainer.commit();

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
