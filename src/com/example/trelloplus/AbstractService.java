package com.example.trelloplus;

import java.sql.SQLException;

import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;

public abstract class AbstractService {

	protected JDBCConnectionPool connectionPool;

	public AbstractService() {
		try {
			connectionPool = new SimpleJDBCConnectionPool(
					"com.mysql.jdbc.Driver",
					"jdbc:mysql://localhost/trello_plus?useUnicode=true&characterEncoding=UTF-8",
					"root", "");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		initContainers();

	}

	protected abstract void initContainers();
}
