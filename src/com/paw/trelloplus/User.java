package com.paw.trelloplus;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.paw.trelloplus.views.TasksView;

public class User implements Serializable{
	
	private final static Logger logger =
	          Logger.getLogger(User.class.getName());
	
	public User(String id, String login) {
		super();
		this.id = id;
		this.login = login;
	}
	private String id;
	private String login;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	@Override
	public String toString() {
		return login;
	}
	@Override
	public boolean equals(Object arg0) {
		logger.log(Level.SEVERE, arg0.toString());
		return super.equals(arg0);
	}

	


}
