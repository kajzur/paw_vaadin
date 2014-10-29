package com.example.trelloplus;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class Task extends CustomComponent {

	private String id_list;
	private String title;
	private String desc;

	public Task(String title, String desc) {
		this.setStyleName("task");
		HorizontalLayout l1 = new HorizontalLayout();
		l1.addComponent(new Label(title));

		HorizontalLayout l2 = new HorizontalLayout();
		l2.addComponent(new Label(desc));

		VerticalLayout vt = new VerticalLayout();

		vt.addComponent(l1);
		vt.addComponent(l2);

		setCompositionRoot(vt);

	}

	public String getId_list() {
		return id_list;
	}

	public void setId_list(String id_list) {
		this.id_list = id_list;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
