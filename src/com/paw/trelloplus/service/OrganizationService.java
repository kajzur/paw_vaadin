package com.paw.trelloplus.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;

import com.paw.trelloplus.components.List;
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
//	private SQLContainer organizationContainer;
	private final static Logger logger = Logger
			.getLogger(OrganizationService.class.getName());

	public OrganizationService() {
		super();
	}

	@Override
	protected void initContainers() {

//		try {
//			TableQuery q1 = new TableQuery("organizations", connectionPool);
//			q1.setVersionColumn("VERSION");
//			organizationContainer = new SQLContainer(q1);
//
//		} catch (SQLException e) {
//
//			e.printStackTrace();
//		}

	}

	public void addNewOrganization(String name) throws UnsupportedOperationException, SQLException {
	
		 String sql ="INSERT INTO organizations values(NUll, ?)";
		 PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		 statement.setString(1, name);
		 statement.executeUpdate();
		 statement.close();
		 connection.commit();	   
	}

	public ArrayList<Organization> getAllOrganizations() throws SQLException {
		ArrayList<Organization> organizations = new ArrayList<Organization>();
		String sql = "SELECT * from organizations";
		PreparedStatement statement =  connection.prepareStatement(sql);
		ResultSet rs = statement.executeQuery();
		while(rs.next()) 
		{
			organizations.add(new Organization(rs.getString("id"),rs.getString("name")));
		}
		return organizations;
	}

	public void addOrganizationByUser(Organization organization, String id) throws SQLException {
		 String sql ="INSERT INTO users_organizations values(?, ?)";
		 PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		 statement.setString(1, id);
		 statement.setString(2, organization.getId());
		 statement.executeUpdate();
		 statement.close();
		 connection.commit();
	}

	public void deleteOrganizationByUser(String id) throws SQLException {
		 String sql ="DELETE FROM users_organizations WHERE id_user =?";
		 PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		 statement.setString(1, id);
		 statement.executeUpdate();
		 statement.close();
		 connection.commit();
	}

	public void addOrganizationByBoard(Organization organization, String id)
			throws SQLException {
		String sql ="INSERT INTO boards_organizations values(?, ?)";
		 PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		 statement.setString(1, id);
		 statement.setString(2, organization.getId());
		 statement.executeUpdate();
		 statement.close();
		 connection.commit();
	}

	public void deleteOrganizationByBoard(String id) throws SQLException {

		String sql ="DELETE FROM users_organizations WHERE id_user =?";
		 PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		 statement.setString(1, id);
		 statement.executeUpdate();
		 statement.close();
		 connection.commit();
	}

	public ArrayList<Organization> getOrganizationByBoard(String board_id)
			throws SQLException {

		ArrayList<Organization> organizations = new ArrayList<Organization>();
		String sql = "SELECT o.id, o.name from boards_organizations bo join organizations o on bo.id_organization = o.id WHERE id_board = ?";
		PreparedStatement statement =  connection.prepareStatement(sql);
		statement.setString(1, board_id);
		ResultSet rs = statement.executeQuery();
		while(rs.next()) 
		{
			organizations.add(new Organization(rs.getString("id"),rs.getString("name")));
		}
		return organizations;
	}

}
