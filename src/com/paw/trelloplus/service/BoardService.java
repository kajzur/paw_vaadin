package com.paw.trelloplus.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.paw.trelloplus.components.Board;
import com.paw.trelloplus.components.List;
import com.paw.trelloplus.views.LoginView;
import com.paw.trelloplus.views.TasksView;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.navigator.View;

public class BoardService extends AbstractService {
	
	
	private SQLContainer boardContainer;
	private SQLContainer boardsByUserContainer;
	private final static Logger logger = Logger.getLogger(BoardService.class.getName());
	
	
//	@Override
//	protected void initContainers() {
//		try {
//			TableQuery q1 = new TableQuery("boards", connectionPool);
//			q1.setVersionColumn("VERSION");
//			boardService = new SQLContainer(q1);
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//	}
	
	@Override
	protected void initContainers()
	{
		
		try {
			TableQuery q1 = new TableQuery("boards", connectionPool);
			q1.setVersionColumn("VERSION");
			boardContainer = new SQLContainer(q1);
			
			FreeformQuery q2 = new FreeformQuery("SELECT b.id, b.name from boards_users bu join boards b on bu.id_board = b.id WHERE id_user ="+TasksView.ID_USER, connectionPool);
			boardsByUserContainer = new SQLContainer(q2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	public Board addNew(String title, String user) throws UnsupportedOperationException, SQLException{
//
//		Object rowId = boardContainer.addItem();
//		Item rowItem = boardContainer.getItem(rowId);
//		rowItem.getItemProperty("name").setValue(title);
//		rowItem.getItemProperty("id_user").setValue(Integer.parseInt(user));
//
//		boardContainer.commit();
//		Board b = new Board(title);
//		return b;
//	}
	
	public Board addNew(String name)
			throws UnsupportedOperationException, SQLException {
		
		Object rowId = boardContainer.addItem();
		Item rowItem = boardContainer.getItem(rowId);
		rowItem.getItemProperty("name").setValue(name);
		boardContainer.commit();
		
		Connection conn = connectionPool.reserveConnection();
        Statement statement = conn.createStatement();
        statement.executeUpdate("INSERT INTO boards_users values("+(((RowId) boardContainer.lastItemId()).toString())+", "+TasksView.ID_USER+")");
        statement.close();
        conn.commit();                   
		
		Board board = new Board(name);	
		return board;
	}
	
	public ArrayList<Board> getAllBoard() {
		ArrayList<Board> boards = new ArrayList<Board>();
		for (int i = 0; i < boardContainer.size(); i++) {
			Object id = boardContainer.getIdByIndex(i);
			Item item = boardContainer.getItem(id);
			Property listId = item.getItemProperty("id");
			Property name = item.getItemProperty("name");
			Board board = new Board((String) name.getValue());
			board.setName((String) name.getValue());
			board.setBoardId(listId.getValue().toString());
			boards.add(board);
		}

		return boards;
	}

}
