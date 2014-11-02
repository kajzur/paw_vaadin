package com.example.trelloplus;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class List extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = -854281813374146888L;
	private String id_list;
	private String id_board;
	private String name;

	public List(String title) {

		VerticalLayout vt = new VerticalLayout();
		vt.addComponent(new Label(title));
		vt.setStyleName("list-header");
		addComponent(vt);

	}

	public String getId_board() {
		return id_board;
	}

	public void setId_board(String id_board) {
		this.id_board = id_board;
	}

	public String getId_list() {
		return id_list;
	}

	public void setId_list(String id_list) {
		this.id_list = id_list;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
