package com.paw.trelloplus.service;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.paw.trelloplus.components.List;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;

public abstract class AbstractService implements Serializable {

	protected static JDBCConnectionPool connectionPool;
	protected static Connection connection;
	private final static Logger logger =Logger.getLogger(AbstractService.class.getName());

	public AbstractService() {
		if(connectionPool==null || connection == null)
			connect();

	}
	private void connect(){
		logger.log(Level.SEVERE, "INFO: Connected to DB.");
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
}
