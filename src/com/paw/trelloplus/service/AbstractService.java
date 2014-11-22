package com.paw.trelloplus.service;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;

public abstract class AbstractService implements Serializable {

	protected static JDBCConnectionPool connectionPool;
	protected static Connection connection;
	public AbstractService() {
		if(connectionPool==null || connection == null)
			connect();
		initContainers();

	}
	private void connect(){
		try {
			connectionPool = new SimpleJDBCConnectionPool(
					"com.mysql.jdbc.Driver",
					"jdbc:mysql://localhost/trello_plus?useUnicode=true&characterEncoding=UTF-8",
					"root", "");
			connection = connectionPool.reserveConnection();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	protected abstract void initContainers();
}
