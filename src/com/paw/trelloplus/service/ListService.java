package com.paw.trelloplus.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.paw.trelloplus.components.List;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.navigator.View;

public class ListService extends AbstractService  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7234308085879341017L;
	
	private final static Logger logger = Logger.getLogger(ListService.class.getName());
	
	public ListService() {
		super();
	
	}
	
	public List addList(int idBoard, String title, int idUser)throws UnsupportedOperationException, SQLException {
		
	    String sql ="INSERT INTO lists values(NUll, ?, ?, 0)";
	    PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	    statement.setInt(1, idBoard);
	    statement.setString(2, title);
	    statement.executeUpdate();
	    ResultSet rs = statement.getGeneratedKeys();
		int id = 0;
		if (rs.next()){
		    id =rs.getInt(1);
		}
	    statement.close();
	    connection.commit(); 
	    List list = new List(id+"", idBoard+"", title);
	    
	    return list;
	}

	public ArrayList<List> getAllList() throws SQLException {
		ArrayList<List> lists = new ArrayList<List>();
		String sql = "SELECT * from lists where deleted = 0";
		PreparedStatement statement =  connection.prepareStatement(sql);
		ResultSet rs = statement.executeQuery();
		while(rs.next()) 
		{
			lists.add(new List(rs.getString("id_list"), rs.getString("id_board"),rs.getString("name")));
		}
		return lists;
	}
	
	public void setDeleted(String idList) throws SQLException
	{
		String sql = "UPDATE lists SET deleted = 1 WHERE id_list = ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, idList);
		statement.executeUpdate();
		connection.commit(); 
		
		sql = "UPDATE tasks SET deleted = 1 WHERE id_list = ?";
		statement = connection.prepareStatement(sql);
		statement.setString(1, idList);
		statement.executeUpdate();
		connection.commit(); 	
	}

}
