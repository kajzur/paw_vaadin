package com.paw.trelloplus.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.sql.Statement;
import java.sql.Connection;
import com.paw.trelloplus.components.Organization;
import com.paw.trelloplus.views.LoginView;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

public class OrganizationService extends AbstractService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6004194076570050164L;
	private SQLContainer organizationContainer;
	private final static Logger logger = Logger
			.getLogger(OrganizationService.class.getName());

	public OrganizationService() {
		super();
	}

	@Override
	protected void initContainers() {

		try {
			TableQuery q1 = new TableQuery("organizations", connectionPool);
			q1.setVersionColumn("VERSION");
			organizationContainer = new SQLContainer(q1);

		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public Organization addNewOrganization(String name)
			throws UnsupportedOperationException, SQLException {
		Object rowId = organizationContainer.addItem();
		Item rowItem = organizationContainer.getItem(rowId);
		rowItem.getItemProperty("name").setValue(name);
		organizationContainer.commit();

		return null;
	}

	public ArrayList<Organization> getAllOrganizations() {
		ArrayList<Organization> organizations = new ArrayList<Organization>();

		for (int i = 0; i < organizationContainer.size(); i++) {

			Object id = organizationContainer.getIdByIndex(i);
			Item item = organizationContainer.getItem(id);
			Property organization_id = item.getItemProperty("id");
			Property name = item.getItemProperty("name");

			Organization organization = new Organization(organization_id
					.getValue().toString(), name.getValue().toString());
			organizations.add(organization);
		}
		return organizations;
	}

	public void addOrganizationByUser(Organization organization, String id)
			throws SQLException {
		Connection conn = connectionPool.reserveConnection();
		Statement statement = conn.createStatement();
		statement.executeUpdate("INSERT INTO users_organizations values(" + id
				+ ", " + organization.getId() + ")");
		statement.close();
		conn.commit();
	}

	public void deleteOrganizationByUser(String id) throws SQLException {
		Connection conn = connectionPool.reserveConnection();
		Statement statement = conn.createStatement();
		statement
				.executeUpdate("DELETE FROM users_organizations WHERE id_user ="
						+ id);
		statement.close();
		conn.commit();
	}

	public void addOrganizationByBoard(Organization organization, String id)
			throws SQLException {
		Connection conn = connectionPool.reserveConnection();
		Statement statement = conn.createStatement();
		statement.executeUpdate("INSERT INTO boards_organizations values(" + id
				+ ", " + organization.getId() + ")");
		statement.close();
		conn.commit();
	}

	public void deleteOrganizationByBoard(String id) throws SQLException {
		Connection conn = connectionPool.reserveConnection();
		Statement statement = conn.createStatement();
		statement
				.executeUpdate("DELETE FROM boards_organizations WHERE id_board ="
						+ id);
		statement.close();
		conn.commit();
	}

	public ArrayList<Organization> getOrganizationByBoard(String board_id)
			throws SQLException {
		FreeformQuery q2 = new FreeformQuery(
				"SELECT o.id, o.name from boards_organizations bo join organizations o on bo.id_organization = o.id WHERE id_board = "
						+ board_id, connectionPool);
		SQLContainer organizationByBoardContainer = new SQLContainer(q2);

		ArrayList<Organization> organizations = new ArrayList<Organization>();

		for (int i = 0; i < organizationByBoardContainer.size(); i++) {

			Object id = organizationByBoardContainer.getIdByIndex(i);
			Item item = organizationByBoardContainer.getItem(id);
			Property oraganization_id = item.getItemProperty("id");
			Property name = item.getItemProperty("name");
			Organization organization = new Organization(oraganization_id
					.getValue().toString(), name.getValue().toString());

			organizations.add(organization);
		}

		return organizations;
	}

}
