package com.paw.trelloplus.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
	private SQLContainer listsContainer;
	
	private final static Logger logger = Logger.getLogger(ListService.class.getName());
	
	public ListService() {
		super();
	
	}
	
	public List addList(int idBoard, String title, int idUser)
			throws UnsupportedOperationException, SQLException {
		
		Object rowId = listsContainer.addItem();
		Item rowItem = listsContainer.getItem(rowId);
		rowItem.getItemProperty("id_board").setValue(idBoard);
		rowItem.getItemProperty("name").setValue(title);
		listsContainer.commit();
		                  
		
		List list = new List(title);
		list.setId_board(idBoard+"");
		list.setId_list(((RowId) listsContainer.lastItemId()).toString());
		return list;
	}

	public ArrayList<List> getAllList() {
		ArrayList<List> lists = new ArrayList<List>();
		for (int i = 0; i < listsContainer.size(); i++) {
			Object id = listsContainer.getIdByIndex(i);
			Item item = listsContainer.getItem(id);
			Property id_list = item.getItemProperty("id_list");
			Property id_board = item.getItemProperty("id_board");
			Property name = item.getItemProperty("name");
			List list = new List((String) name.getValue());

			list.setId_list(id_list.getValue() + "");
			list.setId_board(id_board.getValue() + "");
			list.setName(name.getValue() + "");

			lists.add(list);
		}

		return lists;
	}

	
	@Override
	protected void initContainers()
	{
		
		try {
			TableQuery q1 = new TableQuery("lists", connectionPool);
			q1.setVersionColumn("VERSION");
			listsContainer = new SQLContainer(q1);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
