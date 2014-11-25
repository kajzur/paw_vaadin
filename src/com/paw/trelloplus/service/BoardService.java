package com.paw.trelloplus.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.paw.trelloplus.components.Board;
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
			logger.log(Level.SEVERE, LoginView.ID_USER);
			FreeformQuery q2 = new FreeformQuery("SELECT b.id, b.name from boards_users bu join boards b on bu.id_board = b.id WHERE id_user = 11", connectionPool);
			boardsByUserContainer = new SQLContainer(q2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Board addNew(String name)
			throws UnsupportedOperationException, SQLException {
		
		Object rowId = boardContainer.addItem();
		Item rowItem = boardContainer.getItem(rowId);
		rowItem.getItemProperty("name").setValue(name);
		rowItem.getItemProperty("marked").setValue(0);
		boardContainer.commit();
		
		Connection conn = connectionPool.reserveConnection();
        Statement statement = conn.createStatement();
        statement.executeUpdate("INSERT INTO boards_users values("+(((RowId) boardContainer.lastItemId()).toString())+", 11)");
        statement.close();
        conn.commit();                   
		
		Board board = new Board(name);	
		return board;
	}
	
	public ArrayList<Board> getAllBoard() throws SQLException {
		ArrayList<Board> boards = new ArrayList<Board>();
		FreeformQuery q2 = new FreeformQuery("SELECT * FROM boards ORDER BY marked DESC", connectionPool);
		boardsByUserContainer = new SQLContainer(q2);
		
		for (int i = 0; i < boardsByUserContainer.size(); i++) {
			Object id = boardsByUserContainer.getIdByIndex(i);
			Item item = boardsByUserContainer.getItem(id);
			Property id_board = item.getItemProperty("id");
			Property name = item.getItemProperty("name");
			Property marked = item.getItemProperty("marked");
			Board board = new Board((String) name.getValue());
			board.setName((String) name.getValue()); //?
			board.setBoardId(id_board.getValue().toString());
			board.setMarked(Integer.parseInt(marked.getValue().toString()));
			boards.add(board);
		}
		
		return boards;
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
