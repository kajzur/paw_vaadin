package com.paw.trelloplus.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.paw.trelloplus.components.Board;
import com.paw.trelloplus.models.User;
import com.paw.trelloplus.views.LoginView;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

public class BoardService extends AbstractService {
	
	private final static Logger logger = Logger.getLogger(BoardService.class.getName());
	
	public Board addNew(String name) throws UnsupportedOperationException, SQLException {
		
		String sql = "INSERT INTO boards values(NULL, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		statement.setString(1, name);
		statement.setInt(2, 0);
		statement.executeUpdate();
		connection.commit();
		ResultSet rs = statement.getGeneratedKeys();
		int id = 0;
		if (rs.next()){
		    id =rs.getInt(1);
		}
		sql = " INSERT INTO boards_users values(?, 11)";
		statement = connection.prepareStatement(sql);
		statement.setInt(1, id);
	    statement.close();
	    connection.commit();           
		
		Board board = new Board(id+"", name, 0);	
		return board;
	}
	
	public ArrayList<Board> getAllBoard() throws SQLException 
	{
		ArrayList<Board> boards = new ArrayList<Board>();
		
		String sql = "SELECT * FROM boards ORDER BY marked DESC";
		PreparedStatement statement = connection.prepareStatement(sql);
		ResultSet rs = statement.executeQuery();
		while (rs.next()) 
		{
			logger.log(Level.SEVERE, ""+ rs.getInt("marked"));
			String id = rs.getString("id");
			String name = rs.getString("name");
			int marked = rs.getInt("marked");
			boards.add(new Board(id, name, marked));
		}
		statement.close();
		rs.close();
		return boards;
	}
	
	public String getMarkedBoardById(String idBoard) throws SQLException
	{
		String sql = "select marked from boards where id =?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, idBoard);
		ResultSet rs = statement.executeQuery();
		if(rs.next())
		{
			return rs.getString("marked");
		}
		return null;
	}
	
	public void editMarkedBoard(String idBoard, String marked) throws SQLException
	{
		Statement statement = connection.createStatement();
		statement.executeUpdate("update boards set marked = '" + marked + "' where id = '" + idBoard + "'");
		statement.close();
		connection.commit();
	}

}
