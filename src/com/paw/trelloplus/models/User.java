package com.paw.trelloplus.models;

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
	public boolean equals(Object obj) {
		if(!(obj instanceof User)) return false;
		User that = (User) obj;
		return that.getId().equals(this.getId());
	}
	


}
