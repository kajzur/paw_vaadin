package com.example.trelloplus;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class List extends CustomComponent {

	private String id_list;
	private String id_board;
	private String name;

	public List(String title) {
		// this.setStyleName("list");

		HorizontalLayout l1 = new HorizontalLayout();
		l1.addComponent(new Label(title));
		l1.setStyleName("list-header");
		VerticalLayout vt = new VerticalLayout();

		vt.addComponent(l1);

		setCompositionRoot(vt);

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
