package com.paw.trelloplus.components;

import java.util.logging.Logger;

import com.paw.trelloplus.views.TasksView;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class Task extends CustomComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7689196414848406296L;
	private String id_list;
	private String task_id;
	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	private String title;
	private String desc;
	private final static Logger logger =
	          Logger.getLogger(TasksView.class.getName());

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
