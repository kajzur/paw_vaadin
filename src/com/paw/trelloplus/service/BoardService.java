package com.paw.trelloplus.service;

import java.sql.SQLException;
import java.util.logging.Logger;

import com.paw.trelloplus.components.Board;
import com.vaadin.data.Item;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

public class BoardService extends AbstractService {

	private SQLContainer boardService;
	private final static Logger logger =
	          Logger.getLogger(BoardService.class.getName());
	@Override
	protected void initContainers() {
		try {
			TableQuery q1 = new TableQuery("boards", connectionPool);
			q1.setVersionColumn("VERSION");
			boardService = new SQLContainer(q1);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public Board addNew(String title, String user) throws UnsupportedOperationException, SQLException{

		Object rowId = boardService.addItem();
		Item rowItem = boardService.getItem(rowId);
		rowItem.getItemProperty("name").setValue(title);
		rowItem.getItemProperty("id_user").setValue(Integer.parseInt(user));

		boardService.commit();
		Board b = new Board(title);
		return b;
	}

}
