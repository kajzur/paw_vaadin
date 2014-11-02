package com.example.trelloplus;

import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

public class ListService extends AbstractService {

	private SQLContainer listsContainer;

	public void addList(int id, String title)
			throws UnsupportedOperationException, SQLException {
		Object rowId = listsContainer.addItem();
		Item rowItem = listsContainer.getItem(rowId);
		rowItem.getItemProperty("id_board").setValue(id);
		rowItem.getItemProperty("name").setValue(title);

		listsContainer.commit();
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
	protected void initContainers() {
		try {

			TableQuery q3 = new TableQuery("lists", connectionPool);
			q3.setVersionColumn("VERSION");
			listsContainer = new SQLContainer(q3);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
