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
	
	
	private SQLContainer boardContainer, boardsByUserContainer, markedBoardByIdContainer;
	private final static Logger logger = Logger.getLogger(BoardService.class.getName());

	@Override
	public void initContainers()
	{
		
		try {
			TableQuery q1 = new TableQuery("boards", connectionPool);
			q1.setVersionColumn("VERSION");
			boardContainer = new SQLContainer(q1);
			FreeformQuery q2 = new FreeformQuery("SELECT b.id, b.name from boards_users bu join boards b on bu.id_board = b.id WHERE id_user = 11", connectionPool);
			boardsByUserContainer = new SQLContainer(q2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
		//ca³y czas wyœwietla te same wartoœci, jak baza siê zmieni i to wywo³am nadal daje te pocz¹tkowe
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
//		ArrayList<Board> boards = new ArrayList<Board>();
//		
//		FreeformQuery q2 = new FreeformQuery("SELECT * FROM boards ORDER BY marked DESC", connectionPool);
//		boardsByUserContainer = new SQLContainer(q2);
//		
//		for (int i = 0; i < boardsByUserContainer.size(); i++) {
//			Object id = boardsByUserContainer.getIdByIndex(i);
//			Item item = boardsByUserContainer.getItem(id);
//			Property id_board = item.getItemProperty("id");
//			Property name = item.getItemProperty("name");
//			Property marked = item.getItemProperty("marked");
//			Board board = new Board(id_board.getValue().toString(),name.getValue().toString(),(int) marked.getValue());
//			board.setName((String) name.getValue()); //?
//			board.setBoardId(id_board.getValue().toString());
//			board.setMarked(Integer.parseInt(marked.getValue().toString()));
//			boards.add(board);
//		}
//		
//		return boards;
	}
	
	public String getMarkedBoardById(String idBoard) throws SQLException
	{
		FreeformQuery q2 = new FreeformQuery("select marked from boards where id = " + idBoard, connectionPool);
		markedBoardByIdContainer = new SQLContainer(q2);
		
		Object id = markedBoardByIdContainer.getIdByIndex(0);
		Item item = markedBoardByIdContainer.getItem(id);
		Property marked = item.getItemProperty("marked");
		
		return marked.getValue().toString();
	}
	
	public void editMarkedBoard(String idBoard, String marked) throws SQLException
	{
		Connection conn = connectionPool.reserveConnection();
		Statement statement = conn.createStatement();
		statement.executeUpdate("update boards set marked = '" + marked + "' where id = '" + idBoard + "'");
		statement.close();
		conn.commit();
	}

}
