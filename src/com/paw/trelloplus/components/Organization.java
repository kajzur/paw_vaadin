package com.paw.trelloplus.components;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;

public class Organization extends CustomComponent {

	private static final long serialVersionUID = 8118933125234011569L;
	private String id;
	private String name;

	public Organization(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
